    package com.example.jobtracker.database;

    import androidx.room.Dao;
    import androidx.room.Insert;
    import androidx.room.OnConflictStrategy;
    import androidx.room.Query;

    @Dao
    public interface AppSettingsDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(AppSettings data);

        @Query("SELECT * FROM AppSettings")
        AppSettings getSettings();

        @Query("DELETE FROM AppSettings")
        void deleteAll();
    }
