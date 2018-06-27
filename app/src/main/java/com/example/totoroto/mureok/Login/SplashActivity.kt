package com.example.totoroto.mureok.Login

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.example.totoroto.mureok.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import java.util.ArrayList

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                //권한이 모두 허용 되고나서 실행
                Toast.makeText(applicationContext, "실행에 필요한 권한이 허용되었습니다", Toast.LENGTH_SHORT).show()

                val handler = Handler()
                handler.postDelayed({
                    //저장공간 퍼미션 체크
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)

                    finish()
                }, 3000)
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                //요청한 권한중에서 거부당한 권한목록을 리턴
                finish()
            }
        }

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("무럭무럭을 정상적으로 이용하기 위해 접근 권한이 필요합니다")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용해 주세요")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()

    }
}
