package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ManageData;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class ManageAdapter extends RecyclerView.Adapter<ManageViewHolder> {
    private final String TAG = "MADAPTER";
    private FirebaseDB firebaseDB;
    private ArrayList<ManageData> mItems;
    private Context context;

    public void setItemDatas(ArrayList<ManageData> itemDatas){
        mItems = itemDatas;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        firebaseDB = new FirebaseDB();

        int layoutForManageItem = R.layout.item_manage;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutForManageItem, viewGroup, false);

        return new ManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, int position) {
        ManageData itemData = mItems.get(position);

        holder.tv_pName.setText(itemData.pName);
        holder.tv_pRealName.setText(itemData.pRealName);
        holder.tv_pEnrollDate.setText(itemData.pEnrollDate);
        holder.tv_pWaterCnt.setText(String.valueOf(itemData.pWaterCnt));

        if(itemData.pIsAlarm) { //알람이 설정되어 있으면
            holder.btnSetWater.setBackgroundResource(R.color.colorPrimary);
            holder.btnSetWater.setText(R.string.alarmOn);
        }else{
            holder.btnSetWater.setBackgroundColor(Color.GRAY);
            holder.btnSetWater.setText(R.string.alarmOff);
        }

        try {
            Glide.with(context)
                    .load(Uri.parse(itemData.pImg))
                    .override(1000,800)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv_pImg);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!itemData.pAM_PM.equals("")) {
            holder.tv_pWaterDate.setText("" + itemData.pPerDate + "일 후 " + itemData.pHour + " : " + itemData.pMinute
                    + itemData.pAM_PM + " 에 물을 줍니다.");

            WaterAlarm waterAlarm = new WaterAlarm(context);
            waterAlarm.Alarm(itemData.pHour, itemData.pMinute);
        }else{
            holder.tv_pWaterDate.setText("");
        }

        holder.btnSetWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutWaterDialog(holder);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if(pos != RecyclerView.NO_POSITION) {
                    Log.d(TAG,"pos"+pos+"|" + mItems.get(pos).getpRealName());

                    firebaseDB.deleteManageData(mItems.get(pos).getFirebaseKey());
                    mItems.remove(pos);

                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, mItems.size());

               }
            }
        });

    }

    private void aboutWaterDialog(final ManageViewHolder holder) {
        final ManageData itemData = mItems.get(holder.getAdapterPosition());
        WaterAlarmDialog waterDialog = new WaterAlarmDialog();

        waterDialog.setDialogResult(new WaterAlarmDialog.DialogResult() {
            @Override
            public void apply(int perDate, int hour, int minute, String AM_PM) {

                if(!AM_PM.equals("")) {
                    itemData.setpIsAlarm(true);
                }else{
                    itemData.setpIsAlarm(false);
                }
                itemData.setpPerDate(perDate);
                itemData.setpHour(hour);
                itemData.setpMinute(minute);
                itemData.setpAM_PM(AM_PM);
                notifyDataSetChanged();

                firebaseDB.updateManageAlarmData(itemData.getFirebaseKey(), itemData);
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
