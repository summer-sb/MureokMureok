package com.example.totoroto.mureok.community;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.data.CommentData;
import com.example.totoroto.mureok.data.CommunityData;
import com.example.totoroto.mureok.data.FirebaseDBHelper;
import com.example.totoroto.mureok.data.FirebaseStorageHelper;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder> {
    private CommentAdapter commentAdapter;
    private Context context;
    private FirebaseDBHelper firebaseDBHelper;
    private FirebaseStorageHelper firebaseStorageHelper;

    private boolean[] isAdd;
    private Uri shareUri;
    private ArrayList<CommentData> commentDatas;
    private ArrayList<CommunityData> mDatas;

    public void setCommunityDatas(ArrayList<CommunityData> cDatas) {
        mDatas = cDatas;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_community, parent, false);
        commentDatas = new ArrayList<>();
        commentAdapter = new CommentAdapter();

        isAdd = new boolean[getItemCount()];
        for(int i=0; i<getItemCount(); i++){
            isAdd[i] = false;
        }

        shareUri = null;

        firebaseDBHelper = new FirebaseDBHelper();
        firebaseStorageHelper = new FirebaseStorageHelper();

        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, int position) {
        final CommunityData communityData = mDatas.get(position);

        Glide.with(context)
                .load(Uri.parse(communityData.getImgProfile()))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.civProfile);

        Glide.with(context)
                .load(Uri.parse(communityData.getImgPicture()))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivPicture);

        holder.tvNickName.setText(communityData.nickName);
        holder.tvDate.setText(communityData.date);
        holder.tvContents.setText(communityData.contents);
        holder.tvNumLike.setText(String.valueOf(communityData.numLike));


        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

/*
        if(isAdd[position]){
            Log.d("SOLBIN", "color on "+ position + isAdd[position]);
        }else{
            holder.btnLike.setBackgroundResource(android.R.drawable.btn_default);
            Log.d("SOLBIN", "color off" + position + isAdd[position]);
        }
*/
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentNumLike = communityData.getNumLike();

                if (!holder.btnLike.isSelected()) { //좋아요를 누르지 않은 상태이면(좋아요x->좋아요o)
                    holder.btnLike.setSelected(true);
                    firebaseDBHelper.updateNumLikeData(uid, communityData.getFirebaseKey(), currentNumLike + 1, true);

                } else {
                    holder.btnLike.setSelected(false);
                    firebaseDBHelper.updateNumLikeData(uid, communityData.getFirebaseKey(), currentNumLike - 1, false);
                }


            }
        });

        firebaseDBHelper.isLikeCommunityData(communityData.getFirebaseKey(), uid, holder);

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareKakao(holder.getAdapterPosition());
            }
        });

        aboutCommentFunc(holder, position);
        aboutCommentPreview(holder, position);
    }

    private void aboutCommentPreview(final CommunityViewHolder holder, int position) {
        ArrayList<CommentData> preViewDatas = new ArrayList<>();
        firebaseDBHelper.readCommentDataPreview(mDatas.get(position).getFirebaseKey(), preViewDatas);
        firebaseDBHelper.setCommentPreView(new FirebaseDBHelper.CommentPreView() {
            @Override
            public void apply(ArrayList<CommentData> preView) {
                try {
                    if (preView.get(preView.size()-1) != null) {
                        holder.viewGroupComment2.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(Uri.parse(preView.get(preView.size()-1).getProfileImgPath()))
                                .override(100, 100)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(holder.civUserProfile_c2);
                        holder.tvUserName_c2.setText(preView.get(preView.size()-1).getUserName());
                        holder.tvComment_c2.setText(preView.get(preView.size()-1).getComment());
                        holder.tvDate_c2.setText(preView.get(preView.size()-1).getDate());
                    }
                    if (preView.get(preView.size()-2) != null) {
                        holder.viewGroupComment1.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(Uri.parse(preView.get(preView.size()-2).getProfileImgPath()))
                                .override(100, 100)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(holder.civUserProfile_c1);
                        holder.tvUserName_c1.setText(preView.get(preView.size()-2).getUserName());
                        holder.tvComment_c1.setText(preView.get(preView.size()-2).getComment());
                        holder.tvDate_c1.setText(preView.get(preView.size()-2).getDate());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void shareKakao(int position) {
        try {
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
            KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addImage(mDatas.get(position).getImgPicture(), 81, 81); //가로 세로는 80보다 커야한다.
            kakaoBuilder.addText(mDatas.get(position).getContents());
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder, context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aboutCommentFunc(final CommunityViewHolder holder, final int position) { //아이템 포지션에 따라
        final ViewPager viewPager = (ViewPager) ((AppCompatActivity) context).findViewById(R.id.viewPager);

        holder.exBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.exLayoutComment.isExpanded()) {
                    setCommentRecycler(holder);
                    firebaseDBHelper.readCommentData(mDatas.get(position).getFirebaseKey(), commentDatas, commentAdapter); //댓글을 읽어온다.

                    holder.btnLike.setVisibility(View.GONE);
                    holder.exBtnComment.setVisibility(View.GONE);
                    holder.btnShare.setVisibility(View.GONE);
                    holder.viewGroupComment1.setVisibility(View.GONE);
                    holder.viewGroupComment2.setVisibility(View.GONE);

                    holder.exLayoutComment.expand(); //댓글 창을 펼친다.
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //뷰페이저 이동하면 화면X
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        holder.exLayoutComment.collapse();
                        holder.btnLike.setVisibility(View.VISIBLE);
                        holder.exBtnComment.setVisibility(View.VISIBLE);
                        holder.btnShare.setVisibility(View.VISIBLE);
                        if(!holder.tvComment_c1.getText().equals("")) {
                            holder.viewGroupComment1.setVisibility(View.VISIBLE);
                        }
                        if(!holder.tvComment_c2.getText().equals("")) {
                            holder.viewGroupComment2.setVisibility(View.VISIBLE);
                        }
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

                if(!holder.etComment.getText().toString().equals("")) {
                    CommentData cData = new CommentData(fUser.getPhotoUrl().toString(), fUser.getDisplayName(),
                            holder.etComment.getText().toString(), dateFormat.format(currentDate));
                    //param : imagePath, userName, comment, current date
                    commentDatas.add(cData);

                    firebaseDBHelper.writeNewCommentData(mDatas.get(position).getFirebaseKey(), cData);
                }
                holder.etComment.setText("");
            }
        });

        holder.btnClose_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.exLayoutComment.isExpanded()) {
                    holder.exLayoutComment.collapse();
                    holder.btnLike.setVisibility(View.VISIBLE);
                    holder.exBtnComment.setVisibility(View.VISIBLE);
                    holder.btnShare.setVisibility(View.VISIBLE);

                    if(!holder.tvComment_c1.getText().equals("")) {
                        holder.viewGroupComment1.setVisibility(View.VISIBLE);
                    }
                    if(!holder.tvComment_c2.getText().equals("")) {
                        holder.viewGroupComment2.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        holder.commentRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                firebaseDBHelper.deleteCommentData(mDatas.get(position).getFirebaseKey(), commentDatas.get(position).getFirebaseKey());
                commentDatas.remove(position);
                notifyDataSetChanged();
                return false;
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
