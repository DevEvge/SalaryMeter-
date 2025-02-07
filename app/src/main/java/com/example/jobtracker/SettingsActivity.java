package com.example.jobtracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton buttonArrowBack = findViewById(R.id.arrow_back3);
        buttonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button buttonChangeConst = findViewById(R.id.buttonChangeConstant);
        buttonChangeConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditConstActivityModal dialogFragment = EditConstActivityModal.newInstance();
                FragmentManager fm = getSupportFragmentManager();

                dialogFragment.show(fm, "EditConstDialog");
            }
        });

        Button buttonDeleteDB = findViewById(R.id.buttonDeleteDB);
        buttonDeleteDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = MyApp.getDatabase();
                        db.dayDataDAO().deleteAll();
                    }
                });
                Toast.makeText(SettingsActivity.this, "База данных очищена", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}