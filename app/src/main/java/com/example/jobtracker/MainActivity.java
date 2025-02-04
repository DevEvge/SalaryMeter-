package com.example.jobtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvSalary;
    private final int costPerPoint = 24;
    private final int departureFee = 900;
    private final double pricePerTone = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvSalary = findViewById(R.id.textViewSalary);
        loadMonthlyData();

        Button buttonNewData = findViewById(R.id.buttonNewData);
        buttonNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewDataActivity.class);
                startActivity(intent);
            }
        });

        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadMonthlyData() {
        MyApp.getDbExecutor().execute(() -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            String yearMonth = String.format(Locale.getDefault(), "%04d-%02d", year, month);
            List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(yearMonth);

            double totalSalary = 0;
            for (DayData data: records) {
                totalSalary += departureFee  + ( costPerPoint * ( data.pointsCount + data.additionalPoints )) + (data.totalWeight * pricePerTone);
            }
            double finalTotalSalary = totalSalary;
            runOnUiThread(() -> {
                tvSalary.setText(String.valueOf(finalTotalSalary));
            });

        });
    }



}