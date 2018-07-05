package com.example.totoroto.mureok.example.example2

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.totoroto.mureok.R
import kotlinx.android.synthetic.main.activity_example.*
import java.util.*

/**
 * TODO 최종 결과가 [0, 1, 2, 3, 4]가 나오도록 수정
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 05 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    private val handler = Handler()
    private val list = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        runByThread()
//        runByAsyncTask()
    }

    private fun runByThread() {
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
    }

    private fun runByAsyncTask() {
        ExampleTask().execute()
        ExampleTask().execute()
        ExampleTask().execute()
        ExampleTask().execute()
        ExampleTask().execute()
    }

    private inner class ExampleThread(): Thread() {

        override fun run() {
            super.run()
            Log.d(TAG, "========${Thread.currentThread().name} start=======")
            val item = list.size
            Thread.sleep(Random().nextInt(10).toLong())
            list.add(item)
            Log.d(TAG, "========${Thread.currentThread().name} end=======")
            handler.post {
                printCount(list)
            }
        }
    }




    private inner class ExampleTask: AsyncTask<Void, Void, List<Int>>() {

        override fun doInBackground(vararg params: Void): List<Int> {
            Log.d(TAG, "========${Thread.currentThread().name} start=======")
            val item = list.size
            Thread.sleep(Random().nextInt(10).toLong())
            list.add(item)
            Log.d(TAG, "========${Thread.currentThread().name} end=======")
            return list
        }

        override fun onPostExecute(result: List<Int>) {
            super.onPostExecute(result)
            printCount(result)
        }
    }

    private fun printCount(list: List<Int>) {
        Log.d(TAG, "${Thread.currentThread().name} print start")
        textView.text = list.toString()
        Log.d(TAG, "${Thread.currentThread().name} print end")
    }

    companion object {
        private val TAG = ExampleActivity::class.java.simpleName
    }

}