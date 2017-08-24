package com.example.totoroto.mureok.Community;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.totoroto.mureok.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityViewHolder extends RecyclerView.ViewHolder{
    public CircleImageView civProfile;
    public ImageView ivPicture;
    public TextView tvNickName;
    public TextView tvDate;
    public TextView tvContents;
    public TextView tvNumLike;

    public Button btnLike;
    public Button exBtnComment;
    public Button btnShare;
    public ExpandableLayout exLayoutComment;

    //comment(expandable layout) item
    public Button btnClose_comment;
    public Button btnSend_comment;
    public EditText etComment;
    public RecyclerView commentRecyclerView;

    //bottom preview comment
    public ViewGroup viewGroupComment1;
    public CircleImageView civUserProfile_c1;
    public TextView tvUserName_c1;
    public TextView tvComment_c1;
    public TextView tvDate_c1;

    public ViewGroup viewGroupComment2;
    public CircleImageView civUserProfile_c2;
    public TextView tvUserName_c2;
    public TextView tvComment_c2;
    public TextView tvDate_c2;

    public CommunityViewHolder(View itemView) {
        super(itemView);

        init(itemView);
        aboutStorageImgApply();
    }

    private void aboutStorageImgApply() {

    }

    private void init(View v) {
        civProfile = (CircleImageView)v.findViewById(R.id.civUserProfile_community);
        ivPicture = (ImageView)v.findViewById(R.id.ivPicture_community);
        tvNickName = (TextView)v.findViewById(R.id.tvNickName_community);
        tvDate = (TextView)v.findViewById(R.id.tvDate_community);
        tvContents = (TextView)v.findViewById(R.id.tvContents_community);
        tvNumLike = (TextView)v.findViewById(R.id.tvNumLike_community);
        btnLike = (Button)v.findViewById(R.id.btnLike_community);
        btnShare = (Button)v.findViewById(R.id.btnShare_community);
        exBtnComment = (Button)v.findViewById(R.id.exBtnComment_community);

        exLayoutComment = (ExpandableLayout)v.findViewById(R.id.exLayoutComment);
        btnClose_comment = (Button)v.findViewById(R.id.btnClose_comment);
        btnSend_comment = (Button)v.findViewById(R.id.btnSend_comment);
        etComment = (EditText)v.findViewById(R.id.etComment);
        commentRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_comment);

        viewGroupComment1 = (ViewGroup) v.findViewById(R.id.previewComment);
        civUserProfile_c1 = (CircleImageView)viewGroupComment1.findViewById(R.id.civUserProrile_comment);
        tvUserName_c1 = (TextView)viewGroupComment1.findViewById(R.id.tvUserName_comment);
        tvComment_c1 = (TextView)viewGroupComment1.findViewById(R.id.tvComment_comment);
        tvDate_c1 = (TextView)viewGroupComment1.findViewById(R.id.tvDate_comment);

        viewGroupComment2 = (ViewGroup) v.findViewById(R.id.previewComment2);
        civUserProfile_c2 = (CircleImageView)viewGroupComment2.findViewById(R.id.civUserProrile_comment);
        tvUserName_c2 = (TextView)viewGroupComment2.findViewById(R.id.tvUserName_comment);
        tvComment_c2 = (TextView)viewGroupComment2.findViewById(R.id.tvComment_comment);
        tvDate_c2 = (TextView)viewGroupComment2.findViewById(R.id.tvDate_comment);
    }
}
