package com.example.totoroto.mureok.PlantTip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.TipData;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class TipAdapter extends RecyclerView.Adapter<TipViewHolder> {
    private ArrayList<TipData> mTipDatas;

    public void setTipDatas(ArrayList<TipData> pDatas){
        mTipDatas = pDatas;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_tip, parent, false);

        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, int position) {
        TipData tipData = mTipDatas.get(position);

        holder.tvPlantItem.setText(tipData.pRealName);
    }

    @Override
    public int getItemCount() {
        return (mTipDatas != null) ? mTipDatas.size() : 0;
    }
}
