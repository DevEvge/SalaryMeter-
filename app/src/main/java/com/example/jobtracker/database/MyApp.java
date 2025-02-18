package com.example.jobtracker.database;

import android.app.Application;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    private static AppDatabase database;
    private static ExecutorService dbExecutor;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "my_database_name"
        ).build();

        dbExecutor = Executors.newSingleThreadExecutor();
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static ExecutorService getDbExecutor() {
        return dbExecutor;
    }




}
