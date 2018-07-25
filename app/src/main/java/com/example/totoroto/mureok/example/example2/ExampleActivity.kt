package com.example.totoroto.mureok.example.example2

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.totoroto.mureok.R
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_example.*
import java.lang.ref.WeakReference

/**
 *
 * TODO ApiServer로부터 데이터를 받아와서 coffeeNameView와 coffeePriceView에 아메리카노의 이름과 가격이 출력되게 하세요
 *
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 05 - 7월 - 2018
 */


class ExampleActivity: RxActivity() {
    private var sum = 0
    private var priceTaskList: MutableList<AsyncTask<Void, Void, Int?>> = mutableListOf()
    //private var stopSingle = Observable.never<String>().firstOrError()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        val idConnectableObservable = ApiServer.loadCoffeeIds().subscribeOn(Schedulers.io()).toObservable().publish()

        val americanoId = idConnectableObservable.flatMap { Observable.fromIterable(it) }
                .filter { it == ApiServer.ID_AMERICANO }
                .firstOrError().onErrorResumeNext { Single.error(CoffeeException(ERROR_ID)) }

        val latteId = idConnectableObservable.flatMap { Observable.fromIterable(it) }
                .filter { it == ApiServer.ID_LATTE }
                .firstOrError().onErrorResumeNext { Single.error(CoffeeException(ERROR_ID)) }


        val americanoName = americanoId.flatMap {
            ApiServer.loadName(it)
                    .onErrorResumeNext { Single.error(CoffeeException(ERROR_NAME)) }
        }

        val latteName = latteId.flatMap {
            ApiServer.loadName(it)
                    .onErrorResumeNext { Single.error(CoffeeException(ERROR_NAME)) }
        }

        Single.zip(americanoName, latteName,
                BiFunction { americano: String, latte: String -> Pair(americano, latte) })
                .observeOn(AndroidSchedulers.mainThread())
                //.takeUntil(stopSingle)
                .bindUntilEvent(this, ActivityEvent.DESTROY)
                .subscribe({
                    val nameConcat = "${it.first} + ${it.second}"
                    coffeeNameView.text = nameConcat
                }, {
                    if (it is CoffeeException) {
                        when (it.errorCode) {
                            ERROR_ID -> Toast.makeText(applicationContext, "데이터 없음", Toast.LENGTH_SHORT).show()
                            ERROR_NAME -> Toast.makeText(applicationContext, "getName() 에러", Toast.LENGTH_SHORT).show()
                        }
                    }
                })


        val americanoPriceSingle = americanoId.flatMap {
            ApiServer.loadPriceCode(it)
                    .onErrorResumeNext {
                        Single.error(CoffeeException(ERROR_PRICE_CODE))
                    }
        }.flatMap {
            ApiServer.loadPrice(it)
                    .onErrorResumeNext {
                        Single.error(CoffeeException(ERROR_PRICE))
                    }.retry(3)
        }

        val lattePriceSingle = latteId.flatMap {
            ApiServer.loadPriceCode(it)
                    .onErrorResumeNext {
                        Single.error(CoffeeException(ERROR_PRICE_CODE))
                    }
        }.flatMap {
            ApiServer.loadPrice(it)
                    .onErrorResumeNext {
                        Single.error(CoffeeException(ERROR_PRICE))
                    }.retry(3)
        }

        Single.zip(americanoPriceSingle, lattePriceSingle,
                BiFunction { americanoPrice: Int, lattePrice: Int -> Pair(americanoPrice, lattePrice) })
                .observeOn(AndroidSchedulers.mainThread())
                //.takeUntil(stopSingle)
                .bindUntilEvent(this, ActivityEvent.DESTROY)
                .subscribe({ coffeePriceView.text = (it.first + it.second).toString() }
                        , {
                    if (it is CoffeeException) {
                        when (it.errorCode) {
                            ERROR_PRICE_CODE -> Toast.makeText(applicationContext, "getPriceCode() 에러", Toast.LENGTH_SHORT).show()
                            ERROR_PRICE -> Toast.makeText(applicationContext, "getPrice() 에러", Toast.LENGTH_SHORT).show()
                        }
                    }
                })


        idConnectableObservable.connect()

//        IdTask(applicationContext, coffeeNameView, coffeePriceView).execute()
    }

    private class CoffeeException(val errorCode: Int = DEFAULT) : IllegalArgumentException()

    private inner class IdTask(context: Context, coffeeNameView: TextView, coffeePriceView: TextView) : AsyncTask<Void, Void, List<String>?>() {
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

                for (i in 0 until result.size) {
                    priceTaskList.add(PriceTask(context, coffeePriceView, result[i],0))
                    priceTaskList[i].executeOnExecutor(THREAD_POOL_EXECUTOR)
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
        const val ERROR_ID = 1
        const val ERROR_NAME = 2
        const val ERROR_PRICE_CODE = 3
        const val ERROR_PRICE = 4
        const val RETRY_COUNT = 100
    }

    override fun onDestroy() {
       // stopSingle = Single.just("stop")

        for (i in 0 until priceTaskList.size) {
            priceTaskList[i].cancel(true)
        }

        super.onDestroy()
    }
}
