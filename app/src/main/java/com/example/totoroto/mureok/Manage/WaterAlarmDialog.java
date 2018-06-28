package com.example.totoroto.mureok.Manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.totoroto.mureok.R;


public class WaterAlarmDialog extends DialogFragment implements View.OnClickListener{
    private DialogResult mDialogResult;
    private TimePicker tpAlarm;
   // private EditText etWaterDate;
    private Button btnCancel;
    private Button btnOK;

    public interface DialogResult{
        void apply(int perDate, int hour, int minute, String AM_PM);
    }

    public void setDialogResult(DialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public WaterAlarmDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_water, container);
        init(view);

        return view;
    }

    private void aboutAlarmDate() {
        //며칠 마다
        int mPerDate = 0;

        //시간
        int mHour = tpAlarm.getHour();
        int mMinute = tpAlarm.getMinute();
        String AM_PM;

        if(mHour <12){
            AM_PM = "AM";
        }else{
            AM_PM = "PM";
        }

        mDialogResult.apply(mPerDate, mHour, mMinute, AM_PM);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel_water:
                aboutBtnCancel();
                break;
            case R.id.btnOK_water:
                aboutAlarmDate();
                break;
        }
    }

    private void aboutBtnCancel() {
        mDialogResult.apply(-1, 0, 0, "");
        dismiss();
    }

    private void init(View v) {
        //etWaterDate = (EditText) v.findViewById(R.id.etWaterDate);
        tpAlarm = (TimePicker) v.findViewById(R.id.timePicker);
        btnCancel = (Button)v.findViewById(R.id.btnCancel_water);
        btnOK = (Button)v.findViewById(R.id.btnOK_water);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }
}
