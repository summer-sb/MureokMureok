package com.example.totoroto.mureok.community;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.totoroto.mureok.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView civUserProfile;
    public TextView tvUserName;
    public TextView tvComment;
    public TextView tvDate;

    public CommentViewHolder(View itemView) {
        super(itemView);

        init(itemView);
    }

    private void init(View itemView) {
        civUserProfile = (CircleImageView)itemView.findViewById(R.id.civUserProrile_comment);
        tvUserName = (TextView)itemView.findViewById(R.id.tvUserName_comment);
        tvComment = (TextView)itemView.findViewById(R.id.tvComment_comment);
        tvDate = (TextView)itemView.findViewById(R.id.tvDate_comment);
    }
}
