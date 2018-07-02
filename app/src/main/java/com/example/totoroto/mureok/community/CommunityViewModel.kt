package com.example.totoroto.mureok.community

import android.arch.lifecycle.ViewModel
import com.example.totoroto.mureok.data.Post
import rx.Observable

class CommunityViewModel : ViewModel() {

    fun observePostList(): Observable<Post>{}


}
