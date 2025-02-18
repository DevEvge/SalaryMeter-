package com.example.jobtracker;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.List;

public class Config {

    public static boolean edit = false;

    public static String currentData;

    public static void setCurrentData(String currentData) {
        Config.currentData = currentData;
    }

    public static String getCurrentData() {
        return currentData;
    }
}


