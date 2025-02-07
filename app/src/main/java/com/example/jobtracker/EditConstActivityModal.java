package com.example.jobtracker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.jobtracker.database.AppDatabase;
import com.example.jobtracker.database.AppSettings;
import com.example.jobtracker.database.MyApp;

public class EditConstActivityModal extends DialogFragment {

    public static EditConstActivityModal newInstance() {
        return new EditConstActivityModal();
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

        View view = inflater.inflate(R.layout.activity_editconst_modal, container, false);

        ImageButton btnClose = view.findViewById(R.id.x_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        EditText costPerPointConst = view.findViewById(R.id.costPerPointConst);
        EditText departureFeeConst = view.findViewById(R.id.departureFeeConst);
        EditText pricePerTon = view.findViewById(R.id.pricePerTon);
        Button buttonSaveNewConst = view.findViewById(R.id.buttonSaveConst);

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
                dismiss();
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
}