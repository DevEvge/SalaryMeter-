package com.example.jobtracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AdditionalDayDataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AdditionalDayData data);

    @Query("SELECT * FROM AdditionalDayData")
    AdditionalDayData getSettings();

    @Query("SELECT * FROM AdditionalDayData")
    List<AdditionalDayData> getAll();

    @Query("SELECT * FROM AdditionalDayData WHERE date LIKE :yearMonth || '%'")
    List<AdditionalDayData> getAllByYearMonth(String yearMonth);

    @Query("SELECT * FROM AdditionalDayData WHERE date = :data")
    List<AdditionalDayData> getDayDataByData(String data);

    @Query("DELETE FROM AdditionalDayData")
    void deleteAll();

}
