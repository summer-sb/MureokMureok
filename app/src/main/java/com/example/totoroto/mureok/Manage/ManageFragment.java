package com.example.totoroto.mureok.Manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.Data.ManageData;
import com.example.totoroto.mureok.PlantTip.TipActivity;
import com.example.totoroto.mureok.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

public class ManageFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "MF";
    private FloatingActionButton fBtnAdd_m;
    private FloatingActionButton fBtnTip_m;
    private FloatingActionMenu fabMenu_m;

    private RecyclerView mRecyclerView;
    private ManageAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ManageData> mItemDatas;
    private FirebaseDBHelper firebaseDBHelper;

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        
        init(view);

        aboutSetRecycler();
        firebaseDBHelper.readManageData(mItemDatas, mAdapter);

        return view;
    }

    private void init(View view) {
        fBtnAdd_m = (FloatingActionButton) view.findViewById(R.id.fBtnAdd_m);
        fBtnTip_m = (FloatingActionButton) view.findViewById(R.id.fBtnTip_m);
        fabMenu_m = (FloatingActionMenu)view.findViewById(R.id.floatingMenu_m);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        firebaseDBHelper = new FirebaseDBHelper();
        mItemDatas = new ArrayList<>();
        fBtnAdd_m.setOnClickListener(this);
        fBtnTip_m.setOnClickListener(this);
    }


    private void aboutSetRecycler() {
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ManageAdapter();
        mAdapter.setItemDatas(mItemDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fabMenu_m.showMenuButton(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 ||dy<0 && fabMenu_m.isShown()) {
                    fabMenu_m.hideMenuButton(true);
                }
            }
        });

    }

    private void addItemFunc() {

        FragmentManager fm = getFragmentManager();
        AddPlantDialog addDialog = new AddPlantDialog();
        addDialog.show(fm, "addPlantDialog");

        addDialog.setDialogResult(new AddPlantDialog.DialogResult() {
            @Override
            public void apply(String picture, String name, String realName, String date) {
                ManageData manageData = new ManageData(picture, name, realName, date,
                        false, 0, 0, 0, "", "물 OFF");
                mItemDatas.add(manageData);
                firebaseDBHelper.writeNewManageData(manageData);

                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void moveTipActivity() {
        Intent intent = new Intent(getContext(), TipActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fBtnAdd_m:
                addItemFunc();
                break;
            case R.id.fBtnTip_m:
                moveTipActivity();
                break;
        }
    }

}
