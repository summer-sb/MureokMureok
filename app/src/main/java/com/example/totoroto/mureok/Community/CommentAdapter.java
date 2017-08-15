package com.example.totoroto.mureok.Community;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{
    private ArrayList<CommentData> mDatas;
    private Context context;

    public void setCommunityDatas(ArrayList<CommentData> commentDatas){
        mDatas = commentDatas;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_comment, parent , false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentData cData = mDatas.get(position);
        try {
            Glide.with(context)
                    .load(Uri.parse(cData.getProfileImgPath()))
                    .override(150, 150)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.civUserProfile);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.tvUserName.setText(cData.getUserName());
        holder.tvComment.setText(cData.getComment());
        holder.tvDate.setText(cData.getDate());

        Log.d("SOLBIN","commentCnt:"+String.valueOf(getItemCount()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
