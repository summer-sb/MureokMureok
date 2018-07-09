package com.example.totoroto.mureok.example.example2

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.example.totoroto.mureok.R
import com.example.totoroto.mureok.R.id.text
import com.example.totoroto.mureok.R.id.textView
import com.google.android.gms.internal.tv
import kotlinx.android.synthetic.main.activity_example.*
import org.w3c.dom.Text
import java.lang.ref.WeakReference
import java.util.*

/**
 * TODO 최종 결과가 [0, 1, 2, 3, 4]가 나오도록 수정
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 05 - 7월 - 2018
 */
class ExampleActivity: Activity() {

    private val handler = Handler()
    private val list = mutableListOf<Int>()
    private lateinit var tv: TextView

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
        tv = textView
        ExampleTask(list, tv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, tv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, tv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, tv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        ExampleTask(list, tv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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

    private class ExampleTask(val list: MutableList<Int>, val textView: TextView): AsyncTask<Void, Void, List<Int>>() {

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

            textView.text = list.toString()
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