package com.example.totoroto.mureok.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.totoroto.mureok.R;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView tvDate;
    public TextView tvContents;
    public ImageView ivImage;

    private Button btnShare;
    private Button btnModify;
    public Button btnDelete;

    public ListViewHolder(View itemView) {
        super(itemView);

        init();
    }

    private void init() {
        tvDate = (TextView)itemView.findViewById(R.id.listItem_tvDate);
        tvContents = (TextView)itemView.findViewById(R.id.listItem_tvContent);
        ivImage = (ImageView)itemView.findViewById(R.id.listItem_iv);
        btnShare = (Button)itemView.findViewById(R.id.listItem_btnShare);
        btnModify = (Button)itemView.findViewById(R.id.listItem_btnModify);
        btnDelete = (Button)itemView.findViewById(R.id.listItem_btnDelete);

        btnShare.setOnClickListener(this);
        btnModify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.listItem_btnShare:
                //TODO: ListData- isShare true
                break;
            case R.id.listItem_btnModify:
                break;
            default:
                //
        }
    }
}
