package com.example.jobtracker.database;

import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "my_database_name"
        ).build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }


}
