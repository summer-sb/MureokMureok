package com.example.totoroto.mureok.example.example1

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import com.example.totoroto.mureok.R
import kotlinx.android.synthetic.main.activity_example.*

/**
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        loadByAsyncTask()
    }

    private fun loadByThread() {
        val handler = Handler()

        Thread(Runnable {
            val data = ApiServer.getData()

            handler.post {
                textView.text = data
            }

        }).start()
    }

    private fun loadByAsyncTask() {
        // TODO AsyncTask를 이용하여 textView에 ApiServer로부터 받아온 데이터를 출력하세요

        ExampleTask().get().execute()
    }

    internal inner class ExampleTask : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg voids: Void): String = ApiServer.getData()

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            textView.text = result
        }
    }
}