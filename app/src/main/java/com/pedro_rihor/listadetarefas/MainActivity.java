package com.pedro_rihor.listadetarefas;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private TarefaViewModel tarefaViewModel;
    private EditText editTextInserir;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // componentes
        toolbar = findViewById(R.id.toolbar);
        editTextInserir = findViewById(R.id.text_insert);
        fab = findViewById(R.id.button_add);
        fabClick();

        toolbar.setTitleTextAppearance(this, R.style.titulo);
        setSupportActionBar(toolbar); // define a toolbar como a ActionBar


        configRecyclerView();
    }

    private void configRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TarefaAdapter adapter = new TarefaAdapter();
        recyclerView.setAdapter(adapter);

        tarefaViewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        tarefaViewModel.getLiveData().observe(this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tarefas) {
                adapter.submitList(tarefas);
            }
        });

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
                Toast.makeText(MainActivity.this, adapter.getTarefaAt(viewHolder.getAdapterPosition()).getDate(), Toast.LENGTH_SHORT).show();
                tarefaViewModel.delete(
                        adapter.getTarefaAt(viewHolder.getAdapterPosition())
                );
                //Toast.makeText(MainActivity.this, "tarefa removida com sucesso!", Toast.LENGTH_SHORT).show();
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

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String descricao = editTextInserir.getText().toString();
                if (descricao.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Escreva algo!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // inserir tarefa no banco de dados
                    tarefaViewModel.insert(
                            new Tarefa(editTextInserir.getText().toString(), false)
                    );
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
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
