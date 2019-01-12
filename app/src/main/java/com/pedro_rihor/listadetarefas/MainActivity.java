package com.pedro_rihor.listadetarefas;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private TarefaViewModel tarefaViewModel;
    // componentes
    private EditText editTextInserir;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // componentes
        editTextInserir = findViewById(R.id.text_insert);
        fab = findViewById(R.id.button_add);
        fabClick();

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
                tarefaViewModel.delete(
                        adapter.getTarefaAt(viewHolder.getAdapterPosition())
                );
                Toast.makeText(MainActivity.this, "tarefa removida com sucesso!", Toast.LENGTH_SHORT).show();
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
                // abre uma activity para mudar as configurações
                Toast.makeText(this, "Não implementado ainda!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
