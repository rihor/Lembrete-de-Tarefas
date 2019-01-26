package com.pedro_rihor.listadetarefas;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    Toolbar toolbar;
    TextView textoDescricao;
    CheckBox checkBox;
    CheckBox btnDomingo, btnSegunda, btnTerca, btnQuarta, btnQuinta, btnSexta, btnSabado;
    TimePicker timePicker;
    String time; // tempo pego na timePicker

    Bundle bundle;
    Intent intent;
    Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);   // define o layout
        getWindow().setEnterTransition(null);       // interrompe o flash na transição da activity
        intent = getIntent();
        bundle = intent.getExtras();
        time = "";
        // define o arquivo de transição
        getWindow().setSharedElementEnterTransition(
                TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));

        textoDescricao = findViewById(R.id.text_detail);
        checkBox = findViewById(R.id.checkbox_detail);
        btnDomingo = findViewById(R.id.btn_day_1);
        btnSegunda = findViewById(R.id.btn_day_2);
        btnTerca = findViewById(R.id.btn_day_3);
        btnQuarta = findViewById(R.id.btn_day_4);
        btnQuinta = findViewById(R.id.btn_day_5);
        btnSexta = findViewById(R.id.btn_day_6);
        btnSabado = findViewById(R.id.btn_day_7);
        timePicker = findViewById(R.id.timepicker);

        timePicker.setIs24HourView(true);

        btnDomingo.setOnClickListener(this);
        btnSegunda.setOnClickListener(this);
        btnTerca.setOnClickListener(this);
        btnQuarta.setOnClickListener(this);
        btnQuinta.setOnClickListener(this);
        btnSexta.setOnClickListener(this);
        btnSabado.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   // set toolbar

        getChoiceTimePicker();          // listener do TimePicker

        // evitar null pointer exception
        if (getIntent() != null && bundle != null) {
            tarefaSelecionada = bundle.getParcelable(MainActivity.EXTRA_TAREFA);
            Toast.makeText(this, tarefaSelecionada.isDomingo() + " domingo", Toast.LENGTH_SHORT).show();
            if (tarefaSelecionada != null) {
                textoDescricao.setText(tarefaSelecionada.getDescricao());
                checkBox.setChecked(tarefaSelecionada.isEstado());
                // recebe a string das horas e divide em hora e minuto, acaba com o array out of bounds exception
                String tempoSelecionado[] = tarefaSelecionada.getTempoNotificacao().split(":", 2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setHour(Integer.parseInt(tempoSelecionado[0]));
                    timePicker.setMinute(Integer.parseInt(tempoSelecionado[1]));
                } else {
                    timePicker.setCurrentHour(Integer.parseInt(tempoSelecionado[0]));
                    timePicker.setCurrentMinute(Integer.parseInt(tempoSelecionado[1]));
                }

                btnDomingo.setChecked(tarefaSelecionada.isDomingo());

                fab = findViewById(R.id.fab);   // set floating action button
                fabClick();                     // listener do Floating Action Button
            }
        } else {
            finish(); // não vai permitir entrar na activity
        }
    }

    void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                tarefaSelecionada.setTempoNotificacao(time);
                System.out.println("!!!!!fabclick " + tarefaSelecionada.getTempoNotificacao());
                resultIntent.putExtra(MainActivity.EXTRA_TAREFA, tarefaSelecionada);
                setResult(RESULT_OK, resultIntent);
                finishAfterTransition(); // fecha a activity assim que acabar a transição
            }
        });
    }

    public void getChoiceTimePicker() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time = hourOfDay + ":" + minute;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_day_1:
                tarefaSelecionada.setDomingo(btnDomingo.isChecked());
                break;
            case R.id.btn_day_2:
                tarefaSelecionada.setSegunda(btnSegunda.isChecked());
                break;
            case R.id.btn_day_3:
                tarefaSelecionada.setTerca(btnTerca.isChecked());
                break;
            case R.id.btn_day_4:
                tarefaSelecionada.setQuarta(btnQuarta.isChecked());
                break;
            case R.id.btn_day_5:
                tarefaSelecionada.setQuinta(btnQuinta.isChecked());
                break;
            case R.id.btn_day_6:
                tarefaSelecionada.setSexta(btnSexta.isChecked());
                break;
            case R.id.btn_day_7:
                tarefaSelecionada.setSabado(btnSabado.isChecked());
                break;
            default:
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // impede de voltar para a activity anterior
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
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
