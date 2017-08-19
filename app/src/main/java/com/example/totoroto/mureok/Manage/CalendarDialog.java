package com.example.totoroto.mureok.Manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoroto.mureok.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarDialog extends DialogFragment {
    private MaterialCalendarView mCalendarView;
    private ArrayList<String> waterDateArray;

    public CalendarDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container);

        waterDateArray = new ArrayList<>();
        mCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        aboutSetCalendar();
        aboutWaterDate();
        return view;
    }

    private void aboutSetCalendar() {
        mCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2025, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }

    private void aboutWaterDate() {
        Bundle mArgs = getArguments();
        waterDateArray = mArgs.getStringArrayList("waterDateArray");

        mCalendarView.addDecorator(new EventDecorator(getContext(), waterDateArray));
    }
}
