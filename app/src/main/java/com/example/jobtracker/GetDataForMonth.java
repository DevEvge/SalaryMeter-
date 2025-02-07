package com.example.jobtracker;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.List;
import java.util.Locale;

public class GetDataForMonth extends AppCompatActivity {

    private final int costPerPoint = 24;
    private final int departureFee = 900;
    private final double pricePerTone = 0.5;
    TextView pointsForMonth;
    LinearLayout blockwithtext;
    TextView noDataForMonthError;
    TextView totalWeightMonth;
    TextView additionalPointsMonth;
    TextView totalJobPaidMonth;
    private String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_for_month);

        ImageButton buttonArrowBack = findViewById(R.id.arrow_back3);
        buttonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDataForMonth.this, GetDataActivity.class);
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText inputForYear = findViewById(R.id.etYear);

        pointsForMonth = findViewById(R.id.edittextPointsForMonth);
        totalWeightMonth = findViewById(R.id.edittextTotalWeightMonth);
        additionalPointsMonth = findViewById(R.id.edittextAdditionalPointsMonth);
        totalJobPaidMonth = findViewById(R.id.totalJobPaidMonth);
        noDataForMonthError = findViewById(R.id.noDataForMonth);
        blockwithtext = findViewById(R.id.getDataForMonthResult);



        Button buttonLoadData = findViewById(R.id.buttonLoadData);
        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = spinner.getSelectedItemPosition() + 1;
                String  year = inputForYear.getText().toString();
                String pickedData = String.format(Locale.getDefault(),"%s-%02d", year, month);

                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(pickedData);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (records == null || records.isEmpty()) {
                                    noDataForMonthError.setVisibility(VISIBLE);
                                    blockwithtext.setVisibility(INVISIBLE);
                                } else {
                                    blockwithtext.setVisibility(VISIBLE);
                                    noDataForMonthError.setVisibility(INVISIBLE);
                                }
                            }
                        });

                        int pointsCount = 0;
                        int totalWeight = 0;
                        int additionalPoints = 0;
                        double salary = 0;

                        for (DayData data: records) {
                            pointsCount += data.pointsCount;
                            totalWeight += data.totalWeight;
                            additionalPoints += data.additionalPoints;
                            salary += departureFee + (costPerPoint * (pointsCount + additionalPoints)) + (totalWeight * pricePerTone);
                        }

                        pointsForMonth.setText(String.valueOf(pointsCount));
                        totalWeightMonth.setText(String.valueOf(totalWeight));
                        additionalPointsMonth.setText(String.valueOf(additionalPoints));
                        totalJobPaidMonth.setText(String.valueOf(salary));

                    }
                });



            }
        });




    }
}