package com.pedro_rihor.listadetarefas;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements TarefaAdapter.CallbackInterface {
    public static final String BROADCAST = "com.pedro_rihor.android.action.broadcast";
    public static final String NOTIFICATION_SERVICE = "com.pedro_rihor.android.action.notification_service";
    public static final String EXTRA_NOTIFICATION = "com.pedro_rihor.listadetarefas.notification";
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
                Toast.makeText(MainActivity.this, "Tarefa excluida!", Toast.LENGTH_SHORT).show();
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

    void setNotificacao(Tarefa tarefa) {
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "Canal 1 de tarefas";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        Intent resultIntent = new Intent(this, MainActivity.class); // activity destino

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = builder
                .setContentTitle("Tarefa")
                .setContentText("Tarefa a ser feita!")
                .setSmallIcon(R.mipmap.ic_stat_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(android.R.color.holo_red_dark))
                .build();

        // intent para o receiver
        Intent intentBroadcast = new Intent(BROADCAST);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.EXTRA_TAREFA, tarefa);
        bundle.putParcelable(MainActivity.EXTRA_NOTIFICATION, notification);
        intentBroadcast.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentBroadcast, 0);
        //sendBroadcast(intentBroadcast);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15000, pendingIntent);
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
        intent.putExtra(EXTRA_TAREFA, tarefaSelecionada); // passagem do objeto Tarefa

        // define os pares, elementos para a transição
        //Pair<View, String> pairText = Pair.create((View) tarefaHolder.textViewDescricao, tarefaHolder.textViewDescricao.getTransitionName());
        Pair<View, String> pairCard = Pair.create((View) tarefaHolder.cardView, tarefaHolder.cardView.getTransitionName());
        Pair<View, String> pairFab = Pair.create((View) fab, fab.getTransitionName());

        ActivityOptions transitionAnimation = ActivityOptions.makeSceneTransitionAnimation(
                this,
                pairFab,
                pairCard);
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

                            // TODO alarm set up
                            setNotificacao(tarefa);
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
