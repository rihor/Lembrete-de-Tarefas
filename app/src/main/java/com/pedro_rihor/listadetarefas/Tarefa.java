package com.pedro_rihor.listadetarefas;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tarefas_table")
class Tarefa implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;

    private String data;

    private boolean estado;

    @ColumnInfo(name = "tempo")
    private String tempoNotificacao;

    // colunas para notificacao do dia
    private boolean domingo;
    private boolean segunda;
    private boolean terca;
    private boolean quarta;
    private boolean quinta;
    private boolean sexta;
    private boolean sabado;

    @SuppressLint("SimpleDateFormat")
    @Ignore
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    @Ignore
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tarefa> CREATOR = new Parcelable.Creator<Tarefa>() {
        @Override
        public Tarefa createFromParcel(Parcel in) {
            return new Tarefa(in);
        }

        @Override
        public Tarefa[] newArray(int size) {
            return new Tarefa[size];
        }
    };

    public Tarefa(String descricao, boolean estado) {
        this.descricao = descricao;
        this.data = dateFormat.format(Calendar.getInstance().getTime());
        this.estado = estado;
        this.tempoNotificacao = timeFormat.format(Calendar.getInstance().getTime());
        this.domingo = false;
        this.segunda = false;
        this.terca = false;
        this.quarta = false;
        this.quinta = false;
        this.sexta = false;
        this.sabado = false;
    }

    protected Tarefa(Parcel in) {
        descricao = in.readString();
        data = in.readString();
        estado = in.readByte() != 0x00;
        tempoNotificacao = in.readString();
        domingo = in.readByte() != 0x00;
        segunda = in.readByte() != 0x00;
        terca = in.readByte() != 0x00;
        quarta = in.readByte() != 0x00;
        quinta = in.readByte() != 0x00;
        sexta = in.readByte() != 0x00;
        sabado = in.readByte() != 0x00;
        dateFormat = (SimpleDateFormat) in.readValue(SimpleDateFormat.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getDescricao() {
        return descricao;
    }

    void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    String getData() {
        return data;
    }

    void setData(String data) {
        this.data = data;
    }

    boolean isEstado() {
        return estado;
    }

    void setEstado(boolean estado) {
        this.estado = estado;
    }

    String getTempoNotificacao() {
        return tempoNotificacao;
    }

    void setTempoNotificacao(String tempoNotificacao) {
        this.tempoNotificacao = tempoNotificacao;
    }

    boolean isDomingo() {
        return domingo;
    }

    void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    boolean isSegunda() {
        return segunda;
    }

    void setSegunda(boolean segunda) {
        this.segunda = segunda;
    }

    boolean isTerca() {
        return terca;
    }

    void setTerca(boolean terca) {
        this.terca = terca;
    }

    boolean isQuarta() {
        return quarta;
    }

    void setQuarta(boolean quarta) {
        this.quarta = quarta;
    }

    boolean isQuinta() {
        return quinta;
    }

    void setQuinta(boolean quinta) {
        this.quinta = quinta;
    }

    boolean isSexta() {
        return sexta;
    }

    void setSexta(boolean sexta) {
        this.sexta = sexta;
    }

    boolean isSabado() {
        return sabado;
    }

    void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descricao);
        dest.writeString(data);
        dest.writeByte((byte) (estado ? 0x01 : 0x00));
        dest.writeString(tempoNotificacao);
        dest.writeByte((byte) (domingo ? 0x01 : 0x00));
        dest.writeByte((byte) (segunda ? 0x01 : 0x00));
        dest.writeByte((byte) (terca ? 0x01 : 0x00));
        dest.writeByte((byte) (quarta ? 0x01 : 0x00));
        dest.writeByte((byte) (quinta ? 0x01 : 0x00));
        dest.writeByte((byte) (sexta ? 0x01 : 0x00));
        dest.writeByte((byte) (sabado ? 0x01 : 0x00));
        dest.writeValue(dateFormat);
    }

}
