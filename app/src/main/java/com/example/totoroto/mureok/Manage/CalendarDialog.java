package com.example.totoroto.mureok.Manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

public class CalendarDialog extends DialogFragment {
    private final String TAG = "CD";
    private MaterialCalendarView mCalendarView;

    public CalendarDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container);

        mCalendarView = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        aboutSetCalendar();
        aboutWaterCnt();
        return view;
    }

    private void aboutSetCalendar() {
        mCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017,0,1))
                .setMaximumDate(CalendarDay.from(2025, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }

    private void aboutWaterCnt() {
        Bundle mArgs = getArguments();
        int mWaterCnt = mArgs.getInt("waterCount");
        Log.d("CD", "waterCount: "+ mWaterCnt);

        //물 카운트 수가 1이상이면 파란원 표시
        if(mWaterCnt > 0){
            mCalendarView.addDecorator(new EventDecorator(getContext()));
        }else{
            //compactCalendar.setCurrentDayBackgroundColor(Color.RED);
        }
    }
}
