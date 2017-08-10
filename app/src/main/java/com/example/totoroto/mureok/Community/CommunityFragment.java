package com.example.totoroto.mureok.Community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.R;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {
    private TabLayout tabLayoutCategory;
    private RecyclerView cRecyclerView;
    private CommunityAdapter cAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<CommunityData> cDatas;

    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);
        aboutTab();
        aboutSetRecycler();
        addTempItemFunc();

        return view;
    }

    private void aboutSetRecycler() {
        cRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        cRecyclerView.setLayoutManager(layoutManager);

        cAdapter = new CommunityAdapter();
        cAdapter.setCommunityDatas(cDatas);
        cRecyclerView.setAdapter(cAdapter);
    }

    private void addTempItemFunc() {
        CommunityData tempData = new CommunityData(R.mipmap.ic_launcher_round, "레어닉",
                "2010.10.10", R.mipmap.ic_launcher, "임시 데이터를 넣어봅니다");
        cDatas.add(tempData);
        cAdapter.notifyDataSetChanged();
    }

    private void init(View v) {
        cDatas = new ArrayList<>();

        tabLayoutCategory = (TabLayout)v.findViewById(R.id.tabLayoutCategory);
        cRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerCommunity);
    }

    private void aboutTab() {
        tabLayoutCategory.setSelectedTabIndicatorHeight(0);
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("전체"));
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("꽃"));
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("허브"));
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("다육이"));
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("채소"));
        tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("나무"));

        tabLayoutCategory.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}