package com.example.totoroto.mureok.example.example1

import android.app.Activity
import android.os.Bundle
import com.example.totoroto.mureok.R

/**
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        // TODO Thread와 Handler를 이용하여 textView에 ApiServer로부터 받아온 데이터를 출력하세요
    }


}