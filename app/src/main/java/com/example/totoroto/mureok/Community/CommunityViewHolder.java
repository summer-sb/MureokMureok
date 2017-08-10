package com.example.totoroto.mureok.Community;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.totoroto.mureok.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityViewHolder extends RecyclerView.ViewHolder{
    public CircleImageView civProfile;
    public ImageView ivPicture;
    public TextView tvNickName;
    public TextView tvDate;
    public TextView tvContents;

    public CommunityViewHolder(View itemView) {
        super(itemView);

        init(itemView);
    }

    private void init(View v) {
        civProfile = (CircleImageView)v.findViewById(R.id.civUserProfile_community);
        ivPicture = (ImageView)v.findViewById(R.id.ivPicture_community);
        tvNickName = (TextView)v.findViewById(R.id.tvNickName_community);
        tvDate = (TextView)v.findViewById(R.id.tvDate_community);
        tvContents = (TextView)v.findViewById(R.id.tvContents_community);

    }
}
