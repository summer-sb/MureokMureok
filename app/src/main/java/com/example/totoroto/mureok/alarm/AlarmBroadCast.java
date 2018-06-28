package com.example.totoroto.mureok.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.totoroto.mureok.manage.ManageAdapter;
import com.example.totoroto.mureok.R;

public class AlarmBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ManageAdapter.class), PendingIntent.FLAG_UPDATE_CURRENT);

        String pRealName = intent.getStringExtra("pRealName");
        String pName = intent.getStringExtra("pName");

        //param2 : unique
        Notification.Builder builder = new Notification.Builder(context);
        if(!pName.equals("")) {
            builder.setSmallIcon(R.drawable.app_icon).setTicker("무럭무럭").setWhen(System.currentTimeMillis())
                    .setContentTitle(pRealName + "(" + pName + ")").setContentText("물 줄 시간이에요!")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
        }else{
            builder.setSmallIcon(R.drawable.app_icon).setTicker("무럭무럭").setWhen(System.currentTimeMillis())
                    .setContentTitle(pRealName).setContentText("물 줄 시간이에요!")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
        }
        notificationmanager.notify(1, builder.build());
    }
}
