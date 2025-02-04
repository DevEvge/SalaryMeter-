package com.example.jobtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        Button button = findViewById(R.id.buttonSaveNewData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointsCount = inputPointsCount.getText().toString();
                String  totalWeight = inputWeithtTotal.getText().toString();
                String additionalPoints = inputAdditional.getText().toString();


            }
        });


    }
}