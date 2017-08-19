package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Alarm.WaterAlarm;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.Data.ManageData;
import com.example.totoroto.mureok.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageAdapter extends RecyclerView.Adapter<ManageViewHolder> {
    private final String TAG = "SOLBIN";
    private int waterCount;
    private ArrayList<String> waterDateArray;

    private FirebaseDBHelper firebaseDBHelper;
    private ArrayList<ManageData> mItems;
    private Context context;

    public void setItemDatas(ArrayList<ManageData> itemDatas) {
        mItems = itemDatas;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        firebaseDBHelper = new FirebaseDBHelper();
        waterDateArray = new ArrayList<>();

        int layoutForManageItem = R.layout.item_manage;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutForManageItem, viewGroup, false);

        return new ManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        final ManageData itemData = mItems.get(position);

        holder.tv_pName.setText(itemData.pName);
        holder.tv_pRealName.setText(itemData.pRealName);
        holder.tv_pEnrollDate.setText(itemData.pEnrollDate);
        aboutWaterText(holder, itemData);
        readWaterCalendar(itemData);

        if (itemData.pIsAlarm) { //알람이 설정되어 있으면
            holder.btnAlarm.setBackgroundResource(R.color.colorPrimary);
            holder.btnAlarm.setText(R.string.alarmOn);
        } else {
            holder.btnAlarm.setBackgroundResource(android.R.drawable.btn_default);
            holder.btnAlarm.setText(R.string.alarmOff);
        }

        try {
            Glide.with(context)
                    .load(Uri.parse(itemData.pImg))
                    .override(1000, 800)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv_pImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutWaterDialog(holder);

                WaterAlarm waterAlarm = new WaterAlarm(context);
                if (!itemData.getpAM_PM().equals("")) {
                    waterAlarm.Alarm(itemData.pPerDate, itemData.pHour, itemData.pMinute);
                } else {
                    waterAlarm.Alarm(itemData.pPerDate, itemData.pHour, itemData.pMinute); //itemData.pPerDate = -1(Cancel)
                }
            }
        });

        holder.btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutWaterButton(holder, itemData);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    firebaseDBHelper.deleteManageData(mItems.get(pos).getFirebaseKey());
                    mItems.remove(pos);

                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mItems.size());
                }
            }
        });


        holder.btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                CalendarDialog cDialog = new CalendarDialog();

                //오늘 물을 준 경우, 오늘 날짜를 달력에 표시하기 위해 데이터를 보낸다
                Bundle args = new Bundle(1);
                cDialog.setArguments(args); //send waterCount -> calendar
                args.putStringArrayList("waterDateArray", waterDateArray);

                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                cDialog.show(fm, "CalendarDialog");
            }
        });

    }

    private void readWaterCalendar(ManageData itemData) {
        firebaseDBHelper.readWaterCalendarManageData(itemData.getFirebaseKey()); //달력에 물 줬던 날 표시
        firebaseDBHelper.setIswWaterDateResult(new FirebaseDBHelper.IsWaterDateResult() {
            @Override
            public void apply(ArrayList<String> waterDate) {
                waterDateArray.clear();
                for (int i = 0; i < waterDate.size(); i++) {
                    waterDateArray.add(waterDate.get(i));
                }
            }
        });
    }

    private void aboutWaterButton(ManageViewHolder holder, ManageData itemData) {
        long ctm = System.currentTimeMillis();
        final Date currentDate = new Date(ctm);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        if (holder.btnWater.getText().toString().equals("물 ON")) { //ON->OFF
            firebaseDBHelper.isWaterCalendarManageData(itemData.getFirebaseKey(), dateFormat.format(currentDate), false);
            holder.btnWater.setText("물 OFF");
        } else { //OFF->ON
            firebaseDBHelper.isWaterCalendarManageData(itemData.getFirebaseKey(), dateFormat.format(currentDate), true);
            holder.btnWater.setText("물 ON");
        }
    }

    private void aboutWaterText(ManageViewHolder holder, ManageData itemData) {
        if (!itemData.pAM_PM.equals("")) { //알람이 설정되어 있으면
            holder.tv_pWaterDate.setText("" + itemData.pPerDate + "일 마다 " + itemData.pHour + " : " + itemData.pMinute
                    + itemData.pAM_PM + " 에 물을 줍니다.");
        } else {
            holder.tv_pWaterDate.setText("");
        }
    }

    private void aboutWaterDialog(final ManageViewHolder holder) {
        final ManageData itemData = mItems.get(holder.getAdapterPosition());
        WaterAlarmDialog waterDialog = new WaterAlarmDialog();

        waterDialog.setDialogResult(new WaterAlarmDialog.DialogResult() {
            @Override
            public void apply(int perDate, int hour, int minute, String AM_PM) {
                if (!AM_PM.equals("")) {
                    itemData.setpIsAlarm(true);
                } else {
                    itemData.setpIsAlarm(false);
                }
                itemData.setpPerDate(perDate);
                itemData.setpHour(hour);
                itemData.setpMinute(minute);
                itemData.setpAM_PM(AM_PM);
                notifyDataSetChanged();

                firebaseDBHelper.updateManageAlarmData(itemData.getFirebaseKey(), itemData);
            }
        });

        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        waterDialog.show(fm, "waterDialog");
    }

    @Override
    public int getItemCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

}
