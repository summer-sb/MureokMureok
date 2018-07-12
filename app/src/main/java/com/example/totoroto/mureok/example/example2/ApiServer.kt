package com.example.totoroto.mureok.example.example2

import android.os.Looper
import android.os.NetworkOnMainThreadException
import io.reactivex.Observable

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

    fun observeItem(): Observable<Int> {
        return Observable.create {
            checkThread()
            Thread.sleep(5000)
            it.onNext(0)
            it.onComplete()
        }
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
            else -> "잘못된 입력"
        }
    }

    fun getPrice(priceCode: String): Int {
        checkThread()
        return when (priceCode) {
            PRICE_CODE_2000 -> 2000
            PRICE_CODE_2500 -> 2500
            else -> -1
        }
    }

    fun getName(id: String): String {
        checkThread()
        return when (id) {
            ID_AMERICANO -> "아메리카노"
            ID_LATTE -> "라떼"
            else -> "잘못된 입력"
        }
    }

    private fun checkThread() {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            throw NetworkOnMainThreadException()
        }
    }




}