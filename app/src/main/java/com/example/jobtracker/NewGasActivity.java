package com.example.jobtracker;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.GasData;
import com.example.jobtracker.database.MyApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewGasActivity extends AppCompatActivity {

    private EditText etGasPrice;
    private EditText etGasAmount;
    private Button btnSave;
    private ImageButton arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_gas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etGasPrice = findViewById(R.id.edittextGasPrice);
        etGasAmount = findViewById(R.id.edittextGasAmount);
        btnSave = findViewById(R.id.buttonSaveNewGas);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        arrowBack = findViewById(R.id.arrow_back5);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGasActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
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

        etGasAmount.addTextChangedListener(inputWatcher);
        etGasPrice.addTextChangedListener(inputWatcher);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(new Date());

                String gasAmount = etGasAmount.getText().toString();
                String gasPrice = etGasPrice.getText().toString();

                double totalGasCost = Double.parseDouble(gasPrice) * Double.parseDouble(gasAmount);

                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        MyApp.getDatabase().gasDataDAO().insert(new GasData(currentDate, totalGasCost));
                    }
                });

                Toast.makeText(NewGasActivity.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewGasActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void checkInputFields() {
        String pointsStr = etGasPrice.getText().toString().trim();
        String totalWeightStr = etGasAmount.getText().toString().trim();

        boolean allFilled = !pointsStr.isEmpty() && !totalWeightStr.isEmpty();
        btnSave.setEnabled(allFilled);
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