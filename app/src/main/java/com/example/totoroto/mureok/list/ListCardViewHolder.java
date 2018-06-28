package com.example.totoroto.mureok.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.totoroto.mureok.R;

public class ListCardViewHolder extends RecyclerView.ViewHolder{
    public ImageView iv_listInput;
    public EditText et_listInput;
    public Button btnReset;
    public Button btnAdd;
    public Button btnFilter;

    public ListCardViewHolder(View itemView) {
        super(itemView);

        init();
    }

    private void init() {
        iv_listInput = (ImageView)itemView.findViewById(R.id.iv_listInput);
        et_listInput = (EditText)itemView.findViewById(R.id.et_listInput);
        btnReset = (Button)itemView.findViewById(R.id.btnReset_listInput);
        btnAdd = (Button)itemView.findViewById(R.id.btnAdd_listInput);
        btnFilter = (Button)itemView.findViewById(R.id.btnFilter_listInput);
    }
}
