package com.example.totoroto.mureok.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.R;

public class ListCardViewHolder extends RecyclerView.ViewHolder{
    public ImageView iv_listInput;
    public EditText et_listInput;
    public Button btnReset;
    public Button btnAdd;

    public ListCardViewHolder(View itemView) {
        super(itemView);

        init();
    }

    private void init() {
        iv_listInput = (ImageView)itemView.findViewById(R.id.iv_listInput);
        et_listInput = (EditText)itemView.findViewById(R.id.et_listInput);
        btnReset = (Button)itemView.findViewById(R.id.btnReset_listInput);
        btnAdd = (Button)itemView.findViewById(R.id.btnAdd_listInput);
    }
/*
    @Override
    public void applyPath(String path) {

    }
    */
}
