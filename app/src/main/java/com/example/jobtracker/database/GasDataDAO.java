package com.example.jobtracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GasDataDAO {

    @Insert
    long insert(GasData data);

    @Query("SELECT * FROM GasData WHERE date LIKE :yearMonth || '%'")
    List<GasData> getAllByYearMonth(String yearMonth);

    @Query("DELETE FROM GasData")
    void deleteAll();

}
