package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.totoroto.mureok.PlantTip.TipActivity;
import com.example.totoroto.mureok.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class ManageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView iv_pImg;
    public TextView tv_pName;
    public TextView tv_pRealName;
    public TextView tv_pEnrollDate;
    public TextView tv_pWaterDate;
    public TextView tv_pWaterCnt;

    private Button btnCalendar;
    private Button btnMinus;
    private Button btnPlus;
    public Button btnSetWater;
    private Button btnTip;
    public Button btnDelete;

    private int waterCount;

    public ManageViewHolder(final View itemView) {
        super(itemView);

        init();
    }

    private void init() {
        iv_pImg = (ImageView) itemView.findViewById(R.id.iv_pImg_m);
        tv_pName = (TextView) itemView.findViewById(R.id.tv_pName_m);
        tv_pRealName = (TextView) itemView.findViewById(R.id.tv_pRealName_m);
        tv_pEnrollDate = (TextView) itemView.findViewById(R.id.tvEnrollDate_m);
        tv_pWaterDate = (TextView) itemView.findViewById(R.id.tvWaterDate_m);
        tv_pWaterCnt = (TextView) itemView.findViewById(R.id.tvWaterCnt_m);

        btnCalendar = (Button) itemView.findViewById(R.id.btnCalendar_m);
        btnMinus = (Button) itemView.findViewById(R.id.btnMinus_m);
        btnPlus = (Button) itemView.findViewById(R.id.btnPlus_m);
        btnSetWater = (Button) itemView.findViewById(R.id.btnSetWater_m);
        btnTip = (Button) itemView.findViewById(R.id.btnTip_m);
        btnDelete = (Button) itemView.findViewById(R.id.btnDelete_m);

        iv_pImg.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnTip.setOnClickListener(this);

        waterCount = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalendar_m:
                aboutCalendar(v);
                break;
            case R.id.btnPlus_m:
                aboutBtnPlus();
                break;
            case R.id.btnMinus_m:
                aboutBtnMinus();
                break;
            case R.id.btnTip_m:
                moveTipActivity(v);
                break;
            case R.id.iv_pImg_m:
                break;
            default:
                //
        }
    }

    private void moveTipActivity(View v) {
        Intent intent = new Intent(v.getContext(), TipActivity.class);
        v.getContext().startActivity(intent);
    }

    private void aboutBtnMinus() {
        if (waterCount > 0) {
            tv_pWaterCnt.setText(String.valueOf(--waterCount));
        } else if (waterCount == 0) {
            tv_pWaterCnt.setText(String.valueOf(waterCount));
        }
    }

    private void aboutBtnPlus() {
        tv_pWaterCnt.setText(String.valueOf(++waterCount));
    }

    private void aboutCalendar(View v) {
        Context context = v.getContext();

        Bundle args =  new Bundle(1);
        args.putInt("waterCount", waterCount);

        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        CalendarDialog cDialog = new CalendarDialog();
        cDialog.setArguments(args); //send waterCount -> calendar
        cDialog.show(fm, "CalendarDialog");
    }
}