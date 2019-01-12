package com.pedro_rihor.listadetarefas;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TarefaRepository {
    private TarefaDao tarefaDao;
    private LiveData<List<Tarefa>> liveData;

    public TarefaRepository(Application application) {
        TarefaDatabase database = TarefaDatabase.getInstance(application);
        tarefaDao = database.tarefaDao();
        liveData = tarefaDao.getAll();
    }

    public void insert(Tarefa tarefa) {
        new InsertTarefaAsyncTask(tarefaDao).execute(tarefa);
    }

    public void update(Tarefa tarefa) {
        new UpdateTarefaAsyncTask(tarefaDao).execute(tarefa);
    }

    public void delete(Tarefa tarefa) {
        new DeleteTarefaAsyncTask(tarefaDao).execute(tarefa);
    }

    public void deleteAll() {
        new DeleteAllTarefasAsyncTask(tarefaDao).execute();
    }

    public LiveData<List<Tarefa>> getLiveData() {
        return liveData;
    }

    private static class InsertTarefaAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao tarefaDao;

        private InsertTarefaAsyncTask(TarefaDao tarefaDao) {
            this.tarefaDao = tarefaDao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            tarefaDao.insert(tarefas[0]);
            return null;
        }
    }

    private static class UpdateTarefaAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao tarefaDao;

        private UpdateTarefaAsyncTask(TarefaDao tarefaDao) {
            this.tarefaDao = tarefaDao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            tarefaDao.update(tarefas[0]);
            return null;
        }
    }

    private static class DeleteTarefaAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao tarefaDao;

        private DeleteTarefaAsyncTask(TarefaDao tarefaDao) {
            this.tarefaDao = tarefaDao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            tarefaDao.delete(tarefas[0]);
            return null;
        }
    }

    private static class DeleteAllTarefasAsyncTask extends AsyncTask<Void, Void, Void> {
        private TarefaDao tarefaDao;

        private DeleteAllTarefasAsyncTask(TarefaDao tarefaDao) {
            this.tarefaDao = tarefaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tarefaDao.deleteAll();
            return null;
        }
    }

}
