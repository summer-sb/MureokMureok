package com.example.totoroto.mureok.Community;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder>{
    private CommentAdapter commentAdapter;
    private ArrayList<CommentData> commentDatas;

    private ArrayList<CommunityData> mDatas;
    private Context context;
    private FirebaseDB firebaseDB;

    public void setCommunityDatas(ArrayList<CommunityData> cDatas){
        mDatas = cDatas;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_community, parent, false);
        commentDatas = new ArrayList<>();
        commentAdapter = new CommentAdapter();

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, int position) {
        firebaseDB = new FirebaseDB();
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

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aboutCommentFunc(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void aboutCommentFunc(final CommunityViewHolder holder, final int position) { //아이템 포지션에 따라
        final TabLayout  mainTab = (TabLayout)((AppCompatActivity)context).findViewById(R.id.tabLayout);
        final TabLayout communityTab = (TabLayout)((AppCompatActivity) context).findViewById(R.id.tabLayoutCategory);
        final ViewPager viewPager = (ViewPager)((AppCompatActivity) context).findViewById(R.id.viewPager);

        holder.exBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.exLayoutComment.isExpanded()) {
                    setCommentRecycler(holder);
                    firebaseDB.readCommentData(mDatas.get(position).getFirebaseKey(), commentDatas, commentAdapter); //댓글을 읽어온다.

                    ((AppCompatActivity)context).getSupportActionBar().hide();
                    mainTab.setVisibility(View.GONE);
                    communityTab.setVisibility(View.GONE);
                    holder.exLayoutComment.expand(); //댓글 창을 펼친다.
                 }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //뷰페이저 이동하면 화면X
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        holder.exLayoutComment.collapse();
                        ((AppCompatActivity)context).getSupportActionBar().show();
                        mainTab.setVisibility(View.VISIBLE);
                        communityTab.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onPageSelected(int position) {
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
        });

        holder.btnSend_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                long ctm = System.currentTimeMillis();
                Date currentDate = new Date(ctm);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");
                //setCommentRecycler(holder);

                CommentData cData = new CommentData(fUser.getPhotoUrl().toString() ,fUser.getDisplayName(),
                        holder.etComment.getText().toString() ,dateFormat.format(currentDate));
                //param : imagePath, userName, comment, current date
                commentDatas.add(cData);

                firebaseDB.writeNewCommentData(mDatas.get(position).getFirebaseKey(), cData);
            }
        });

        holder.btnClose_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.exLayoutComment.isExpanded()){
                    holder.exLayoutComment.collapse();
                    ((AppCompatActivity)context).getSupportActionBar().show();
                    mainTab.setVisibility(View.VISIBLE);
                    communityTab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setCommentRecycler(CommunityViewHolder holder) {
        holder.commentRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.commentRecyclerView.setLayoutManager(layoutManager);
        commentAdapter.setCommunityDatas(commentDatas);
        holder.commentRecyclerView.setAdapter(commentAdapter);
    }

}
