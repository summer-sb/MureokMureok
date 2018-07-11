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

    fun observeItem(): Observable<Int> {
        return Observable.create {
            checkThread()
            Thread.sleep(100)
            it.onNext(0)
            it.onComplete()
        }
    }

    private fun checkThread() {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            throw NetworkOnMainThreadException()
        }
    }




}