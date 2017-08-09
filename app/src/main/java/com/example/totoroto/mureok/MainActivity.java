package com.example.totoroto.mureok;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.totoroto.mureok.List.ListFragment;
import com.example.totoroto.mureok.Manage.ManageFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        aboutToolbar_Navi();
        aboutTabViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void aboutToolbar_Navi() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void aboutTabViewPager() {
        //관리 화면을 첫화면으로
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, ManageFragment.newInstance()).commit();

        //탭 추가
       tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_list));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_community));
       tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); //너비를 모두 같게 표시

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selectFragment = null;
                switch (position){
                    case 0:
                        selectFragment = ManageFragment.newInstance();
                        break;
                    case 1:
                        selectFragment = ListFragment.newInstance();
                        break;
                    case 2:
                        selectFragment = CommunityFragment.newInstance();
                        break;
                    default:
                        //
                }
                //선택된 프래그먼트로 replace 해준다.
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, selectFragment).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
    private void init() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationView = (NavigationView)findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
    }
}
