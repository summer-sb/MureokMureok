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
    private var sum = 0
    private var priceTask: MutableList<AsyncTask<Void, Void, Int?>> = mutableListOf()
    private var priceTaskSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        disposable = ApiServer.observeItem().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    textView.text = it.toString()
                }

        IdTask(applicationContext, coffeeNameView, coffeePriceView).execute()
    }

    private inner class IdTask(context : Context, coffeeNameView : TextView, coffeePriceView: TextView) : AsyncTask<Void, Void, List<String>?>() {
        private val context = WeakReference(context)
        private val coffeeNameView = WeakReference(coffeeNameView)
        private val coffeePriceView = WeakReference(coffeePriceView)

        override fun doInBackground(vararg params: Void?): List<String>? {
            val itemList = mutableListOf<String>()
            val idList = ApiServer.getCoffeeIds()


            for (i in 0 until idList.size) {
                when (idList[i]) {
                    ApiServer.ID_AMERICANO, ApiServer.ID_LATTE -> itemList.add(idList[i])
                }
            }

            return if (itemList.size != 0) itemList else null
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)

            if (result != null) {
                NameTask(context, coffeeNameView, result[0]).executeOnExecutor(THREAD_POOL_EXECUTOR)

                priceTaskSize = result.size
                for (i in 0 until result.size) {
                    priceTask.add(PriceTask(context, coffeePriceView, result[i],0))
                    priceTask[i].executeOnExecutor(THREAD_POOL_EXECUTOR)
                }
            } else {
                Toast.makeText(context.get(), "데이터 없음", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private class NameTask(val context : WeakReference<Context>, val coffeeNameView : WeakReference<TextView>, val id : String) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String? {
            return ApiServer.getName(id)
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(result != null) {
                coffeeNameView.get()?.text = result
            } else {
                Toast.makeText(context.get(), "getName 에러 발생", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private inner class PriceTask(val context : WeakReference<Context>, val coffeePriceView: WeakReference<TextView>, val id: String, val count: Int) : AsyncTask<Void, Void, Int?>(){
        var errorCode = DEFAULT

        override fun doInBackground(vararg params: Void?): Int? {
            val price: Int
            val priceCode: String

            try {
                priceCode = ApiServer.getPriceCode(id)

                try {
                    price = ApiServer.getPrice(priceCode)
                } catch (e: IllegalArgumentException) {
                    errorCode = ERROR_PRICE

                    return null
                }

            } catch (e: IllegalArgumentException) {
                errorCode = ERROR_PRICE_CODE

                return null
            }

            return price
        }


        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            if(result == null) {

                when (errorCode) {
                    ERROR_PRICE_CODE -> Toast.makeText(context.get(), "getPriceCode 에러 발생", Toast.LENGTH_SHORT).show()

                    ERROR_PRICE -> {

                        if (count < RETRY_COUNT) {
                            PriceTask(context, coffeePriceView, id, count+1).execute()
                        } else {
                            Toast.makeText(context.get(), "getPrice 에러 발생", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                sum += result
                coffeePriceView.get()?.text = sum.toString()
            }
        }
    }

    companion object {
        const val DEFAULT = 0
        const val ERROR_PRICE_CODE = 2
        const val ERROR_PRICE = 3
        const val RETRY_COUNT = 100
    }

    override fun onDestroy() {
        disposable?.dispose()

        for(i in 0 until priceTaskSize) {
            priceTask[i].cancel(true)
        }

        super.onDestroy()
    }
}
