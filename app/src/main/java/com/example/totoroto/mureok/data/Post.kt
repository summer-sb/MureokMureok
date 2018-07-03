package com.example.totoroto.mureok.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Post(val key: String = "", val imgProfile: String = "", val nickName: String = "", val date: String = "", val imgPicture: String = "",
           val contents: String = "", val typeCategory: Int = 0, var numLike: Int = 0){

    fun getPosts() : List<Post>{
        val postList = mutableListOf<Post>()
        val postRef = FirebaseDatabase.getInstance().reference.child("community").child("communityData")

        postRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (s in dataSnapshot.children) {
                    val data = s.getValue(Post::class.java) ?: return
                    postList.add(data)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return postList
    }
}
