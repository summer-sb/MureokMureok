package com.example.totoroto.mureok.example.example2

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.totoroto.mureok.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_example.*
import java.lang.ref.WeakReference

/**
 *
 * TODO ApiServer로부터 데이터를 받아와서 coffeeNameView와 coffeePriceView에 아메리카노의 이름과 가격이 출력되게 하세요 (AsyncTask를 사용할 것)
 *
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 05 - 7월 - 2018
 */
class ExampleActivity: Activity() {
    private var disposable : Disposable ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        disposable = ApiServer.observeItem().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe{
                                   textView.text = it.toString()
                               }

        val printTask = PrintTask(applicationContext, coffeeNameView, coffeePriceView)
        printTask.execute()
    }

    private class PrintTask(context : Context, coffeeNameView : TextView, coffeePriceView: TextView) : AsyncTask<Void, String, Pair<String?, String?>?>() {
        private val context = WeakReference(context)
        private val coffeeNameView = WeakReference(coffeeNameView)
        private val coffeePriceView = WeakReference(coffeePriceView)

        override fun doInBackground(vararg params: Void?): Pair<String?, String?>? {
            val idList = ApiServer.getCoffeeIds()
            var name : String ?= null
            var price : String ?= null

            for(i in 0 until idList.size) {
                if (idList[i] == ApiServer.ID_AMERICANO) {
                    try {
                        name = ApiServer.getName(idList[i])
                        price = ApiServer.getPrice(ApiServer.getPriceCode(idList[i])).toString()
                    }catch (e: IllegalArgumentException){
                        publishProgress("에러 발생")
                    }
                    return Pair(name , price)
                }
            }
            publishProgress("데이터 없음")
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            Toast.makeText(context.get(), values[0],Toast.LENGTH_SHORT).show()
        }

        override fun onPostExecute(result: Pair<String?, String?>?) {
            super.onPostExecute(result)

            coffeeNameView.get()?.text = result?.first
            coffeePriceView.get()?.text = result?.second
        }

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}