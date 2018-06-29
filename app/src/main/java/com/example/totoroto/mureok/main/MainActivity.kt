package com.example.totoroto.mureok.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.totoroto.mureok.R
import com.example.totoroto.mureok.info.InfoActivity
import com.example.totoroto.mureok.login.LoginActivity
import com.example.totoroto.mureok.manage.ManageFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboutToolbar()
        aboutTab()
        aboutNavi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun aboutNavi() {

        navigationView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout.closeDrawers()

            when (item.itemId) {
                R.id.navi_item_myInfo -> moveInfoActivity()
                R.id.navi_item_send_opinion -> {
                }
                R.id.navi_item_logout -> aboutLogout()
            }
            true
        }
    }


    private fun aboutLogout() {
        val fAuth = FirebaseAuth.getInstance()
        fAuth.signOut()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(applicationContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun moveInfoActivity() {
        val intent = Intent(applicationContext, InfoActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)

            //navigation header
            val fUser = FirebaseAuth.getInstance().currentUser
            if (fUser != null) {
                tvEmail_navi.text = fUser.email
                tvProfile_navi.text = fUser.displayName
                Glide.with(applicationContext)
                        .load(fUser.photoUrl)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(civProfile_navi)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun aboutToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar ?: return
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun aboutTab() {
        //관리 화면을 첫화면으로
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, ManageFragment.newInstance()).commit()

        //탭 추가
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_manage))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_gallery))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_community))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL //너비를 모두 같게 표시


        val tabAdapter = TabAdapter(supportFragmentManager)
        viewPager.adapter = tabAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    companion object {
        const val TAG = "SOLBIN"
    }
}
