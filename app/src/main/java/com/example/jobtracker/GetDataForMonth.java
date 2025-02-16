package com.example.jobtracker;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.List;
import java.util.Locale;

public class GetDataForMonth extends AppCompatActivity {

    private int costPerPoint;
    private int departureFee;
    private double pricePerTone;
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

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));

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
                String year = inputForYear.getText().toString();

                // Формируем строку вида "yyyy-MM", например "2025-05"
                String pickedData = String.format(Locale.getDefault(), "%s-%02d", year, month);

                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        // Получаем все записи за выбранный месяц
                        List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(pickedData);

                        // Обновляем UI: если записей нет, показываем сообщение об отсутствии данных
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

                        // Агрегация данных:
                        // Так как зарплата уже сохранена для каждого дня, просто суммируем её
                        int totalPoints = 0;
                        int totalWeight = 0;
                        int totalAdditionalPoints = 0;
                        double totalSalary = 0;

                        for (DayData data : records) {
                            totalPoints += data.pointsCount;
                            totalWeight += data.totalWeight;
                            totalAdditionalPoints += data.additionalPoints;
                            totalSalary += data.salary;
                        }

                        // Обновляем UI с итоговыми значениями
                        int finalTotalPoints = totalPoints;
                        int finalTotalWeight = totalWeight;
                        int finalTotalAdditionalPoints = totalAdditionalPoints;
                        double finalTotalSalary = totalSalary;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pointsForMonth.setText(String.valueOf(finalTotalPoints));
                                totalWeightMonth.setText(String.valueOf(finalTotalWeight));
                                additionalPointsMonth.setText(String.valueOf(finalTotalAdditionalPoints));
                                totalJobPaidMonth.setText(String.format(Locale.getDefault(), "%.2f", finalTotalSalary));
                            }
                        });
                    }
                });
            }
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
                    // Если касание вне EditText – снимаем фокус и скрываем клавиатуру
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