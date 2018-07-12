package com.example.totoroto.mureok.example.example2

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
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

        val printTask = PrintTask(coffeeNameView, coffeePriceView)
        printTask.execute()
    }

    private class PrintTask(coffeeNameView : TextView, coffeePriceView: TextView) : AsyncTask<Void, Void, List<String>>() {
        private val coffeeNameView = WeakReference(coffeeNameView)
        private val coffeePriceView = WeakReference(coffeePriceView)

        override fun doInBackground(vararg params: Void?): List<String> {
            val ids = ApiServer.getCoffeeIds()
            val names = mutableMapOf<String, String>()
            val prices = mutableMapOf<String, Int>()
            val results : MutableList<String> = mutableListOf()

            for(i in 0..1){
                names[ids[i]] = ApiServer.getName(ids[i])
                prices[ids[i]] = ApiServer.getPrice(ApiServer.getPriceCode(ids[i]))
            }

            for(i in 0..1) {
                if(ids[i] == ApiServer.ID_AMERICANO) {
                    results.add(names[ApiServer.ID_AMERICANO] ?: return emptyList())
                    results.add(prices[ApiServer.ID_AMERICANO].toString())
                }
            }
            return results
        }
        override fun onPostExecute(result: List<String>) {
            super.onPostExecute(result)

            coffeeNameView.get()?.text = result[0]
            coffeePriceView.get()?.text = result[1]
        }

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}