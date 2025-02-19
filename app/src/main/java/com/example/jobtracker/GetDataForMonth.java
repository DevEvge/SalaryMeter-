package com.example.jobtracker;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.GasData;
import com.example.jobtracker.database.MyApp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GetDataForMonth extends AppCompatActivity {

    TextView pointsForMonth;
    TextView totalGas;
    LinearLayout blockWithText;
    TextView noDataForMonthError;
    TextView totalWeightMonth;
    TextView additionalPointsMonth;
    TextView totalJobPaidMonth;
    private final String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_for_month);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        ImageButton buttonArrowBack = findViewById(R.id.arrow_back3);
        buttonArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(GetDataForMonth.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Spinner spinner = findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(month);

        EditText inputForYear = findViewById(R.id.etYear);
        inputForYear.setText(String.valueOf(year));

        pointsForMonth = findViewById(R.id.edittextPointsForMonth);
        totalWeightMonth = findViewById(R.id.edittextTotalWeightMonth);
        additionalPointsMonth = findViewById(R.id.edittextAdditionalPointsMonth);
        totalJobPaidMonth = findViewById(R.id.totalJobPaidMonth);
        noDataForMonthError = findViewById(R.id.noDataForMonth);
        blockWithText = findViewById(R.id.getDataForMonthResult);
        totalGas = findViewById(R.id.totalGasMonth);


        Button buttonLoadData = findViewById(R.id.buttonLoadData);
        buttonLoadData.setOnClickListener(v -> {
            int month1 = spinner.getSelectedItemPosition() + 1;
            String year1 = inputForYear.getText().toString();

            String pickedData = String.format(Locale.getDefault(), "%s-%02d", year1, month1);

            MyApp.getDbExecutor().execute(() -> {
                List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(pickedData);
                List<GasData> gasRecords = MyApp.getDatabase().gasDataDAO().getAllByYearMonth(pickedData);

                runOnUiThread(() -> {
                    if (records == null || records.isEmpty()) {
                        noDataForMonthError.setVisibility(VISIBLE);
                        blockWithText.setVisibility(INVISIBLE);
                    } else {
                        blockWithText.setVisibility(VISIBLE);
                        noDataForMonthError.setVisibility(INVISIBLE);
                    }
                });

                int totalPoints = 0;
                double totalWeight = 0;
                int totalAdditionalPoints = 0;
                double totalSalary = 0;
                double totalsGasCost = 0;

                for (DayData data : records) {
                    totalPoints += data.pointsCount;
                    totalWeight += data.totalWeight;
                    totalAdditionalPoints += data.additionalPoints;
                    totalSalary += data.salary;
                }
                for (GasData data : gasRecords) {
                    totalsGasCost += data.totalFuelCost;
                }

                int finalTotalPoints = totalPoints;
                String finalTotalWeight = String.format(Locale.US, "%.2f", totalWeight);
                int finalTotalAdditionalPoints = totalAdditionalPoints;
                double finalTotalGasCost = totalsGasCost;
                double finalTotalSalary = totalSalary - totalsGasCost;

                runOnUiThread(() -> {
                    pointsForMonth.setText(String.valueOf(finalTotalPoints));
                    totalWeightMonth.setText(finalTotalWeight);
                    additionalPointsMonth.setText(String.valueOf(finalTotalAdditionalPoints));
                    totalJobPaidMonth.setText(String.format(Locale.getDefault(), "%.2f", finalTotalSalary));
                    totalGas.setText(String.format(Locale.getDefault(), "%.2f", finalTotalGasCost));
                });
            });
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}