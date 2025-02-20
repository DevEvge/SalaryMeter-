package com.example.jobtracker.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class AdditionalDayData {

    @PrimaryKey
    @NonNull
    public String date;
    public double salary;
    public double paymentForWay;
    public int pointsCount;
    public double totalWeight;
    public int additionalPoints;

    public AdditionalDayData(@NonNull String date, double salary, double paymentForWay, double totalWeight, int pointsCount, int additionalPoints) {
        this.date = date;
        this.salary = salary;
        this.paymentForWay = paymentForWay;
        this.totalWeight = totalWeight;
        this.pointsCount = pointsCount;
        this.additionalPoints = additionalPoints;
    }


}
