package com.example.totoroto.mureok.community

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

class PostListFragment : Fragment() {
    private lateinit var viewModel: CommunityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CommunityViewModel::class.java)
    }
}
