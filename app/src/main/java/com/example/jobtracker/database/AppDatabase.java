package com.example.jobtracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DayData.class, AppSettings.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DayDataDAO dayDataDAO();
    public abstract AppSettingsDAO appSettingsDAO();


}
