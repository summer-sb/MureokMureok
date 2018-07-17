package com.example.totoroto.mureok.example.example2

import android.os.Looper
import android.os.NetworkOnMainThreadException
import io.reactivex.Observable
import io.reactivex.Single

/**
 * 데이터를 제공하는 서버라고 가정합니다
 * 이 코드는 수정하지 마세요
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
object ApiServer {

    const val ID_AMERICANO = "ID_AMERICANO"
    const val ID_LATTE = "ID_LATTE"
    const val PRICE_CODE_2000 = "PRICE_CODE_2000"
    const val PRICE_CODE_2500 = "PRICE_CODE_2500"

    fun loadCoffeeIds(): Single<List<String>> = Single.fromCallable {
        return@fromCallable getCoffeeIds()
    }

    fun loadPriceCode(id: String): Single<String> = Single.fromCallable {
        return@fromCallable getPriceCode(id)
    }

    fun loadPrice(priceCode: String): Single<Int> = Single.fromCallable {
        return@fromCallable getPrice(priceCode)
    }

    fun loadName(id: String): Single<String> = Single.fromCallable {
        return@fromCallable getName(id)
    }

    fun getCoffeeIds(): List<String> {
        checkThread()
        return listOf(ID_AMERICANO, ID_LATTE)
    }

    fun getPriceCode(id: String): String {
        checkThread()
        return when (id) {
            ID_AMERICANO -> PRICE_CODE_2000
            ID_LATTE -> PRICE_CODE_2500
            else -> throw IllegalArgumentException()
        }
    }

    fun getPrice(priceCode: String): Int {
        checkThread()
        return when (priceCode) {
            PRICE_CODE_2000 -> 2000
            PRICE_CODE_2500 -> 2500
            else -> throw IllegalArgumentException()
        }
    }

    fun getName(id: String): String {
        checkThread()
        return when (id) {
            ID_AMERICANO -> "아메리카노"
            ID_LATTE -> "라떼"
            else -> throw IllegalArgumentException()
        }
    }

    private fun checkThread() {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            throw NetworkOnMainThreadException()
        }
    }




}