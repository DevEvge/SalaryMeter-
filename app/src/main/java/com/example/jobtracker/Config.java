package com.example.jobtracker;


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


