package com.example.totoroto.mureok.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class WaterAlarm {
    private Context context;

    public WaterAlarm(Context context) {
        this.context = context;
    }

    public void Alarm(int perDate, int hour, int minute){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadCast.class);

        int alarmKey = perDate*hour*minute*10;
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmKey, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, 0);

        if(perDate == -1){
            am.cancel(sender);
        }else if(perDate > 0) {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 60 * 24 * perDate, sender);
        }else{
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
           // am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60, sender);
        }
    }

}
