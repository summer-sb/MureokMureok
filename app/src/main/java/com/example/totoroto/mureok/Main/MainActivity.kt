package com.example.totoroto.mureok.Main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.totoroto.mureok.Info.InfoActivity
import com.example.totoroto.mureok.Login.LoginActivity
import com.example.totoroto.mureok.Manage.ManageFragment
import com.example.totoroto.mureok.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private val TAG = "SOLBIN"

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var toolbar: Toolbar? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var tvNickName: TextView? = null
    private var tvEmail: TextView? = null
    private var civProfilePicture: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
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

        navigationView!!.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout!!.closeDrawers()

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
            drawerLayout!!.openDrawer(GravityCompat.START)

            //navigation header
            val fUser = FirebaseAuth.getInstance().currentUser
            if (fUser != null) {
                tvEmail!!.text = fUser.email
                tvNickName!!.text = fUser.displayName
                Glide.with(applicationContext)
                        .load(fUser.photoUrl)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(civProfilePicture!!)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun aboutToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_drawer)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun aboutTab() {
        //관리 화면을 첫화면으로
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, ManageFragment.newInstance()).commit()

        //탭 추가
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(R.drawable.selector_tab_manage))
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(R.drawable.selector_tab_gallery))
        tabLayout!!.addTab(tabLayout!!.newTab().setIcon(R.drawable.selector_tab_community))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL //너비를 모두 같게 표시


        val tabAdapter = TabAdapter(supportFragmentManager)
        viewPager!!.adapter = tabAdapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun init() {
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        navigationView = findViewById<View>(R.id.navigationView) as NavigationView
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout

        val header = navigationView!!.getHeaderView(0)
        tvNickName = header.findViewById<View>(R.id.tvProfile_navi) as TextView
        tvEmail = header.findViewById<View>(R.id.tvEmail_navi) as TextView
        civProfilePicture = header.findViewById<View>(R.id.civProfile_navi) as CircleImageView
    }
}
