package com.example.totoroto.mureok.Community;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder> {
    private ArrayList<CommunityData> mDatas;
    private Context context;

    public void setCommunityDatas(ArrayList<CommunityData> cDatas){
        mDatas = cDatas;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_community, parent, false);

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        CommunityData communityData = mDatas.get(position);
        try {
        Glide.with(context)
                .load(Uri.parse(communityData.getImgProfile()))
                .override(150, 150)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.civProfile);

            Glide.with(context)
                    .load(Uri.parse(communityData.getImgPicture()))
                    .override(3500, 1500)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.ivPicture);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        holder.tvNickName.setText(communityData.nickName);
        holder.tvDate.setText(communityData.date);
        holder.tvContents.setText(communityData.contents);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
