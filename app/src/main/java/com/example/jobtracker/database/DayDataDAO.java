package com.example.jobtracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DayDataDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DayData data);

    @Query("SELECT * FROM DayData")
    List<DayData> getAll();

    @Query("SELECT * FROM DayData WHERE date LIKE :yearMonth || '%'")
    List<DayData> getAllByYearMonth(String yearMonth);

    @Query("SELECT * FROM DayData WHERE date = :data")
    List<DayData> getDayDataByData(String data);

    @Query("DELETE FROM DayData")
    void deleteAll();
}
