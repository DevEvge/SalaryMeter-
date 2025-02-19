package com.example.jobtracker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.MyApp;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        ImageButton buttonArrowBack = findViewById(R.id.arrow_back3);
        buttonArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Button buttonChangeConst = findViewById(R.id.buttonChangeConstant);
        buttonChangeConst.setOnClickListener(v -> {
            EditConstActivityModal dialogFragment = EditConstActivityModal.newInstance();
            FragmentManager fm = getSupportFragmentManager();

            dialogFragment.show(fm, "EditConstDialog");
        });

        Button buttonDeleteDB = findViewById(R.id.buttonDeleteDB);
        buttonDeleteDB.setOnClickListener(v -> {
            MyApp.getDbExecutor().execute(() -> {
                AppDatabase db = MyApp.getDatabase();
                db.dayDataDAO().deleteAll();
                db.appSettingsDAO().deleteAll();
                db.gasDataDAO().deleteAll();
            });
            Toast.makeText(SettingsActivity.this, "База данных очищена", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Button buttonEditDB = findViewById(R.id.buttonEditDB);
        buttonEditDB.setOnClickListener(v -> {
            buttonDeleteDB.setEnabled(true);
            new Handler().postDelayed(() -> buttonDeleteDB.setEnabled(false), 5000);
        });
    }
}