package com.example.totoroto.mureok.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.totoroto.mureok.R

/**
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        // TODO ApiServer로 부터 data를 받아와서 recyclerView에 표시하시오
    }


}