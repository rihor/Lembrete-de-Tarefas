package com.pedro_rihor.listadetarefas;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// SELECT strftime('%d/%m/%Y', 'now');


@Entity(tableName = "tarefas_table")
public class Tarefa {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;

    private String data;

    private boolean estado;

    @Ignore
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Tarefa(String descricao, boolean estado) {
        this.descricao = descricao;
        this.estado = estado;
        this.data = dateFormat.format(Calendar.getInstance().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDate() {
        return data;
    }

    public void setDate(String data) {
        this.data = data;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
