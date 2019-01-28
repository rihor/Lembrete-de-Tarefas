package com.pedro_rihor.listadetarefas;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Tarefa.class, version = 9, exportSchema = false)
public abstract class TarefaDatabase extends RoomDatabase {

    private static TarefaDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized TarefaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TarefaDatabase.class, "tarefa_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract TarefaDao tarefaDao();
/*
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TarefaDao tarefaDao;

        private PopulateDbAsyncTask(TarefaDatabase db) {
            tarefaDao = db.tarefaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

    }
*/
}
