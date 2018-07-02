package com.example.totoroto.mureok.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.totoroto.mureok.community.CommunityFragment;
import com.example.totoroto.mureok.community.PostListFragment;
import com.example.totoroto.mureok.list.ListFragment;
import com.example.totoroto.mureok.manage.ManageFragment;


public class TabAdapter extends FragmentStatePagerAdapter {
    private final int numTab = 4;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ManageFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            case 2:
                return CommunityFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTab;
    }
}
