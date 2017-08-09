package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.example.totoroto.mureok.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator{
    private Drawable drawable;
    private CalendarDay selectDay;

    public EventDecorator(Context context) {
/* 특정 날짜로 셋할 때
        String strDate = "2017년 8월 30일";
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        try {
            date = format.parse(strDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
*/
        selectDay = CalendarDay.from(new Date()); //오늘 날짜

        drawable = ContextCompat.getDrawable(context, R.drawable.first_day_month);
        Log.d("ED",String.valueOf(selectDay));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(selectDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
