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

    private class PrintTask(context : Context, coffeeNameView : TextView, coffeePriceView: TextView) : AsyncTask<Void, Void, Pair<String, String>?>() {
        private val context = WeakReference(context)
        private val coffeeNameView = WeakReference(coffeeNameView)
        private val coffeePriceView = WeakReference(coffeePriceView)

        override fun doInBackground(vararg params: Void?): Pair<String, String>? {
            val idList = ApiServer.getCoffeeIds()

            for(i in 0 until idList.size) {
                if (idList[i] == ApiServer.ID_AMERICANO) {
                    var name = ""
                    var price = ""
                    var priceCode = ""

                    try {
                        name = ApiServer.getName(idList[i])
                    } catch (e: IllegalArgumentException) {
                        name = "getName 에러 발생"
                    }

                    try {
                        priceCode = ApiServer.getPriceCode(idList[i])

                        try {
                            price = ApiServer.getPrice(priceCode).toString()
                        } catch (e: IllegalArgumentException) {
                            name = "getPrice 에러 발생"
                        }

                    } catch (e: IllegalArgumentException) {
                        name = "getPriceCode 에러 발생"
                    }

                    return Pair(name , price)
                }
            }

            return null
        }

        override fun onPostExecute(result: Pair<String, String>?) {
            super.onPostExecute(result)

            if (result == null) {
                Toast.makeText(context.get(), "데이터 없음", Toast.LENGTH_SHORT).show()
            } else {
                when (result.first) {
                    "getName 에러 발생" -> Toast.makeText(context.get(), result.first, Toast.LENGTH_SHORT).show()
                    "getPriceCode 에러 발생" -> Toast.makeText(context.get(), result.first, Toast.LENGTH_SHORT).show()
                    "getPrice 에러 발생" -> Toast.makeText(context.get(), result.first, Toast.LENGTH_SHORT).show()
                    else -> {
                        coffeeNameView.get()?.text = result.first
                        coffeePriceView.get()?.text = result.second
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}