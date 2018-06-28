package com.example.totoroto.mureok.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.data.FirebaseDBHelper;
import com.example.totoroto.mureok.data.ListData;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class ListFragment extends Fragment  {
    private final int REQ_GALLERY_PICTURE  = 100;
    private final String TAG = "SOLBIN";
    public static String imgPath;

    private ListAdapter listAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<ListData> mListDatas;
    private RecyclerView recyclerView;
    private ImageView imgView;

    private FirebaseDBHelper firebaseDBHelper;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        init(view);
        aboutRecycler();
      //  aboutInput();

        firebaseDBHelper.readListData(mListDatas, listAdapter);

        return view;
    }

    private void aboutInput() {

        if(listAdapter.getItemCount() == 0) {
            Log.d(TAG, "itemcnt1:"+listAdapter.getItemCount());
            ListData tmpListData = new ListData();

            mListDatas.add(tmpListData);
            firebaseDBHelper.writeNewListData(tmpListData);
            listAdapter.notifyDataSetChanged();

            Log.d(TAG, "itemcnt2:"+listAdapter.getItemCount());
        }
    }


    private void aboutRecycler() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        listAdapter.setListDatas(mListDatas);
        recyclerView.setAdapter(listAdapter);
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.listRecycler);

        listAdapter = new ListAdapter();
        mListDatas = new ArrayList<>();
        firebaseDBHelper = new FirebaseDBHelper();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_GALLERY_PICTURE  && resultCode == Activity.RESULT_OK) {
            try {
                imgView = (ImageView)recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.iv_listInput);
                imgPath = data.getData().toString(); //이미지의 uri

                Glide.with(getContext())
                        .load(imgPath)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
