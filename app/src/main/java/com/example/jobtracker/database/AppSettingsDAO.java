package com.example.jobtracker.database;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppSettingsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AppSettings data);

    @Query("SELECT * FROM AppSettings")
    List<AppSettings> getAll();

}
