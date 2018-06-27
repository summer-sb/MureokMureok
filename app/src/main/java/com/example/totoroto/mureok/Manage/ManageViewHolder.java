package com.example.totoroto.mureok.Manage;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.totoroto.mureok.R;

public class ManageViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    public ImageView iv_pImg;
    public TextView tv_pName;
    public TextView tv_pRealName;
    public TextView tv_pEnrollDate;
    public TextView tv_pWaterDate;

    public Button btnWater;
    public Button btnCalendar;
    public Button btnAlarm;
    public Button btnDelete;

    public ManageViewHolder(View itemView) {
        super(itemView);

        init(itemView);
    }

    private void init(View itemView) {
        iv_pImg = (ImageView) itemView.findViewById(R.id.iv_pImg_m);
        tv_pName = (TextView) itemView.findViewById(R.id.tv_pName_m);
        tv_pRealName = (TextView) itemView.findViewById(R.id.tv_pRealName_m);
        tv_pEnrollDate = (TextView) itemView.findViewById(R.id.tvEnrollDate_m);
        tv_pWaterDate = (TextView) itemView.findViewById(R.id.tvWaterDate_m);

        btnWater = (Button)itemView.findViewById(R.id.btnWater_m);
        btnCalendar = (Button) itemView.findViewById(R.id.btnCalendar_m);
        btnAlarm = (Button) itemView.findViewById(R.id.btnWaterAlarm_m);
       btnDelete = (Button) itemView.findViewById(R.id.btnDelete_m);

        iv_pImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pImg_m:
                break;
            default:
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("SOLBIN", "longclick pos:"+ position);
        return false;
    }
}