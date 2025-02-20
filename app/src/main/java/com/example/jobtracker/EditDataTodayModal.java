package com.example.jobtracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditDataTodayModal extends DialogFragment {
    public static EditDataTodayModal newInstance() {
        return new EditDataTodayModal();
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

        View view = inflater.inflate(R.layout.activity_edit_today_data_modal, container, false);

        ImageButton btnClose = view.findViewById(R.id.x_close1);
        btnClose.setOnClickListener(v -> dismiss());

        Button buttonEditData = view.findViewById(R.id.buttonEditDataToday);
        buttonEditData.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NewDataActivity.class);
            Config.edit = true;
            startActivity(intent);
        });

        Button buttonAddNewWay = view.findViewById(R.id.buttonAddNewWay);
        buttonAddNewWay.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AdditionalWayActivity.class);
            startActivity(intent);
        });

        Button buttonClose = view.findViewById(R.id.button_cancelEditing);
        buttonClose.setOnClickListener(v -> dismiss());

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
