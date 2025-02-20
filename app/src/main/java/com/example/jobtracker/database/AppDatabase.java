package com.example.jobtracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DayData.class, AppSettings.class, GasData.class, AdditionalDayData.class}, version = 10, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DayDataDAO dayDataDAO();
    public abstract AppSettingsDAO appSettingsDAO();
    public abstract GasDataDAO gasDataDAO();

    public abstract AdditionalDayDataDAO additionalDayDataDAO();

}
