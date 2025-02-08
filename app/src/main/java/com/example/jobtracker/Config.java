package com.example.jobtracker;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.List;

public class Config {

    private AppDatabase db;
    public static boolean edit = false;


    public static double totalSalary(int pointsCount, int totalWeight, int additionalPoints) {
        List<AppSettings> appSettings = MyApp.getDatabase().appSettingsDAO().getAll();

        double totalSalary = 0;
        for (AppSettings constant: appSettings) {
            totalSalary = constant.departureFee +
                    (constant.costPerPoint * (pointsCount + additionalPoints))
                    + (totalWeight * constant.pricePerTone);
        }

        return totalSalary;
    }
}


