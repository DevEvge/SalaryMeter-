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

import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.GasData;
import com.example.jobtracker.database.MyApp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private TextView tvSalary;
    private int costPerPoint;
    private int departureFee;
    private double pricePerTone;
    private double salary;
    private ExecutorService executor;


    //TODO: Почистить код от ненужных импортов и переменных
    //TODO: Добавить просчет топлива
    //TODO: Убрать кнопку удаления базы данных
    //TODO: Очитстить все ошибки которые пишет IDE
    //TODO: Пересмотреть код\отрефакторить
    //TODO: Сделать что бы приложение нельзя было поворачивать
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

        MyApp.getDbExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<AppSettings> constants = MyApp.getDatabase().appSettingsDAO().getAll();
                if (constants.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, FirstAddConstants.class);
                    startActivity(intent);
                }
            }
        });

        Button buttonGas = findViewById(R.id.buttonNewGas);
        buttonGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewGasActivity.class);
                startActivity(intent);
            }
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
        buttonNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<DayData> dayDataByData = MyApp.getDatabase().dayDataDAO().getDayDataByData(currentDay);
                        if (dayDataByData.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, NewDataActivity.class);
                            startActivity(intent);
                        } else {
                            EditDataTodayModal dialogFragment = EditDataTodayModal.newInstance();
                            FragmentManager fm = getSupportFragmentManager();

                            dialogFragment.show(fm, "EditDataTodayModal");
                        }
                    }
                });


            }
        });

        Button buttonMyData = findViewById(R.id.buttonGetData);
        buttonMyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GetDataActivity.class);
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

    private void loadMonthlyData(String yearMonth) {
        MyApp.getDbExecutor().execute(() -> {
            List<DayData> records = MyApp.getDatabase().dayDataDAO().getAllByYearMonth(yearMonth);
            List<GasData> gasRecords = MyApp.getDatabase().gasDataDAO().getAllByYearMonth(yearMonth);


            double totalSalary = 0;
            for (DayData data : records) {
                salary += data.salary;
            }
            for (GasData data : gasRecords){
                salary -= data.totalFuelCost;
            }

            double finalTotalSalary = salary;
            runOnUiThread(() -> {
                tvSalary.setText(String.valueOf(finalTotalSalary));
            });

        });
    }


}