package com.pedro_rihor.listadetarefas;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements TarefaAdapter.CallbackInterface {
    final static String EXTRA_TAREFA = "com.pedro_rihor.listadetarefas.tarefa";
    private static final int MY_REQUEST = 1001;

    RecyclerView recyclerView;
    private TarefaViewModel tarefaViewModel;
    private EditText editTextInserir;
    public FloatingActionButton fab;
    Toolbar toolbar;
    TarefaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextInserir = findViewById(R.id.text_insert);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.button_add);
        recyclerView = findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar); // define a toolbar como a ActionBar
        fabClick();

        configRecyclerView();
    }

    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TarefaAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
        setTarefaViewModel();

        // implementação do swipe para remover elemento
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(MainActivity.this, adapter.getTarefaAt(viewHolder.getAdapterPosition()).getData(), Toast.LENGTH_SHORT).show();
                tarefaViewModel.delete(
                        adapter.getTarefaAt(viewHolder.getAdapterPosition())
                );
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnCheckboxListener(new TarefaAdapter.OnCheckboxListener() {
            @Override
            public void onCheckboxClick(Tarefa tarefa, boolean novoEstado) {
                tarefa.setEstado(novoEstado);
                tarefaViewModel.update(tarefa);
            }
        });
    }

    public void setTarefaViewModel() {
        tarefaViewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        tarefaViewModel.getLiveData().observe(this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tarefas) {
                adapter.submitList(tarefas);
            }
        });
    }

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String descricao = editTextInserir.getText().toString();
                if (descricao.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Escreva algo!", Toast.LENGTH_SHORT).show();
                } else {
                    // inserir tarefa no banco de dados
                    tarefaViewModel.insert(new Tarefa(descricao, false));
                    editTextInserir.setText(""); // reset text
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config_settings:
                //tarefaViewModel.deleteOlderThan("-1 day");
                // abre uma activity para mudar as configurações
                //TODO activity de configuração
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onHandleSelection(Tarefa tarefaSelecionada, int position, TarefaAdapter.TarefaHolder tarefaHolder) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        System.out.println("tarefaSelecionada.id: " + tarefaSelecionada.getId());
        intent.putExtra(EXTRA_TAREFA, tarefaSelecionada); // passagem do objeto Tarefa

        // define os pares, elementos para a transição
        Pair<View, String> pairText = Pair.create((View) tarefaHolder.textViewDescricao, tarefaHolder.textViewDescricao.getTransitionName());
        Pair<View, String> pairCard = Pair.create((View) tarefaHolder.cardView, tarefaHolder.cardView.getTransitionName());
        Pair<View, String> pairFab = Pair.create((View) fab, fab.getTransitionName());

        ActivityOptions transitionAnimation = ActivityOptions.makeSceneTransitionAnimation(
                this,
                pairFab,
                pairCard,
                pairText);
        // inicia a activity
        startActivityForResult(intent, MY_REQUEST, transitionAnimation.toBundle());
        // para o brilho branco que ocorre antes no meio da transição
        getWindow().setExitTransition(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == MY_REQUEST) {
                    if (data != null && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        Tarefa tarefa = bundle.getParcelable(MainActivity.EXTRA_TAREFA);

                        if (tarefa != null) {
                            tarefaViewModel.update(tarefa);
                        }
                    }
                }
                break;

            case RESULT_CANCELED:
                Toast.makeText(this, "As alterações não foram salvas!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
