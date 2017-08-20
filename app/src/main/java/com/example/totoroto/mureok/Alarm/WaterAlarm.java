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

    public void Alarm(int perDate, String realName,String name, int hour, int minute){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadCast.class);
        intent.putExtra("pRealName", realName);
        intent.putExtra("pName", name);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기
        long currentTime = System.currentTimeMillis();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour,minute, 0);

        long settingTime = calendar.getTimeInMillis();

        if(currentTime > settingTime){
            settingTime += 1000*60*60*24;
        }

        if(perDate != -1) {
            //알람 예약
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, settingTime, sender);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, settingTime, sender);
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, settingTime, AlarmManager.INTERVAL_DAY, sender);
        }else{
            am.cancel(sender);
        }
    }

}
