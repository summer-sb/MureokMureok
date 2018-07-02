package com.example.totoroto.mureok.community;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import com.example.totoroto.mureok.data.PostData;
import com.google.firebase.database.DatabaseReference;

public class CommunityViewModel extends ViewModel{
    ObservableField<PostData> postData;
    CommunityRepository repository;
    DatabaseReference rootRef;

    public CommunityViewModel() {

    }

    public ObservableField<PostData> requestPostData(){
        try {
            postData = repository.readPostData(postData.get().typeCategory);




        }catch (NullPointerException e){ e.printStackTrace(); }

        return postData;
    }

}
