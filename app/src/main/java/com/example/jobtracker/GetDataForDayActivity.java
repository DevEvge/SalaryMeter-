package com.example.jobtracker;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDataForDayActivity extends AppCompatActivity {

    private final int costPerPoint = 24;
    private final int departureFee = 900;
    private final double pricePerTone = 0.5;

    private TextView alertError;
    private LinearLayout dayDataLayout;

    private EditText editTextDate;
    private TextView pointsForDay;
    private TextView totalWeightForDay;
    private TextView additionalPointsForDay;
    private TextView totalJobCost;
    private TextView textViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_data_for_day);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageButton buttonArrowBack = findViewById(R.id.arrow_back2);
        buttonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDataForDayActivity.this, GetDataActivity.class);
                startActivity(intent);
            }
        });

        editTextDate = findViewById(R.id.inputToPickADate);
        textViewResults = findViewById(R.id.textViewResults);

        pointsForDay = findViewById(R.id.edittextPointsForDay);
        totalWeightForDay = findViewById(R.id.edittextTotalWeight);
        additionalPointsForDay = findViewById(R.id.edittextAdditionalPoints);
        totalJobCost = findViewById(R.id.totalJobPaid);

        alertError = findViewById(R.id.tvAlertNoRecords);
        dayDataLayout = findViewById(R.id.getDataForDayResult);


        editTextDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    GetDataForDayActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            // Форматируем выбранную дату. Обратите внимание, что selectedMonth + 1, так как месяцы нумеруются с 0.
                            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                    selectedYear, selectedMonth + 1, selectedDay);
                            editTextDate.setText(selectedDate);

                            alertError.setVisibility(INVISIBLE);
                            dayDataLayout.setVisibility(INVISIBLE);

                            loadDataForDate(selectedDate);
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void loadDataForDate(String data) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<DayData> records = MyApp.getDatabase().dayDataDAO().getDayDataByData(data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (records == null || records.isEmpty()) {
                            alertError.setVisibility(VISIBLE);
                        } else {
                            dayDataLayout.setVisibility(VISIBLE);
                        }
                    }
                });


                int pointsCount = 0;
                int totalWeight = 0;
                int additionalPoints = 0;
                double salary = 0;

                for (
                        DayData record : records) {
                    pointsCount = record.pointsCount;
                    totalWeight = record.totalWeight;
                    additionalPoints = record.additionalPoints;
                }

                salary = departureFee + (costPerPoint * (pointsCount + additionalPoints)) + (totalWeight * pricePerTone);

                pointsForDay.setText(String.valueOf(pointsCount));
                totalWeightForDay.setText(String.valueOf(totalWeight));
                additionalPointsForDay.setText(String.valueOf(additionalPoints));
                totalJobCost.setText(String.valueOf(salary));
            }
        });
    }
}