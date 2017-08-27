package com.example.totoroto.mureok.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Info.InfoActivity;
import com.example.totoroto.mureok.Login.LoginActivity;
import com.example.totoroto.mureok.Manage.ManageFragment;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "SOLBIN";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView tvNickName;
    private TextView tvEmail;
    private CircleImageView civProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        aboutToolbar();
        aboutTab();
        aboutNavi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void aboutNavi() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.navi_item_shares:
                       // moveInfoShareActivity();
                        break;
                    case R.id.navi_item_myInfo:
                        moveInfoActivity();
                        break;
                    case R.id.navi_item_send_opinion:
                        break;
                    case R.id.navi_item_logout:
                        aboutLogout();
                        break;
                }
                return true;
            }
        });
    }


    private void aboutLogout() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void moveInfoActivity() {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);

            //navigation header
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            if(fUser != null) {
                tvEmail.setText(fUser.getEmail());
                tvNickName.setText(fUser.getDisplayName());
                Glide.with(getApplicationContext())
                        .load(fUser.getPhotoUrl())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(civProfilePicture);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void aboutToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void aboutTab() {
        //관리 화면을 첫화면으로
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, ManageFragment.newInstance()).commit();

        //탭 추가
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_manage));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_gallery));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_community));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); //너비를 모두 같게 표시


        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        View header = navigationView.getHeaderView(0);
        tvNickName = (TextView) header.findViewById(R.id.tvProfile_navi);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail_navi);
        civProfilePicture = (CircleImageView) header.findViewById(R.id.civProfile_navi);
    }
}
