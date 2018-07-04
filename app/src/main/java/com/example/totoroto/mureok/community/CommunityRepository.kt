package com.example.totoroto.mureok.community

import com.example.totoroto.mureok.data.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import java.util.concurrent.Callable


class CommunityRepository {
    interface PostCallback{
        fun apply(posts : List<Post>)
    }

    var callback = object : PostCallback{
        override fun apply(posts: List<Post>) {}
    }

    fun setPosts(p : PostCallback){
        callback = p
    }

    fun getPosts(){
        val postList = mutableListOf<Post>()
        val postRef = FirebaseDatabase.getInstance().reference.child("community").child("communityData")

        postRef.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot : DataSnapshot) {
                for(s in snapshot.children){
                    val data = s.getValue(Post::class.java) ?: return
                    postList.add(data)
                }
                callback.apply(postList)
            }

            override fun onCancelled(err : DatabaseError?) {}
        })
    }



    fun getPostList() : Single<List<Post>> {
        var postList : List<Post> = listOf()
        getPosts()

        setPosts(object :PostCallback{
            override fun apply(posts: List<Post>) {
                postList = posts
            }
        })

        return Single.fromCallable(Callable { postList })
    }
}

