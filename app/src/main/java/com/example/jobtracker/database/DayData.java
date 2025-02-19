package com.example.jobtracker.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DayData {

    @PrimaryKey
    @NonNull
    public String date;
    public double salary;
    public int pointsCount;
    public double totalWeight;
    public int additionalPoints;


    public DayData(int pointsCount, double totalWeight, int additionalPoints, String date, double salary) {
        this.pointsCount = pointsCount;
        this.totalWeight = totalWeight;
        this.salary = salary;
        this.additionalPoints = additionalPoints;
        this.date = date;
    }
}
