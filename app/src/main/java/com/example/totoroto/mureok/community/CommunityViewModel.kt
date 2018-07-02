package com.example.totoroto.mureok.community

import android.arch.lifecycle.ViewModel
import com.example.totoroto.mureok.data.Post
import io.reactivex.Observable

class CommunityViewModel : ViewModel() {

    fun observePostList(): Observable<List<Post>> {
        val list :Observable<List<Post>> = Observable.empty()

        return list
    }


}
