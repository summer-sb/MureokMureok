package com.example.totoroto.mureok.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.totoroto.mureok.Community.CommunityFragment;
import com.example.totoroto.mureok.List.ListFragment;
import com.example.totoroto.mureok.Manage.ManageFragment;


public class TabAdapter extends FragmentStatePagerAdapter {
    private final int numTab = 3;

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
