package com.example.totoroto.mureok.Community;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.R;

import java.util.ArrayList;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder> {
    private ArrayList<CommunityData> mDatas;

    public void setCommunityDatas(ArrayList<CommunityData> cDatas){
        mDatas = cDatas;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_community, parent, false);
        CommunityViewHolder cViewHolder = new CommunityViewHolder(view);

        return cViewHolder;
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        CommunityData communityData = mDatas.get(position);

        holder.civProfile.setBackgroundResource(communityData.imgProfile);
        holder.ivPicture.setBackgroundResource(communityData.imgPicture);
        holder.tvNickName.setText(communityData.nickName);
        holder.tvDate.setText(communityData.date);
        holder.tvContents.setText(communityData.contents);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
