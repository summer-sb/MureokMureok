package com.example.totoroto.mureok.Data;

import android.util.Log;

import com.example.totoroto.mureok.List.ListAdapter;
import com.example.totoroto.mureok.Manage.ManageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {
    private final String TAG = "FDB";
    private DatabaseReference mRootRef;
    private FirebaseDatabase firebaseDatabase;

    public FirebaseDB() {
    }

    public void writeNewUser(String email, String nickName) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        User user = new User(email, nickName);
        //Uid == primary key
        mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }

    public User readUserProfile(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final User temp = new User();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                temp.setUserEmail(user.getUserEmail());
                temp.setNickName(user.getNickName());
                Log.d(TAG, "in Listener :" +temp.getUserEmail() +"|" + temp.getNickName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Log.d(TAG, "out Listener :" +temp.getUserEmail() +"|" + temp.getNickName());
        return temp;
    }

    public void writeNewManageData(ManageData mData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef =  mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String manageKey = userIdRef.child("manageData").push().getKey();

        Map<String, Object> manageDataUpdate = new HashMap<String, Object>();
        manageDataUpdate.put("/manageData/"+manageKey, mData);
        mData.setFirebaseKey(manageKey); //파이어베이스 키 저장

        userIdRef.updateChildren(manageDataUpdate);
    }

    public void readManageData(final ArrayList<ManageData> manageDatas, final ManageAdapter mAdapter){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference manageRef = FirebaseDatabase.getInstance().getReference().child("users")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("manageData");

        Log.d(TAG, "CurrentUser:"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        manageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                manageDatas.clear();
                for(DataSnapshot s : dataSnapshot.getChildren()){
                    ManageData mData = s.getValue(ManageData.class);
                    manageDatas.add(mData);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateManageAlarmData(String key, ManageData mData){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef =  mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(key);

        Map<String, Object> alarmUpdate = new HashMap<String, Object>();
        alarmUpdate.put("/pIsAlarm", mData.getpIsAlarm());
        alarmUpdate.put("/pPerDate", mData.getpPerDate());
        alarmUpdate.put("/pHour", mData.getpHour());
        alarmUpdate.put("/pMinute", mData.getpMinute());
        alarmUpdate.put("/pAM_PM", mData.getpAM_PM());

        manageRef.updateChildren(alarmUpdate);
    }

    public void deleteManageData(String key){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef =  mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(key);
        manageRef.removeValue();
    }

    public void writeNewListData(ListData listData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef =  mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String listkey = userIdRef.child("listData").push().getKey();

        Map<String, Object> listDataUpdate = new HashMap<String, Object>();
        listDataUpdate.put("/listData/"+listkey, listData);
        listData.setFirebaseKey(listkey);

        userIdRef.updateChildren(listDataUpdate);
    }
    public void readListData(final ArrayList<ListData> listDatas, final ListAdapter listAdapter){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference listRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("listData");

        Log.d(TAG, "CurrentUser:"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDatas.clear();
                for(DataSnapshot s : dataSnapshot.getChildren()){
                    ListData mListData = s.getValue(ListData.class);
                    listDatas.add(mListData);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void shareListData(String key, boolean isShare){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef =  mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("listData").child(key);

        Map<String, Object> shareUpdate = new HashMap<String, Object>();
        shareUpdate.put("/isShare", isShare);
        manageRef.updateChildren(shareUpdate);
    }
    public void deleteListData(String key){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef =  mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("listData").child(key);
        manageRef.removeValue();
    }
}
