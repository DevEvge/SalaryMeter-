package com.example.jobtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class GetDataForMonthActivity extends AppCompatActivity {

    private EditText editTextDate;
    private TextView textViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_data_for_month);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageButton buttonArrowBack = findViewById(R.id.arrow_back2);
        buttonArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDataForMonthActivity.this, GetDataActivity.class);
                startActivity(intent);
            }
        });

        editTextDate = findViewById(R.id.inputToPickADate);
        textViewResults = findViewById(R.id.textViewResults);


        editTextDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    GetDataForMonthActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            // Форматируем выбранную дату. Обратите внимание, что selectedMonth + 1, так как месяцы нумеруются с 0.
                            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                                    selectedYear, selectedMonth + 1, selectedDay);
                            editTextDate.setText(selectedDate);

                            // После выбора даты можно выполнить запрос к базе данных,
                            // передав выбранную дату в качестве параметра.
                            // Например:
//                            loadDataForDate(selectedDate);
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

}