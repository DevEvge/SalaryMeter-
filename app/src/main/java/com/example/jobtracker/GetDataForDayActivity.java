package com.example.jobtracker;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GetDataForDayActivity extends AppCompatActivity {

    private TextView alertError;
    private LinearLayout dayDataLayout;
    private String selectedDate;
    private EditText editTextDate;
    private TextView pointsForDay;
    private TextView totalWeightForDay;
    private TextView additionalPointsForDay;
    private TextView totalJobCost;
    private Button buttonChangeDataForDay;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_data_for_day);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = MyApp.getDatabase();


        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        ImageButton buttonArrowBack = findViewById(R.id.arrow_back2);
        buttonArrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(GetDataForDayActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        editTextDate = findViewById(R.id.inputToPickADate);
        pointsForDay = findViewById(R.id.edittextPointsForDay);
        totalWeightForDay = findViewById(R.id.edittextTotalWeight);
        additionalPointsForDay = findViewById(R.id.edittextAdditionalPoints);
        totalJobCost = findViewById(R.id.totalJobPaid);
        alertError = findViewById(R.id.tvAlertNoRecords);
        dayDataLayout = findViewById(R.id.getDataForDayResult);
        buttonChangeDataForDay = findViewById(R.id.buttonChangeDataForDay);

        buttonChangeDataForDay.setOnClickListener(v -> {
            EditDayDataModal dialogFragment = EditDayDataModal.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            dialogFragment.show(fm, "EditDayDataDialog");
        });

        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputFields();
            }
        };

        editTextDate.addTextChangedListener(inputWatcher);
        editTextDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    GetDataForDayActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                selectedYear, selectedMonth + 1, selectedDay);
                        Config.setCurrentData(selectedDate);
                        editTextDate.setText(selectedDate);

                        alertError.setVisibility(INVISIBLE);
                        dayDataLayout.setVisibility(INVISIBLE);

                        loadDataForDate(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void loadDataForDate(String data) {
        MyApp.getDbExecutor().execute(() -> {
            List<DayData> records = db.dayDataDAO().getDayDataByData(data);

            int pointsCount = 0;
            double totalWeight = 0;
            int additionalPoints = 0;
            double salary = 0;

            for (DayData record : records) {
                pointsCount = record.pointsCount;
                totalWeight = record.totalWeight;
                additionalPoints = record.additionalPoints;
                salary = record.salary;
            }

            int finalPointsCount = pointsCount;
            double finalTotalWeight = totalWeight;
            int finalAdditionalPoints = additionalPoints;
            double finalSalary = salary;

            runOnUiThread(() -> {
                if (records.isEmpty()) {
                    alertError.setVisibility(VISIBLE);
                } else {
                    dayDataLayout.setVisibility(VISIBLE);
                }

                pointsForDay.setText(String.valueOf(finalPointsCount));
                totalWeightForDay.setText(String.valueOf(finalTotalWeight));
                additionalPointsForDay.setText(String.valueOf(finalAdditionalPoints));
                totalJobCost.setText(String.valueOf(finalSalary));
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

    private void checkInputFields() {
        String pointsStr = editTextDate.getText().toString().trim();

        boolean allFilled = !pointsStr.isEmpty();
        buttonChangeDataForDay.setEnabled(allFilled);
    }
}