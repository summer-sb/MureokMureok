package com.example.totoroto.mureok.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.totoroto.mureok.R;


public class ListFilterDialog extends DialogFragment implements View.OnClickListener{
    private DatePicker datePickerFilter;
    private Button btnOK;
    private Button btnCancel;
    private FilterDialogResult mFilterResult;

    public interface FilterDialogResult{
        void apply(int year, int month, int day);
    }

    public void setFilterResult(FilterDialogResult filterResult){
        mFilterResult = filterResult;
    }

    public ListFilterDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list_filter, container);

        init(view);

        return view;
    }

    private void aboutDatePicker() {
        int year = datePickerFilter.getYear();
        int month = datePickerFilter.getMonth() + 1;
        int day = datePickerFilter.getDayOfMonth();

        mFilterResult.apply(year, month, day);
        dismiss();
    }

    private void init(View view) {
        datePickerFilter = (DatePicker)view.findViewById(R.id.datePicker_list_filter);
        btnCancel = (Button)view.findViewById(R.id.btnCancel_list_filter);
        btnOK = (Button)view.findViewById(R.id.btnOK_list_filter);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel_list_filter:
                mFilterResult.apply(-1, -1, -1);
                dismiss();
                break;
            case R.id.btnOK_list_filter:
                aboutDatePicker();
                break;
        }
    }
}
