package com.example.jobtracker;

import android.content.Context;
import android.content.Intent;
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

import com.example.jobtracker.database.AdditionalDayData;
import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.MyApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdditionalWayActivity extends AppCompatActivity {

    private EditText addPoints;
    private EditText addWeight;
    private EditText addPaymentForWay;
    private EditText addPointsAdd;
    private Button addSaveButton;
    private ImageButton arrowBack;
    private AppDatabase db;
    private ExecutorService dbExecutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_additional_way);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addPoints = findViewById(R.id.inputPointsCountAdditional);
        addWeight = findViewById(R.id.inputTotalWeightAdditional);
        addPaymentForWay = findViewById(R.id.paymentForWayAdditional);
        addPointsAdd = findViewById(R.id.inputPointsAdditional);
        arrowBack = findViewById(R.id.arrow_back9);
        addSaveButton = findViewById(R.id.buttonSaveDataAdditional);
        db = MyApp.getDatabase();
        dbExecutor = MyApp.getDbExecutor();

        TextWatcher textWatcher = new TextWatcher() {
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
        addPoints.addTextChangedListener(textWatcher);
        addWeight.addTextChangedListener(textWatcher);
        addPaymentForWay.addTextChangedListener(textWatcher);

        arrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdditionalWayActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        addSaveButton.setOnClickListener(v -> {
            String wayPoints = addPoints.getText().toString();
            String wayWeight = addWeight.getText().toString();
            String wayPayment = addPaymentForWay.getText().toString();
            String wayAdditionalPoints = addPointsAdd.getText().toString();

            int points = !wayPoints.isEmpty() ? Integer.parseInt(wayPoints) : 0;
            double weight = !wayWeight.isEmpty() ? Double.parseDouble(wayWeight) : 0;
            double payment = !wayPayment.isEmpty() ? Double.parseDouble(wayPayment) : 0;
            int additionalPoints = !wayAdditionalPoints.isEmpty() ? Integer.parseInt(wayAdditionalPoints) : 0;

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            dbExecutor.execute(() -> {
                AppSettings constant = db.appSettingsDAO().getSettings();
                double salary = 0;

                salary = payment +
                        (constant.costPerPoint * (points + additionalPoints)) +
                        (weight * constant.pricePerTone);

                AdditionalDayData data = new AdditionalDayData(currentDate, salary, payment, weight, points, additionalPoints);
                db.additionalDayDataDAO().insert(data);

                runOnUiThread(() -> {
                    Toast.makeText(AdditionalWayActivity.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdditionalWayActivity.this, MainActivity.class);
                    startActivity(intent);
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

    private void checkInputFields() {
        String pointsStr = addPoints.getText().toString().trim();
        String totalWeightStr = addWeight.getText().toString().trim();
        String paymentForWay = addPaymentForWay.getText().toString().trim();

        boolean allFilled = !pointsStr.isEmpty() && !totalWeightStr.isEmpty() && !paymentForWay.isEmpty();
        addSaveButton.setEnabled(allFilled);
    }
}