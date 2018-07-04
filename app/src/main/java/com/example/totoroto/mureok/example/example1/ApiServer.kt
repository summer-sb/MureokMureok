package com.example.totoroto.mureok.example.example1

import android.os.Handler
import android.os.Looper
import android.os.NetworkOnMainThreadException

/**
 * 데이터를 제공하는 서버라고 가정합니다
 * 이 코드는 수정하지 마세요
 * @author Changwoo Hong(chawoo@hpcnt.com)
 * @since 04 - 7월 - 2018
 */
object ApiServer {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val data = "Hello World"


    fun getData(): String {
        if (Thread.currentThread() === mainHandler.looper.thread) {
            throw NetworkOnMainThreadException()
        }
        Thread.sleep(1000)
        return data
    }




}