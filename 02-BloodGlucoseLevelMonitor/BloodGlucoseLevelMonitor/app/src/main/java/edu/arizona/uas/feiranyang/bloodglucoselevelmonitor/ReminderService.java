package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReminderService extends IntentService {
    private static final String TAG = "ReminderService";

    public static Intent newIntent(Context context){
        return new Intent(context, ReminderService.class);
    }

    public ReminderService(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent){
        Log.d(TAG,"Received an intent: "+ intent);

        // if today's data does not exist, push a notification.
        if(!levelSet.get(this).getToday()) {
            Intent i = MainViewPager.newIntent(this, "add");
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            Notification notification = new NotificationCompat.Builder(this, "default")
                    .setTicker("hah")
                    .setSmallIcon(R.drawable.ic_action_assign)
                    .setContentTitle("Set your today's blood glucose level now")
                    .setContentText("You have not set it yet")
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(0, notification);
        }
    }


    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = ReminderService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn){
            // set calendar to 9 pm
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE,0);

            // weak up at 9pm every day
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
            System.out.println(alarmManager.getNextAlarmClock()==null);

        }else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = ReminderService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        // return if service is already exist
        return pi != null;
    }


    // debug info
    public void onDestroy(){
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
    public void onCreate(){
        Log.d(TAG,"onCreate");
        super.onCreate();
    }
}
