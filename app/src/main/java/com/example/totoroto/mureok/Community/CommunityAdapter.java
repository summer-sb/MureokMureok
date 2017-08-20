package com.example.totoroto.mureok.Community;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.CommentData;
import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.Data.FirebaseStorageHelper;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder>{
    private CommentAdapter commentAdapter;
    private Context context;
    private FirebaseDBHelper firebaseDBHelper;
    private FirebaseStorageHelper firebaseStorageHelper;

    private boolean isAdd;
    private Uri shareUri;
    private ArrayList<CommentData> commentDatas;
    private ArrayList<CommunityData> mDatas;

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
        isAdd = false;

          firebaseDBHelper = new FirebaseDBHelper();
          firebaseStorageHelper = new FirebaseStorageHelper();

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, final int position) {
        final CommunityData communityData = mDatas.get(position);

        firebaseStorageHelper.imageDownLoad(communityData.getFirebaseKey());
        firebaseStorageHelper.setStorageImageResult(new FirebaseStorageHelper.StorageImageResult() {
            @Override
            public void applyImage(Uri uri) {
                shareUri = uri;
                Glide.with(context)
                        .load(uri)
                        .override(3500, 1500)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.ivPicture);
            }
        });

        firebaseStorageHelper.profileDownLoad(communityData.getFirebaseKey());
        firebaseStorageHelper.setStorageProfileResult(new FirebaseStorageHelper.StorageProfileResult() {
            @Override
            public void applyProfile(Uri uri) {
               Glide.with(context)
                        .load(uri)
                        .override(150, 150)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.civProfile);
            }
        });

        holder.tvNickName.setText(communityData.nickName);
        holder.tvDate.setText(communityData.date);
        holder.tvContents.setText(communityData.contents);
        holder.tvNumLike.setText(String.valueOf(communityData.numLike));

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNumLike = communityData.getNumLike();
                if(!isAdd) { //좋아요를 누르지 않은 상태이면(좋아요x->좋아요o)
                    isAdd = firebaseDBHelper.updateNumLikeData(uid, communityData.getFirebaseKey(), currentNumLike + 1, true);
                    holder.btnLike.setBackgroundResource(R.color.colorPrimary);
                }else{
                    isAdd = firebaseDBHelper.updateNumLikeData(uid, communityData.getFirebaseKey(), currentNumLike -1, false);
                    holder.btnLike.setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareKakao(position);
            }
        });

        firebaseDBHelper.isLikeCommunityData(mDatas.get(position).getFirebaseKey(), uid);
        firebaseDBHelper.setIsLikeResult(new FirebaseDBHelper.IsLikeResult() {
            @Override
            public void success() {
                holder.btnLike.setBackgroundResource(R.color.colorPrimary);
                isAdd = true;
            }
        });

        aboutCommentFunc(holder, position);
    }

    private void shareKakao(int position) {
        try{
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addImage(shareUri.toString(), 81, 81); //가로 세로는 80보다 커야한다.
            kakaoBuilder.addText(mDatas.get(position).getContents());
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder, context);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void aboutCommentFunc(final CommunityViewHolder holder, final int position) { //아이템 포지션에 따라
        final ViewPager viewPager = (ViewPager)((AppCompatActivity) context).findViewById(R.id.viewPager);

        holder.exBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.exLayoutComment.isExpanded()) {
                    setCommentRecycler(holder);
                    firebaseDBHelper.readCommentData(mDatas.get(position).getFirebaseKey(), commentDatas, commentAdapter); //댓글을 읽어온다.

                    holder.btnLike.setVisibility(View.GONE);
                    holder.exBtnComment.setVisibility(View.GONE);
                    holder.btnShare.setVisibility(View.GONE);
                    holder.exLayoutComment.expand(); //댓글 창을 펼친다.
                 }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //뷰페이저 이동하면 화면X
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        holder.exLayoutComment.collapse();
                        holder.btnLike.setVisibility(View.VISIBLE);
                        holder.exBtnComment.setVisibility(View.VISIBLE);
                        holder.btnShare.setVisibility(View.VISIBLE);
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

                CommentData cData = new CommentData(fUser.getPhotoUrl().toString() ,fUser.getDisplayName(),
                        holder.etComment.getText().toString() ,dateFormat.format(currentDate));
                //param : imagePath, userName, comment, current date
                commentDatas.add(cData);

                firebaseDBHelper.writeNewCommentData(mDatas.get(position).getFirebaseKey(), cData);
            }
        });

        holder.btnClose_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.exLayoutComment.isExpanded()){
                    holder.exLayoutComment.collapse();
                    holder.btnLike.setVisibility(View.VISIBLE);
                    holder.exBtnComment.setVisibility(View.VISIBLE);
                    holder.btnShare.setVisibility(View.VISIBLE);
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
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
