package com.example.totoroto.mureok.community;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.R;

public class PostListFragment extends Fragment {
    public static PostListFragment newInstance() {
        return new PostListFragment();
    }

    CommunityViewModel viewModel = new CommunityViewModel();

    private TabLayout tabLayoutCategory;
    private LinearLayoutManager layoutManager;
    private RecyclerView cRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);
        aboutTab();
        aboutSetCommunityRecycler();

        return view;
    }


    private void aboutSetCommunityRecycler() {
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(layoutManager);
    }

    private void init(View v) {
        layoutManager = new LinearLayoutManager(getActivity());
        tabLayoutCategory = (TabLayout)v.findViewById(R.id.tabLayoutCategory);
        cRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerCommunity);

        viewModel = ViewModelProviders.of(this).get(CommunityViewModel.class);
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
                    //case 0: 전체
                    case 1:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 1);
                        break;
                    case 2:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 2);
                        break;
                    case 3:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 3);
                        break;
                    case 4:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 4);
                        break;
                    case 5:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 5);
                        break;
                    default:
                       // firebaseDBHelper.readCommunityData(mCommunityData, cAdapter, 0);
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
