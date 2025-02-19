package com.example.jobtracker;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.MyApp;

public class EditConstActivityModal extends DialogFragment {

    public static EditConstActivityModal newInstance() {
        return new EditConstActivityModal();
    }

    private EditText costPerPointConst;
    private EditText departureFeeConst;
    private EditText pricePerTon;
    private Button buttonSaveNewConst;

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

        View view = inflater.inflate(R.layout.activity_editconst_modal, container, false);

        ImageButton btnClose = view.findViewById(R.id.x_close);
        btnClose.setOnClickListener(v -> dismiss());

        costPerPointConst = view.findViewById(R.id.costPerPointConst);
        departureFeeConst = view.findViewById(R.id.departureFeeConst);
        pricePerTon = view.findViewById(R.id.pricePerTon);
        buttonSaveNewConst = view.findViewById(R.id.buttonSaveConst);


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


        buttonSaveNewConst.setOnClickListener(v -> {

            try {
                int costPerPoint = Integer.parseInt(costPerPointConst.getText().toString().trim());
                int departureFee = Integer.parseInt(departureFeeConst.getText().toString().trim());
                double pricePerTona = Double.parseDouble(pricePerTon.getText().toString().trim());

                AppSettings appSettings = new AppSettings(1, costPerPoint, departureFee, pricePerTona);
                MyApp.getDbExecutor().execute(() -> {
                    AppDatabase database = MyApp.getDatabase();
                    database.appSettingsDAO().insert(appSettings);
                });
                Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Проверьте ввод числовых данных", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void checkInputFields() {
        String costStr = costPerPointConst.getText().toString().trim();
        String departureStr = departureFeeConst.getText().toString().trim();
        String priceStr = pricePerTon.getText().toString().trim();

        boolean allFilled = !costStr.isEmpty() && !departureStr.isEmpty() && !priceStr.isEmpty();
        buttonSaveNewConst.setEnabled(allFilled);
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
}