package com.example.jobtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewDataActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        EditText inputAdditional = findViewById(R.id.additionalInput);
        EditText inputPointsCount = findViewById(R.id.inputPointsCount);
        EditText inputWeithtTotal = findViewById(R.id.inputTotalWeight);
        Button buttonSaveNewData = findViewById(R.id.buttonSaveNewData);


        ImageButton arrowBack = findViewById(R.id.arrow_back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SwitchCompat switchBTN = findViewById(R.id.switchbtn);
        switchBTN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                inputAdditional.setEnabled(isChecked);
            }
        });


        buttonSaveNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointsCountStr = inputPointsCount.getText().toString();
                String  totalWeightStr = inputWeithtTotal.getText().toString();
                String additionalPointsStr = inputAdditional.getText().toString();

                int pointsCount = 0;
                int totalWeight = 0;
                int additionalPoints = 0;

                if (!pointsCountStr.isEmpty()){
                    pointsCount = Integer.parseInt(pointsCountStr);
                }
                if (!totalWeightStr.isEmpty()) {
                    totalWeight = Integer.parseInt(totalWeightStr);
                }
                if (!additionalPointsStr.isEmpty()){
                    additionalPoints = Integer.parseInt(additionalPointsStr);
                }

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(new Date());


                DayData data = new DayData(
                        pointsCount,
                        totalWeight,
                        additionalPoints,
                        currentDate
                );

                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = MyApp.getDatabase();
                        db.dayDataDAO().insert(data);
                    }
                });

                Toast.makeText(NewDataActivity.this, "Данные сохранены", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}