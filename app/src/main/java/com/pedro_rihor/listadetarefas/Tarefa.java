package com.pedro_rihor.listadetarefas;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// SELECT strftime('%d/%m/%Y', 'now');


@Entity(tableName = "tarefas_table")
public class Tarefa {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;

    private String date;

    private boolean estado;


    @Ignore
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");


    /*  POSSIVEL SOLUÇÃO PARA CONVERTER STRING EM DATE, CASO EU PRECISE
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-09");
     */

    public Tarefa(String descricao, boolean estado) {
        this.descricao = descricao;
        this.estado = estado;
        this.date = dateFormat.format(Calendar.getInstance().getTime());
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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


}
