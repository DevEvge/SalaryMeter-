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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.MyApp;

public class FirstAddConstants extends AppCompatActivity {
    private EditText costPerPointConst;
    private EditText departureFeeConst;
    private EditText pricePerTon;
    private Button buttonSaveNewConst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_add_constants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));

        costPerPointConst = findViewById(R.id.costPerPointConst1);
        departureFeeConst = findViewById(R.id.departureFeeConst1);
        pricePerTon = findViewById(R.id.pricePerTon1);
        buttonSaveNewConst = findViewById(R.id.buttonSaveConst1);

        TextWatcher watcher = new TextWatcher() {
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

        costPerPointConst.addTextChangedListener(watcher);
        departureFeeConst.addTextChangedListener(watcher);
        pricePerTon.addTextChangedListener(watcher);

        buttonSaveNewConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int costPerPoint = Integer.parseInt(costPerPointConst.getText().toString());
                int deparyureFee = Integer.parseInt(departureFeeConst.getText().toString());
                double pricePerTona = Double.parseDouble(pricePerTon.getText().toString());

                AppSettings appSettings = new AppSettings(1, costPerPoint, deparyureFee, pricePerTona);
                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase database = MyApp.getDatabase();
                        database.appSettingsDAO().insert(appSettings);
                    }
                });
                Intent intent = new Intent(FirstAddConstants.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    private void checkInputFields() {
        String costStr = costPerPointConst.getText().toString().trim();
        String departureStr = departureFeeConst.getText().toString().trim();
        String priceStr = pricePerTon.getText().toString().trim();

        boolean allFilled = !costStr.isEmpty() && !departureStr.isEmpty() && !priceStr.isEmpty();
        buttonSaveNewConst.setEnabled(allFilled);
    }
}