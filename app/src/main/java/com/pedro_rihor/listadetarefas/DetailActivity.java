package com.pedro_rihor.listadetarefas;


import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Toolbar toolbar;
    TextView textoDescricao, textoNumero;
    CheckBox checkBox;
    CheckBox btnDay1, btnDay2, btnDay3, btnDay4, btnDay5, btnDay6, btnDay7;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setEnterTransition(null);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        getWindow().setSharedElementEnterTransition(
                TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));

        textoDescricao = findViewById(R.id.text_detail);
        checkBox = findViewById(R.id.checkbox_detail);
        textoNumero = findViewById(R.id.text_numero_detail);
        btnDay1 = findViewById(R.id.radio_day_1);
        btnDay2 = findViewById(R.id.radio_day_2);
        btnDay3 = findViewById(R.id.radio_day_3);
        btnDay4 = findViewById(R.id.radio_day_4);
        btnDay5 = findViewById(R.id.radio_day_5);
        btnDay6 = findViewById(R.id.radio_day_6);
        btnDay7 = findViewById(R.id.radio_day_7);
        timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);

        if (bundle != null) {
            textoDescricao.setText(bundle.getString(TarefaAdapter.EXTRA_DESCRICAO));
            checkBox.setChecked(bundle.getBoolean(TarefaAdapter.EXTRA_CHECKBOX));
            textoNumero.setText(bundle.getString(TarefaAdapter.EXTRA_NUMERO));
        } else {
            finish(); // acaba a activity
        }

        // set toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set floating action button
        fab = findViewById(R.id.fab);
        fabClick();
    }

    void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
