package com.example.totoroto.mureok.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.R;

import java.util.ArrayList;


public class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder> {
    private ArrayList<String> mInfoDatas;

    public void setInfoDatas(ArrayList<String> infoDatas){
        mInfoDatas = infoDatas;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_info, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        String infoStr = mInfoDatas.get(position);
        holder.tvInfoList.setText(infoStr);
    }

    @Override
    public int getItemCount() {
        return mInfoDatas.size();
    }
}
