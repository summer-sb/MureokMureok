package com.example.totoroto.mureok.example.example2

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.example.totoroto.mureok.R
import com.example.totoroto.mureok.example.example1.ApiServer
import kotlinx.android.synthetic.main.activity_example.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * 최종 결과가 [0, 1, 2, 3, 4]가 나오도록 수정
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 05 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    private val handler = Handler()
    private val list = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_example)
//        runByThread()
        runByAsyncTask()
    }

    private fun runByThread() {
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
        ExampleThread().start()
    }

    private fun runByAsyncTask() {
        ExampleTask(list, textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private inner class ExampleThread(): Thread() {

        override fun run() {
            super.run()

            synchronized(list){
                Log.d(TAG, "========${Thread.currentThread().name} start=======")

                val item = list.size
                Thread.sleep(Random().nextInt(10).toLong())
                list.add(item)

                Log.d(TAG, "========${Thread.currentThread().name} end=======")
            }

            handler.post {
                printCount(list)
            }
        }
    }

    private class ExampleTask(val list: MutableList<Int>, tv: TextView): AsyncTask<Void, Void, List<Int>>() {
        val weakReference = WeakReference<TextView>(tv)

        override fun doInBackground(vararg params: Void): List<Int> {

            synchronized(list){
                Log.d(TAG, "========${Thread.currentThread().name} start=======")
                val item = list.size
                Thread.sleep(Random().nextInt(10).toLong())

                list.add(item)
                Log.d(TAG, "========${Thread.currentThread().name} end=======")
            }
            return list
        }

        override fun onPostExecute(result: List<Int>) {
            super.onPostExecute(result)

            weakReference.get()?.text = list.toString()
        }
    }

    private fun printCount(list: List<Int>) {
        Log.d(TAG, "${Thread.currentThread().name} print start")
        textView.text = list.toString()
        Log.d(TAG, "${Thread.currentThread().name} print end")
    }

    /* Example1 내용
    * Thread, AsyncTask를 이용하여 textView에 ApiServer로부터 받아온 데이터를 출력하세요
    */

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

        Example1Task().execute()
    }

    internal inner class Example1Task : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg voids: Void): String = ApiServer.getData()

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            textView.text = result
        }
    }

    companion object {
        private val TAG = ExampleActivity::class.java.simpleName
    }

}