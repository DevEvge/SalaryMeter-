package com.example.jobtracker.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GasData {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String date;
    public double totalFuelCost;

    public GasData (String date, double totalFuelCost) {
        this.date = date;
        this.totalFuelCost = totalFuelCost;
    }
}
