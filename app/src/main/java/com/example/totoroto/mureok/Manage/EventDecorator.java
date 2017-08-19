package com.example.totoroto.mureok.Manage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.totoroto.mureok.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventDecorator implements DayViewDecorator{
    private Drawable drawable;
    private ArrayList<CalendarDay> selectDay;

    public EventDecorator(Context context, ArrayList<String> waterDateArray) {
        selectDay = new ArrayList<>();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");

      try {
           for (int i = 0; i < waterDateArray.size(); i++) {
               selectDay.add(CalendarDay.from(format.parse(waterDateArray.get(i))));
           }
       }catch (ParseException e){
           e.printStackTrace();
       }
        drawable = ContextCompat.getDrawable(context, R.drawable.first_day_month);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        for(int i=0; i<selectDay.size(); i++) {
            if (day.equals(selectDay.get(i)))
                return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
