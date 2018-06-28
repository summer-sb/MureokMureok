package com.example.totoroto.mureok.Community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.CommunityData;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.Data.FirebaseStorageHelper;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommunityFragment extends Fragment{
    public static final String TAG = "SOLBIN";
    private FirebaseDBHelper firebaseDBHelper;
    private FirebaseStorageHelper firebaseStorageHelper;

    private TabLayout tabLayoutCategory;
    //about community recycler view
    private LinearLayoutManager layoutManager;
    private RecyclerView cRecyclerView;
    private CommunityAdapter cAdapter;
    private ArrayList<CommunityData> mCommunityDatas;

    private final int SELECT_FLOWER = 1;
    private final int SELECT_HERB = 2;
    private final int SELECT_CACTUS = 3;
    private final int SELECT_VEGETABLE = 4;
    private final int SELECT_TREE = 5;

    private String imgPath;
    private String profilePath;

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
        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 0);

        return view;
    }


    private void aboutItemAdd() {
        final int typeCateGory;

        ListData listData = getArguments().getParcelable("sharedListData");
        imgPath = getArguments().getString("imgUrl");

        final String contents = listData.getContents();
        final String listFirebaseKey = listData.getFirebaseKey();

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

        //파이어베이스 저장소에 이미지 저장
        profilePath = getArguments().getString("userProfilePhoto");
        final String nickName = getArguments().getString("userNickName");

     //   firebaseStorageHelper.profileUpload(profilePath, listData.getFirebaseKey());
     //   profilePath = uri.toString();
        //TODO: 밑에 두줄 추가. 주석처리한 부분 수정.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profilePath = firebaseUser.getPhotoUrl().toString();
        Log.d(TAG, "set profileUrl");

        //현재 시간(공유한 시간)
        long ctm = System.currentTimeMillis();
        Date currentDate = new Date(ctm);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");
        //아이템 추가
        CommunityData cData = new CommunityData(profilePath, nickName, dateFormat.format(currentDate),
                imgPath, contents, typeCateGory, 0);

        mCommunityDatas.add(cData);
        firebaseDBHelper.writeNewCommunityData(cData, listFirebaseKey);
        cAdapter.notifyDataSetChanged();
/*
        firebaseStorageHelper.setPassProfileResult(new FirebaseStorageHelper.PassProfileResult() {
            @Override
            public void pass(Uri uri) {
                profilePath = uri.toString();
                Log.d(TAG, "set profileUrl");

                //현재 시간(공유한 시간)
                long ctm = System.currentTimeMillis();
                Date currentDate = new Date(ctm);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");
                //아이템 추가
                CommunityData cData = new CommunityData(profilePath, nickName, dateFormat.format(currentDate),
                        imgPath, contents, typeCateGory, 0);

                mCommunityDatas.add(cData);
                firebaseDBHelper.writeNewCommunityData(cData, listFirebaseKey);
                cAdapter.notifyDataSetChanged();
            }
        });
*/

    }

    private void aboutSetCommunityRecycler() {
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(layoutManager);

        cAdapter = new CommunityAdapter();
        cAdapter.setCommunityDatas(mCommunityDatas);
        cRecyclerView.setAdapter(cAdapter);
    }

    private void init(View v) {
        firebaseDBHelper = new FirebaseDBHelper();
        firebaseStorageHelper = new FirebaseStorageHelper();

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
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 1);
                        break;
                    case 2:
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 2);
                        break;
                    case 3:
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 3);
                        break;
                    case 4:
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 4);
                        break;
                    case 5:
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 5);
                        break;
                    default:
                        firebaseDBHelper.readCommunityData(mCommunityDatas, cAdapter, 0);
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