package com.example.jobtracker.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DayData {

    @PrimaryKey
    @NonNull
    public String date;
    public int pointsCount;
    public int totalWeight;
    public int additionalPoints;


    public DayData(int pointsCount, int totalWeight, int additionalPoints, String date) {
        this.pointsCount = pointsCount;
        this.totalWeight = totalWeight;
        this.additionalPoints = additionalPoints;
        this.date = date;
    }
}
