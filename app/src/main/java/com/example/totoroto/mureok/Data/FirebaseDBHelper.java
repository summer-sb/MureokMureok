package com.example.totoroto.mureok.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.totoroto.mureok.Community.CommentAdapter;
import com.example.totoroto.mureok.Community.CommunityAdapter;
import com.example.totoroto.mureok.List.ListAdapter;
import com.example.totoroto.mureok.Manage.ManageAdapter;
import com.example.totoroto.mureok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDBHelper {
    private final String TAG = "SOLBIN";
    private DatabaseReference mRootRef;
    private FirebaseDatabase firebaseDatabase;

    private IsLikeResult isLikeResult;
    private WaterDateResult mDateResult;
    private IsWater mIsWater;

    public interface IsLikeResult {
        void success();
    }

    public interface WaterDateResult {
        void apply(ArrayList<String> waterDate);
    }

    public interface IsWater {
        void exist();
    }

    public void setIsLikeResult(IsLikeResult result) {
        isLikeResult = result;
    }

    public void setWaterDateResult(WaterDateResult dateResult) {
        mDateResult = dateResult;
    }

  /*  public void setIsWater(IsWater isWater) {
        mIsWater = isWater;
    }
*/
    public FirebaseDBHelper() {
    }

    public void writeNewUser(String email, String nickName, String profileImgPath) {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        User user = new User(email, nickName, profileImgPath);
        //Uid == primary key
        mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }

    public void updateUserProfile(String nickName, String profileImgPath){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("nickName").setValue(nickName);

        mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("profileImgPath").setValue(profileImgPath);
    }

    public void removeUser(String uid){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("users").child(uid).removeValue();
    }

    public void writeNewManageData(ManageData mData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef = mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String manageKey = userIdRef.child("manageData").push().getKey();

        Map<String, Object> manageDataUpdate = new HashMap<String, Object>();
        manageDataUpdate.put("/manageData/" + manageKey, mData);
        mData.setFirebaseKey(manageKey); //파이어베이스 키 저장

        userIdRef.updateChildren(manageDataUpdate);
    }

    public void readManageData(final ArrayList<ManageData> manageDatas, final ManageAdapter mAdapter) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference manageRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("manageData");

        manageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                manageDatas.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
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

    public void updateManageAlarmData(String key, ManageData mData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
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

    public void deleteManageData(String key) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(key);
        manageRef.removeValue();
    }

    public void isWaterCalendarManageData(String key, String date, boolean isWater) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(key);

        if (isWater) {
            manageRef.child("/waterDate").child(date).setValue(date);//2017-01-11
        } else {
            manageRef.child("/waterDate").child(date).removeValue();
        }
    }

    public void readWaterCalendarManageData(String positionKey) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(positionKey);

        manageRef.child("/waterDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> dataList = new ArrayList<>();

                dataList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    dataList.add(childSnapshot.getKey()); //date를 받아와서 배열에 저장
                }
                    mDateResult.apply(dataList); //인터페이스로 전달
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void IsWaterExistManageData(String positionKey){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("manageData").child(positionKey);

        manageRef.child("/waterDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userKey = "";
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    userKey = childSnapshot.getKey();

                    if (!userKey.equals("")) {
                        mIsWater.exist();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void writeNewListData(ListData listData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userIdRef = mRootRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String listkey = userIdRef.child("listData").push().getKey();

        Map<String, Object> listDataUpdate = new HashMap<String, Object>();
        listDataUpdate.put("/listData/" + listkey, listData);
        listData.setFirebaseKey(listkey);

        userIdRef.updateChildren(listDataUpdate);
    }

    public void updateListShareData(String key, ListData listData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("listData").child(key);

        Map<String, Object> isShareUpdate = new HashMap<String, Object>();
        isShareUpdate.put("/isShare", listData.getisShare());
        isShareUpdate.put("/radioFlower", listData.isRadioFlower());
        isShareUpdate.put("/radioHerb", listData.isRadioHerb());
        isShareUpdate.put("/radioCactus", listData.isRadioCactus());
        isShareUpdate.put("/radioVegetable", listData.isRadioVegetable());
        isShareUpdate.put("/radioTree", listData.isRadioTree());

        listRef.updateChildren(isShareUpdate);
    }

    public void updateListData(String key, ListData listData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("listData").child(key);

        Map<String, Object> listUpdate = new HashMap<String, Object>();
        listUpdate.put("/imgPath", listData.getImgPath());
        listUpdate.put("/contents", listData.getContents());
        listUpdate.put("/date", listData.getDate());
        if (listData.getisShare()) {
            listData.setisShare(false);
            listData.setRadioFlower(false);
            listData.setRadioHerb(false);
            listData.setRadioCactus(false);
            listData.setRadioVegetable(false);
            listData.setRadioTree(false);
        }
        listRef.updateChildren(listUpdate);
    }

    public void readListData(final ArrayList<ListData> listDatas, final ListAdapter listAdapter) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference listRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("listData");

        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDatas.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
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

    public void deleteListData(String key) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference manageRef = mRootRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("listData").child(key);
        manageRef.removeValue();
    }

    public void writeNewCommunityData(CommunityData cData, String listFirebaseKey) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference communityRef = mRootRef.child("community");

        Map<String, Object> communityUpdate = new HashMap<String, Object>();
        communityUpdate.put("/communityData/" + listFirebaseKey, cData);

        cData.setFirebaseKey(listFirebaseKey);
        communityRef.updateChildren(communityUpdate);
    }

    public boolean updateNumLikeData(String uid, String positionKey, int numLike, boolean isLike) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cRef = mRootRef.child("community").child("communityData").child(positionKey);

        if (isLike) {
            cRef.child("/likeUsers").child(uid).setValue(uid);

            Map<String, Object> cInfoUpdate = new HashMap<String, Object>();

            cInfoUpdate.put("/numLike", numLike);
            cRef.updateChildren(cInfoUpdate);

            return true;
        } else {
            cRef.child("/likeUsers").child(uid).removeValue(); //좋아요한 유저에서 삭제

            Map<String, Object> cInfoUpdate = new HashMap<String, Object>(); //좋아요-1
            cInfoUpdate.put("/numLike", numLike);
            cRef.updateChildren(cInfoUpdate);

            return false;
        }
    }

    public void isLikeCommunityData(String positionKey, final String uid) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cRef = mRootRef.child("community").child("communityData").child(positionKey);

        cRef.child("likeUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userKey = childSnapshot.getKey();

                    if (userKey.equals(uid)) {
                        isLikeResult.success();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void readCommunityData(final ArrayList<CommunityData> cDatas, final CommunityAdapter cAdapter, final int typeCategory) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cRef = mRootRef.child("community").child("communityData");

        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cDatas.clear();

                if (typeCategory == 1) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);

                        if (mData.getTypeCategory() == 1) {
                            cDatas.add(mData);
                        }
                    }
                } else if (typeCategory == 2) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);

                        if (mData.getTypeCategory() == 2) {
                            cDatas.add(mData);
                        }
                    }
                } else if (typeCategory == 3) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);

                        if (mData.getTypeCategory() == 3) {
                            cDatas.add(mData);
                        }
                    }
                } else if (typeCategory == 4) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);

                        if (mData.getTypeCategory() == 4) {
                            cDatas.add(mData);
                        }
                    }
                } else if (typeCategory == 5) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);

                        if (mData.getTypeCategory() == 5) {
                            cDatas.add(mData);
                        }
                    }
                } else {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        CommunityData mData = s.getValue(CommunityData.class);
                        cDatas.add(mData);
                    }
                }
                cAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void deleteCommunityData(String key) {
        //공유가 취소되었을 때 커뮤니티 데이터를 삭제한다.
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference communityDataRef = mRootRef.child("community").child("communityData").child(key);

        communityDataRef.removeValue();
    }

    public void writeNewCommentData(String key, CommentData commentData) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference communityRef = mRootRef.child("community").child("communityData");
        String commentKey = communityRef.push().getKey();

        Map<String, Object> communityData = new HashMap<String, Object>();
        communityData.put("/" + key + "/commentData/" + commentKey, commentData);

        communityRef.updateChildren(communityData);
    }

    public void readCommentData(String positionKey, final ArrayList<CommentData> commentDatas, final CommentAdapter cAdapter) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference communityRef = mRootRef.child("community").child("communityData")
                .child(positionKey).child("commentData");

        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                commentDatas.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    CommentData cData = s.getValue(CommentData.class);
                    commentDatas.add(cData);
                }
                cAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
