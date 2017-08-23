package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ManageAdapter extends RecyclerView.Adapter<ManageViewHolder> {
    private final String TAG = "SOLBIN";
    private Map<String, ArrayList<String>> map;

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
        map = new LinkedHashMap<String, ArrayList<String>>();

        int layoutForManageItem = R.layout.item_manage;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutForManageItem, viewGroup, false);

        return new ManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        final ManageData itemData = mItems.get(position);

        holder.tv_pName.setText(" ("+itemData.pName+")");
        holder.tv_pRealName.setText(itemData.pRealName);
        holder.tv_pEnrollDate.setText(itemData.pEnrollDate);
        aboutAlarmText(holder, itemData);

        if (itemData.pIsAlarm) { //알람이 설정되어 있으면
            holder.btnAlarm.setBackgroundResource(R.drawable.ic_alarm_on);
            holder.btnAlarm.setText(R.string.alarmOn);

        } else {
            holder.btnAlarm.setBackgroundResource(R.drawable.ic_alarm_off);
            holder.btnAlarm.setText(R.string.alarmOff);
        }

        try {
            Glide.with(context)
                    .load(Uri.parse(itemData.pImg))
                    .override(2800, 1200)
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
                //물을 준 경우, 날짜를 달력에 표시하기 위해 데이터를 보낸다.
                Context context = v.getContext();

                aboutWaterDateResult(itemData);

                //TODO: 지금 adapter->set인데 set->adapter로 되야
                Log.d(TAG, "adapter Array:"+itemData.getFirebaseKey()+"|"+map.get(itemData.getFirebaseKey()));
                Bundle args = new Bundle();
                args.putStringArrayList("waterDateArray", map.get(itemData.getFirebaseKey()));

                CalendarDialog cDialog = new CalendarDialog();
                cDialog.setArguments(args); //send waterDateArray -> calendar

                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                cDialog.show(fm, "CalendarDialog");
            }
        });

    }

    private void aboutWaterDateResult(final ManageData itemData) {
        firebaseDBHelper.readWaterCalendarManageData(itemData.getFirebaseKey()); //달력에 물 줬던 날 표시
        firebaseDBHelper.setWaterDateResult(new FirebaseDBHelper.WaterDateResult() {
            @Override
            public void apply(ArrayList<String> waterDate) {

                if(map.containsKey(itemData.getFirebaseKey())) {
                    map.get(itemData.getFirebaseKey()).clear();
                }
                for (int i = 0; i < waterDate.size(); i++) {
                    if(!map.containsKey(itemData.getFirebaseKey())) {
                        map.put(itemData.getFirebaseKey(), new ArrayList<String>());
                    }

                    map.get(itemData.getFirebaseKey()).add(waterDate.get(i)); //맵에 어레이를 추가한다.
                    Log.d(TAG, "setResult waterArray:"+map.get(itemData.getFirebaseKey()));
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
            try {
                map.get(itemData.getFirebaseKey()).remove(dateFormat.format(currentDate));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } else { //OFF->ON
            firebaseDBHelper.isWaterCalendarManageData(itemData.getFirebaseKey(), dateFormat.format(currentDate), true);
            holder.btnWater.setText("물 ON");
        }
    }

    private void aboutAlarmText(ManageViewHolder holder, ManageData itemData) {
        if (!itemData.pAM_PM.equals("")) { //알람이 설정되어 있으면
            holder.tv_pWaterDate.setText("" + itemData.pHour + " : " + itemData.pMinute
                    + itemData.pAM_PM + " 에 물을 줍니다");

            // Log.d(TAG, "hour:"+itemData.pHour +"|min:" + itemData.pMinute);
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
                itemData.setpPerDate(perDate);
                itemData.setpHour(hour);
                itemData.setpMinute(minute);
                itemData.setpAM_PM(AM_PM);
                if (!AM_PM.equals("")) {
                    itemData.setpIsAlarm(true);

                } else {
                    itemData.setpIsAlarm(false);
                }
                notifyDataSetChanged();

                WaterAlarm waterAlarm = new WaterAlarm(context);
                waterAlarm.Alarm(itemData.getpPerDate(), itemData.getpRealName(), itemData.getpName() ,itemData.getpHour(), itemData.getpMinute());

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
