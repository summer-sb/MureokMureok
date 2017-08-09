package com.example.totoroto.mureok.PlantTip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.totoroto.mureok.R;

public class TipViewHolder extends RecyclerView.ViewHolder {
    TextView tvPlantItem;

    public TipViewHolder(View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        tvPlantItem = (TextView)itemView.findViewById(R.id.tvPlantItem);
    }
}
