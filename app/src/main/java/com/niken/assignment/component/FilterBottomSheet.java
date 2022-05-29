package com.niken.assignment.component;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.niken.assignment.MainActivity;
import com.niken.assignment.R;
import com.niken.assignment.model.ClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterBottomSheet extends BottomSheetDialogFragment {
    private LinearLayout fromLayout;
    private TextInputEditText fromEditForm;
    private LinearLayout toLayout;
    private TextInputEditText toEditForm;
    private Button btnFilter;
    private TextView btnReset;
    private ClickListener clickListener;
    private Long fromDate;
    private Long toDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filter_bottom_sheet_layout,
                container, false);

        fromLayout = view.findViewById(R.id.from_layout);
        fromEditForm = view.findViewById(R.id.from_edit_form);
        toLayout = view.findViewById(R.id.to_layout);
        toEditForm = view.findViewById(R.id.to_edit_form);
        btnFilter = view.findViewById(R.id.btn_filter);
        btnReset = view.findViewById(R.id.reset_view);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onReset();
                getDialog().dismiss();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromEditForm.getText().toString().isEmpty() || toEditForm.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Select Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                clickListener.onClicked(fromDate.toString(), toDate.toString());
                getDialog().dismiss();
            }
        });

        fromEditForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarView("from");
            }
        });

        toEditForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarView("to");
            }
        });

        return view;
    }

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void showCalendarView(String type) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date(selection);

                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                String dateText = df2.format(date);
                if (type.equals("from")) {
                    fromEditForm.setText(dateText);
                    fromDate = selection;
                } else {
                    toEditForm.setText(dateText);

                    toDate = selection;
                }
            }
        });
        datePicker.show(((MainActivity) getContext()).getSupportFragmentManager(), "TA");
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                if (type.equals("from")) {
//                    fromEditForm.setText(year+ "-" + month + "-" + dayOfMonth);
//                } else {
//                    toEditForm.setText(year+ "-" + month + "-" + dayOfMonth);
//                }
//            }
//        }, year, month, day).show();
    }


}
