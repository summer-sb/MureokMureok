package com.example.totoroto.mureok.Manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ManageData;
import com.example.totoroto.mureok.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ManageFragment extends Fragment {
    private final String TAG = "MF";
    private FloatingActionButton fBtnAdd_m;
    private RecyclerView mRecyclerView;
    private ManageAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ManageData> mItemDatas;
    private FirebaseDB firebaseDB;

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        mItemDatas = new ArrayList<>();

        fBtnAdd_m = (FloatingActionButton)view.findViewById(R.id.fBtnAdd_m);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.mRecyclerView);
        firebaseDB = new FirebaseDB();

        addItemFunc();
        aboutSetRecycler();
        firebaseDB.readManageData(mItemDatas, mAdapter);

        return view;
    }


    private void aboutSetRecycler() {
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ManageAdapter();
        mAdapter.setItemDatas(mItemDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addItemFunc() {

        fBtnAdd_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddPlantDialog addDialog = new AddPlantDialog();
                addDialog.show(fm, "addPlantDialog");

                addDialog.setDialogResult(new AddPlantDialog.DialogResult() {
                    @Override
                    public void apply(String picture, String name, String realName, String date) {
                        ManageData manageData = new ManageData(picture, name, realName, date, 0,
                                false, 0, 0, 0, "");
                        mItemDatas.add(manageData);
                        firebaseDB.writeNewManageData(manageData);

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
