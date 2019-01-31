package com.pedro_rihor.listadetarefas;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("delete from tarefas_table where  data <= date('now', :tempo) ")
    int deleteOlderThan(String tempo);

    @Query("SELECT * FROM tarefas_table ORDER BY data")
    LiveData<List<Tarefa>> getAll();

}
