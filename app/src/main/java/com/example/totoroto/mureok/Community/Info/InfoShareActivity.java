package com.example.totoroto.mureok.Community.Info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.totoroto.mureok.Community.CommunityAdapter;
import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class InfoShareActivity extends AppCompatActivity{
    /*
    private RecyclerView recyclerInfoShare;
    private LinearLayoutManager layoutManager;
    private CommunityAdapter cAdapter;
    private FirebaseDBHelper dbHelper;

    private ArrayList<CommunityData> cDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_share);

        init();
        setRecycler();
        aboutAddItems();
    }

    private void aboutAddItems() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        //dbHelper.readMyShareCommunityData(cDatas, cAdapter, fUser.getUid());
        //dbHelper.readCommunityData(cDatas, cAdapter, 0);
    }

    private void setRecycler() {
        recyclerInfoShare.setHasFixedSize(true);
        recyclerInfoShare.setLayoutManager(layoutManager);

        cAdapter = new CommunityAdapter();
        cAdapter.setCommunityDatas(cDatas);
        recyclerInfoShare.setAdapter(cAdapter);
    }

    private void init() {
        recyclerInfoShare = (RecyclerView)findViewById(R.id.recycler_info_share);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        dbHelper = new FirebaseDBHelper();

        cDatas = new ArrayList<>();
    }
    */
}
