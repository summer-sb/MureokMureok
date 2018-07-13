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

    private class PrintTask(context : Context, coffeeNameView : TextView, coffeePriceView: TextView) : AsyncTask<Void, Void, Pair<String?, String?>?>() {
        private val context = WeakReference(context)
        private val coffeeNameView = WeakReference(coffeeNameView)
        private val coffeePriceView = WeakReference(coffeePriceView)

        override fun doInBackground(vararg params: Void?): Pair<String?, String?>? {
            val idList = ApiServer.getCoffeeIds()

            for (i in 0 until idList.size) {
                if (idList[i] == ApiServer.ID_AMERICANO) {

                    val nameTask = NameTask(context, coffeeNameView, idList)
                    nameTask.executeOnExecutor(THREAD_POOL_EXECUTOR)

                    val priceTask = PriceTask(context, coffeePriceView, idList)
                    priceTask.executeOnExecutor(THREAD_POOL_EXECUTOR)
                }
            }

            return null
        }
    }

    private class NameTask(val context : WeakReference<Context>, val coffeeNameView : WeakReference<TextView>, val idList : List<String>) : AsyncTask<Void, Void, String>() {
        var name : String ?= null

        override fun doInBackground(vararg params: Void?): String? {
            for (i in 0 until idList.size) {
                if (idList[i] == ApiServer.ID_AMERICANO) {
                    try {
                        name = ApiServer.getName(idList[i])
                    } catch (e: IllegalArgumentException) {
                        return null
                    }
                }
            }
            return name
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(name != null){
                coffeeNameView.get()?.text = name
            }else{
                Toast.makeText(context.get(), "getName 에러 발생", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private class PriceTask(val context : WeakReference<Context>, val coffeePriceView: WeakReference<TextView>, val idList: List<String>) : AsyncTask<Void, Void, String?>(){
        var price: String? = null
        var priceCode: String = ""
        var errorNum = 0

        override fun doInBackground(vararg params: Void?): String? {

            for (i in 0 until idList.size) {
                if (idList[i] == ApiServer.ID_AMERICANO) {

                    try {
                        priceCode = ApiServer.getPriceCode(idList[i])

                        try {
                            price = ApiServer.getPrice(priceCode).toString()
                        } catch (e: IllegalArgumentException) {
                            errorNum = 2
                            return null
                        }
                    } catch (e: IllegalArgumentException) {
                        errorNum = 3
                        return null
                    }
                }
            }
            return price
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(result == null){
                when (errorNum) {
                    2 -> Toast.makeText(context.get(), "getPriceCode 에러 발생", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(context.get(), "getPrice 에러 발생", Toast.LENGTH_SHORT).show()
                }
            }else{
                coffeePriceView.get()?.text = price
            }
        }
    }


    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}