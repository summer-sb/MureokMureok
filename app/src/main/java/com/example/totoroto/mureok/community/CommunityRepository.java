package com.example.totoroto.mureok.community;

import android.databinding.ObservableField;
import com.example.totoroto.mureok.data.FirebaseDBHelper;
import com.example.totoroto.mureok.data.PostData;

public class CommunityRepository {
    FirebaseDBHelper firebaseDBHelper = new FirebaseDBHelper();

    public CommunityRepository() {

    }

    public ObservableField<PostData> readPostData(int typeCategory){
        ObservableField<PostData> data = new ObservableField<>();

        return data;
    }

}
