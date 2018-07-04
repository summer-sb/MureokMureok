package com.example.totoroto.mureok.example

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
    private val data = listOf("스타벅스", "커피빈", "이디야", "탐앤탐스", "할리스", "폴바셋", "앤젤리너스")


    fun getData(): List<String> {
        if (Thread.currentThread() === mainHandler.looper.thread) {
            throw NetworkOnMainThreadException()
        }
        Thread.sleep(1000)
        return data
    }




}