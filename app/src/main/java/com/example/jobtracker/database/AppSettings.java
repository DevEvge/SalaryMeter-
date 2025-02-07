package com.example.jobtracker.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AppSettings {

    @PrimaryKey
    public int ID;
    public int costPerPoint;
    public int departureFee;
    public double pricePerTone;

    public AppSettings(int ID, int costPerPoint, int departureFee, double pricePerTone) {
        this.ID = ID;
        this.costPerPoint = costPerPoint;
        this.departureFee = departureFee;
        this.pricePerTone = pricePerTone;
    }
}
