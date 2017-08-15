package com.example.totoroto.mureok.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListFragment extends Fragment  {
    private final int REQ_GALLERY_PICTURE  = 100;
    private final String TAG = "SOLBIN";
    public static String imgPath;

    private ListAdapter listAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<ListData> mListDatas;
    private RecyclerView recyclerView;
    private ImageView imgView;

    private FirebaseDB firebaseDB;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        init(view);
        aboutRecycler();

        firebaseDB.readListData(mListDatas, listAdapter);

        return view;
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
        firebaseDB = new FirebaseDB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "listfragment onactivityResult");

        if (requestCode == REQ_GALLERY_PICTURE  && resultCode == Activity.RESULT_OK) {
            try {
                imgView = (ImageView)recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.iv_listInput);
                imgPath = data.getData().toString(); //이미지의 uri

                Glide.with(getContext())
                        .load(imgPath)
                        .override(1000, 1000)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
