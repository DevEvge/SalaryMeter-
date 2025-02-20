package com.example.jobtracker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.jobtracker.database.AdditionalDayData;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.GasData;
import com.example.jobtracker.database.MyApp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {
    private TextView tvSalary;

    //TODO: Почистить код от ненужных импортов и переменных
    //TODO: Убрать кнопку удаления базы данных
    //TODO: Очитстить все ошибки которые пишет IDE
    //TODO: Пересмотреть код\отрефакторить
    //TODO: При двойном клике быстром страничка открывается два раза
    //TODO: При открытии приложения со свернутого состояния обновлять информацию на экране

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        MyApp.getDbExecutor().execute(() -> {
            AppSettings constants = MyApp.getDatabase().appSettingsDAO().getSettings();
            if (constants == null) {
                Intent intent = new Intent(MainActivity.this, FirstAddConstants.class);
                startActivity(intent);
            }
        });

        Button buttonGas = findViewById(R.id.buttonNewGas);
        buttonGas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewGasActivity.class);
            startActivity(intent);
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String yearMonth = String.format(Locale.getDefault(), "%04d-%02d", year, month);
        String currentDay = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);

        TextView dataForMainScreen = findViewById(R.id.dataMain);
        dataForMainScreen.setText(currentDay);

        tvSalary = findViewById(R.id.textViewSalary);
        loadMonthlyData(yearMonth);

        Button buttonNewData = findViewById(R.id.buttonGetDataForDay);
        buttonNewData.setOnClickListener(v -> MyApp.getDbExecutor().execute(() -> {
            List<DayData> dayDataByData = MyApp.getDatabase().dayDataDAO().getDayDataByData(currentDay);
            if (dayDataByData.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, NewDataActivity.class);
                startActivity(intent);
            } else {
                EditDataTodayModal dialogFragment = EditDataTodayModal.newInstance();
                FragmentManager fm = getSupportFragmentManager();

                dialogFragment.show(fm, "EditDataTodayModal");
            }
        }));

        Button buttonMyData = findViewById(R.id.buttonGetData);
        buttonMyData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GetDataActivity.class);
            startActivity(intent);
        });

        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String yearMonth = String.format(Locale.getDefault(), "%04d-%02d", year, month);
        loadMonthlyData(yearMonth);
    }

    private void loadMonthlyData(String yearMonth) {
        MyApp.getDbExecutor().execute(() -> {
            double salary = 0;
            List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(yearMonth);
            List<GasData> gasRecords = MyApp.getDatabase().gasDataDAO().getAllByYearMonth(yearMonth);
            List<AdditionalDayData> addWayRecords = MyApp.getDatabase().additionalDayDataDAO().getAllByYearMonth(yearMonth);

            for (DayData data : records) {
                salary += data.salary;
            }
            for (AdditionalDayData data: addWayRecords){
                salary += data.salary;
            }
            for (GasData data : gasRecords) {
                salary -= data.totalFuelCost;
            }

            String finalTotalSalary = String.format(Locale.US, "%.2f", salary);
            runOnUiThread(() -> {
                tvSalary.setText(finalTotalSalary);
            });
        });
    }
}