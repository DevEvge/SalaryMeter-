package com.example.jobtracker.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DayData {
    @PrimaryKey(autoGenerate = true)
    public long ID;

    public int pointsCount;
    public int totalWeight;
    public String additionalPoints;
    public String date;

    public DayData(int pointsCount, int totalWeight, String additionalPoints, String date) {
        this.pointsCount = pointsCount;
        this.totalWeight = totalWeight;
        this.additionalPoints = additionalPoints;
        this.date = date;
    }
}
