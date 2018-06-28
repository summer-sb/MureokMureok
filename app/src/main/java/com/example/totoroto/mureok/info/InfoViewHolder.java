package com.example.totoroto.mureok.info;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.totoroto.mureok.R;


public class InfoViewHolder extends RecyclerView.ViewHolder {
    public TextView tvInfoList;

    public InfoViewHolder(View itemView) {
        super(itemView);

        tvInfoList = (TextView)itemView.findViewById(R.id.tvInfoItem);
    }
}
