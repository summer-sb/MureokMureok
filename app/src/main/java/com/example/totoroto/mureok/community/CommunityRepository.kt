package com.example.totoroto.mureok.community

import com.example.totoroto.mureok.data.Post
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Callable


class CommunityRepository {

    fun getPostList() : Single<List<Post>> {
        val post = Post()

        return Single.fromCallable(Callable { post.getPosts()})
                .flatMap { posts -> Single.fromObservable(Observable.fromArray(posts))}
    }
}
