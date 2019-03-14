package com.pedro_rihor.listadetarefas;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedro_rihor.listadetarefas.Settings.SettingsActivity;
import com.pedro_rihor.listadetarefas.Settings.SharedPref;
import com.pedro_rihor.listadetarefas.Settings.Themes;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    FloatingActionButton fab;
    Toolbar toolbar;
    TextView textoDescricao;
    CheckBox checkBox;
    CheckBox btnDomingo, btnSegunda, btnTerca, btnQuarta, btnQuinta, btnSexta, btnSabado;
    TimePicker timePicker;
    String tempoEscolhido; // tempo pego na timePicker
    SharedPref sharedPref;
    Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        loadTheme(); // define o tema de acordo com a preferencia guardada
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);   // define o layout
        getWindow().setEnterTransition(null);       // interrompe o flash na transição da activity
        getWindow().setSharedElementEnterTransition(// define a transição
                TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);       // set toolbar

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
        fab = findViewById(R.id.fab);

        timePicker.setIs24HourView(true);   // retira o AM/PM

        btnDomingo.setOnClickListener(this);
        btnSegunda.setOnClickListener(this);
        btnTerca.setOnClickListener(this);
        btnQuarta.setOnClickListener(this);
        btnQuinta.setOnClickListener(this);
        btnSexta.setOnClickListener(this);
        btnSabado.setOnClickListener(this);

        getChoiceTimePicker();          // listener do TimePicker

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tarefaSelecionada = bundle.getParcelable(MainActivity.EXTRA_TAREFA);
            if (tarefaSelecionada == null) {
                finish();
            }

            textoDescricao.setText(tarefaSelecionada.getDescricao());
            checkBox.setChecked(tarefaSelecionada.isEstado());

            btnDomingo.setChecked(tarefaSelecionada.isDomingo());
            btnSegunda.setChecked(tarefaSelecionada.isSegunda());
            btnTerca.setChecked(tarefaSelecionada.isTerca());
            btnQuarta.setChecked(tarefaSelecionada.isQuarta());
            btnQuinta.setChecked(tarefaSelecionada.isQuinta());
            btnSexta.setChecked(tarefaSelecionada.isSexta());
            btnSabado.setChecked(tarefaSelecionada.isSabado());

            String tempoTarefaArray[];
            try {
                tempoTarefaArray = tarefaSelecionada.getTempoNotificacao().split(":"); // dividir as horas, em HORA e MINUTO
            } catch (NullPointerException ex) {
                Calendar calendar = Calendar.getInstance();
                String hour = String.valueOf(calendar.get(Calendar.HOUR));
                String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                tempoTarefaArray = new String[]{hour, minute};
                Log.e(TAG, ex.getMessage());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(Integer.parseInt(tempoTarefaArray[0]));
                timePicker.setMinute(Integer.parseInt(tempoTarefaArray[1]));
            } else {
                timePicker.setCurrentHour(Integer.parseInt(tempoTarefaArray[0]));
                timePicker.setCurrentMinute(Integer.parseInt(tempoTarefaArray[1]));
            }
            checkBoxChange();
            fabClick();
        } else {
            finish();
        }
    }

    void checkBoxChange() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tarefaSelecionada.setEstado(isChecked);
            }
        });
    }

    public void loadTheme() {
        switch (sharedPref.getTheme()) {
            case Themes.VERDE:
                setTheme(R.style.GreenTheme);
                break;
            case Themes.BRANCO:
                setTheme(R.style.WhiteTheme);
                break;
            case Themes.PRETO:
                setTheme(R.style.BlackTheme);
                break;
            case Themes.AZUL:
            default:
                setTheme(R.style.BlueTheme);
        }
    }

    void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultIntent = new Intent();
                tarefaSelecionada.setTempoNotificacao(tempoEscolhido);
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
                tempoEscolhido = hourOfDay + ":" + minute;
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
                // TODO abre uma activity para mudar as configurações
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
