package com.example.totoroto.mureok.List;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private Context context;
    private ArrayList<ListData> mListDatas;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_list, viewGroup, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        ListData listData = mListDatas.get(position);

        holder.tvDate.setText(listData.date);
        holder.tvContents.setText(listData.contents);


            try {
                holder.ivImage.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(Uri.parse(listData.imgPath))
                        .override(3500, 1500)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.ivImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if(pos != RecyclerView.NO_POSITION){
                    FirebaseDB firebaseDB = new FirebaseDB();
                    firebaseDB.deleteListData(mListDatas.get(pos).getFirebaseKey());
                    mListDatas.remove(pos);

                    notifyItemChanged(pos);
                    notifyItemRangeChanged(pos, mListDatas.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListDatas != null) ? mListDatas.size() : 0;
    }

    public void setListDatas(ArrayList<ListData> listDatas){
        mListDatas = listDatas;
    }
}
