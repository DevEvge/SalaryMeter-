package com.example.jobtracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.DayData;
import com.example.jobtracker.database.MyApp;

import java.util.List;

public class EditDayDataModal extends DialogFragment {
    private String currentData;
    private EditText totalPoints;
    private EditText totalWeight;
    private EditText totalAddPoints;
    private Button buttonSave;

    public static EditDayDataModal newInstance() {
        return new EditDayDataModal();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_edit_daydata, container, false);

        currentData = Config.getCurrentData();
        totalPoints = view.findViewById(R.id.etPointsCount);
        totalWeight = view.findViewById(R.id.etWeightCount);
        totalAddPoints = view.findViewById(R.id.etAdditionalPointsCount);
        buttonSave = view.findViewById(R.id.buttonSaveEditedDayData);

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

        totalPoints.addTextChangedListener(inputWatcher);
        totalWeight.addTextChangedListener(inputWatcher);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalPointsCount = totalPoints.getText().toString();
                String totalWeightCount = totalWeight.getText().toString();
                String totalAdditionalPointsCount = totalAddPoints.getText().toString();

                int pointsCount = !totalPointsCount.isEmpty() ? Integer.parseInt(totalPointsCount) : 0;
                double totalWeight = !totalWeightCount.isEmpty() ? Double.parseDouble(totalWeightCount) : 0;
                int additionalPoints = !totalAdditionalPointsCount.isEmpty() ? Integer.parseInt(totalAdditionalPointsCount) : 0;

                MyApp.getDbExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<AppSettings> settingsList = MyApp.getDatabase().appSettingsDAO().getAll();

                        double salary = 0;
                        if (!settingsList.isEmpty()) {

                            AppSettings constant = settingsList.get(0);
                            salary = constant.departureFee +
                                    (constant.costPerPoint * (pointsCount + additionalPoints)) +
                                    (totalWeight * constant.pricePerTone);
                        }


                        DayData data = new DayData(pointsCount, totalWeight, additionalPoints, currentData, salary);
                        MyApp.getDatabase().dayDataDAO().insert(data);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireActivity(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            startActivity(intent);
                            dismiss();
                        });
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void checkInputFields() {
        String pointsStr = totalPoints.getText().toString().trim();
        String totalWeightStr = totalWeight.getText().toString().trim();


        // Если все поля непустые, кнопка активна, иначе — неактивна
        boolean allFilled = !pointsStr.isEmpty() && !totalWeightStr.isEmpty();
        buttonSave.setEnabled(allFilled);
    }
}
