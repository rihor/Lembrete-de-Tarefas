package com.pedro_rihor.listadetarefas;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TarefaViewModel extends AndroidViewModel {
    private TarefaRepository repository;
    private LiveData<List<Tarefa>> liveData;

    public TarefaViewModel(@NonNull Application application) {
        super(application);
        repository = new TarefaRepository(application);
        liveData = repository.getLiveData();
    }

    public void insert(Tarefa tarefa) {
        repository.insert(tarefa);
    }

    public void update(Tarefa tarefa) {
        repository.update(tarefa);
    }

    public void delete(Tarefa tarefa) {
        repository.delete(tarefa);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Tarefa>> getLiveData() {
        return liveData;
    }
}
