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

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();

        //알람 시간 설정
        if(perDate == 0){
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, minute, 0);
            Log.d("SOLBIN","perdate 0");
        }
        else {
            int oneDay = 24 * 60 * 60 * 1000; //24시간
            Log.d("SOLBIN","oneday*perdate:"+oneDay*perDate);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * perDate, sender);
        }

        /*
        if(Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }else{
            if(Build.VERSION.SDK_INT >= 19){
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }else{
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
        }
        */
    }

}
