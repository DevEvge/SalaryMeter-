package com.example.jobtracker;

import static android.view.View.VISIBLE;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewDataActivity extends AppCompatActivity {

    private EditText inputPointsCount, inputTotalWeight, inputAdditional;
    private Button buttonSaveNewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Устанавливаем отступы от системных окон
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(R.color.app_background));
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_background));

        // Инициализация элементов
        inputPointsCount = findViewById(R.id.inputPointsCount);
        inputTotalWeight = findViewById(R.id.inputTotalWeight);
        inputAdditional = findViewById(R.id.additionalInput);
        buttonSaveNewData = findViewById(R.id.buttonSaveNewData);
        ImageButton arrowBack = findViewById(R.id.arrow_back);
        SwitchCompat switchBTN = findViewById(R.id.switchbtn);
        TextView warningText = findViewById(R.id.warningMassage);

        // Если глобальный флаг Config.edit установлен, показываем предупреждение
        if (Config.edit) {
            warningText.setVisibility(VISIBLE);
        }

        // Изначально кнопка неактивна
        buttonSaveNewData.setEnabled(false);

        // Создаем TextWatcher для проверки заполненности полей
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ничего не делаем
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ничего не делаем
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkInputFields();
            }
        };

        // Привязываем TextWatcher к каждому EditText
        inputPointsCount.addTextChangedListener(inputWatcher);
        inputTotalWeight.addTextChangedListener(inputWatcher);
        inputAdditional.addTextChangedListener(inputWatcher);

        // Обработка нажатия на стрелку "назад"
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDataActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Переключатель для включения/отключения ввода дополнительных точек
        switchBTN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                inputAdditional.setEnabled(isChecked);
                // Если выключили, можно сбросить значение и установить прозрачность
                if (!isChecked) {
                    inputAdditional.setText("");
                    inputAdditional.setAlpha(0.5f);
                } else {
                    inputAdditional.setAlpha(1.0f);
                }
            }
        });

        // Обработка нажатия на кнопку сохранения
        buttonSaveNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointsCountStr = inputPointsCount.getText().toString();
                String totalWeightStr = inputTotalWeight.getText().toString();
                String additionalPointsStr = inputAdditional.getText().toString();

                int pointsCount = !pointsCountStr.isEmpty() ? Integer.parseInt(pointsCountStr) : 0;
                double totalWeight = !totalWeightStr.isEmpty() ? Double.parseDouble(totalWeightStr) : 0;
                int additionalPoints = !additionalPointsStr.isEmpty() ? Integer.parseInt(additionalPointsStr) : 0;

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(new Date());

                // Запускаем вычисление зарплаты в фоне
                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Получаем константы из базы
                        List<AppSettings> settingsList = MyApp.getDatabase().appSettingsDAO().getAll();
                        double salary = 0;
                        if (!settingsList.isEmpty()) {
                            // Предполагаем, что в таблице хранится только одна запись
                            AppSettings constant = settingsList.get(0);
                            salary = constant.departureFee +
                                    (constant.costPerPoint * (pointsCount + additionalPoints)) +
                                    (totalWeight * constant.pricePerTone);
                        }

                        // Сохраняем данные в базе (запись дня с рассчитанной зарплатой)
                        DayData data = new DayData(pointsCount, totalWeight, additionalPoints, currentDate, salary);
                        MyApp.getDatabase().dayDataDAO().insert(data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewDataActivity.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewDataActivity.this, MainActivity.class);
                                startActivity(intent);
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

    // Метод для проверки заполненности полей ввода
    private void checkInputFields() {
        String pointsStr = inputPointsCount.getText().toString().trim();
        String totalWeightStr = inputTotalWeight.getText().toString().trim();


        // Если все поля непустые, кнопка активна, иначе — неактивна
        boolean allFilled = !pointsStr.isEmpty() && !totalWeightStr.isEmpty();
        buttonSaveNewData.setEnabled(allFilled);
    }
}