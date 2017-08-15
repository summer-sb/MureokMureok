package com.example.totoroto.mureok.Community;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommunityFragment extends Fragment{
    public static final String TAG = "SOLBIN";
    private FirebaseDB firebaseDB;
    private TabLayout tabLayoutCategory;
    //about community recycler view
    private LinearLayoutManager layoutManager;
    private CommunityAdapter cAdapter;
    private ArrayList<CommunityData> mCommunityDatas;
    private RecyclerView cRecyclerView;

    private final int SELECT_FLOWER = 1;
    private final int SELECT_HERB = 2;
    private final int SELECT_CACTUS = 3;
    private final int SELECT_VEGETABLE = 4;
    private final int SELECT_TREE = 5;


    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    public CommunityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);
        aboutTab();
        aboutSetCommunityRecycler();

        if(getArguments() != null) {
            aboutItemAdd();
        }
        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 0);

        return view;
    }


    private void aboutItemAdd() {
        int typeCateGory;
        ListData listData = getArguments().getParcelable("sharedListData");
        String imagePath = listData.getImgPath();
        String contents = listData.getContents();

        Log.d(TAG, "contents: "+contents);

        if(listData.isRadioFlower()){
            typeCateGory = SELECT_FLOWER;
        }else if(listData.isRadioHerb()){
            typeCateGory = SELECT_HERB;
        }else if(listData.isRadioCactus()){
            typeCateGory = SELECT_CACTUS;
        }else if(listData.isRadioVegetable()){
            typeCateGory = SELECT_VEGETABLE;
        }else if(listData.isRadioTree()){
            typeCateGory = SELECT_TREE;
        }else{
            typeCateGory = 0;
        }

        String profilePhoto = getArguments().getString("userProfilePhoto");
        String nickName = getArguments().getString("userNickName");

        //현재 시간(공유한 시간)
        long ctm = System.currentTimeMillis();
        Date currentDate = new Date(ctm);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");
        //아이템 추가
        CommunityData cData = new CommunityData(profilePhoto, nickName, dateFormat.format(currentDate),
                                                imagePath, contents, typeCateGory);

        mCommunityDatas.add(cData);
        firebaseDB.writeNewCommunityData(cData);
        cAdapter.notifyDataSetChanged();
    }

    private void aboutSetCommunityRecycler() {
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(layoutManager);

        cAdapter = new CommunityAdapter();
        cAdapter.setCommunityDatas(mCommunityDatas);
        cRecyclerView.setAdapter(cAdapter);
    }

    private void init(View v) {
        firebaseDB = new FirebaseDB();
        layoutManager = new LinearLayoutManager(getActivity());
        tabLayoutCategory = (TabLayout)v.findViewById(R.id.tabLayoutCategory);

        mCommunityDatas = new ArrayList<>();

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
                    //case 0: 전체
                    case 1:
                        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 1);
                        break;
                    case 2:
                        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 2);
                        break;
                    case 3:
                        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 3);
                        break;
                    case 4:
                        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 4);
                        break;
                    default:
                        firebaseDB.readCommunityData(mCommunityDatas, cAdapter, 0);
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