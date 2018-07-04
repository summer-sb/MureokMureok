package com.example.totoroto.mureok.example.example1

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.totoroto.mureok.R
import kotlinx.android.synthetic.main.activity_example.*

/**
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
class ExampleActivity: Activity() {
    val handler = Handler()
    var data : String ?= null

    fun loadThread() {

        val thread = Thread(Runnable {
            data = ApiServer.getData()

            handler.post {
                textView.setText(data)
            }

        })
        thread.start()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        // TODO Thread와 Handler를 이용하여 textView에 ApiServer로부터 받아온 데이터를 출력하세요

        loadThread()
    }
}