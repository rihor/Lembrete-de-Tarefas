package com.pedro_rihor.listadetarefas;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TarefaDao {

    @Insert
    void insert(Tarefa tarefa);

    @Update
    void update(Tarefa tarefa);

    @Delete
    void delete(Tarefa tarefa);

    @Query("DELETE FROM tarefas_table")
    void deleteAll();

    @Query("SELECT * FROM tarefas_table")
    LiveData<List<Tarefa>> getAll();

}
