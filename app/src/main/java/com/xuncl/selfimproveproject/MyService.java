package com.xuncl.selfimproveproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.xuncl.selfimproveproject.receivers.AlarmReceiver;
import com.xuncl.selfimproveproject.receivers.WakeServiceReceiver;
import com.xuncl.selfimproveproject.utils.FileUtils;
import com.xuncl.selfimproveproject.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CLEVO on 2016/9/27.
 */
public class MyService extends Service {
    private static final String TAG = "MyService";

    public static final String KEEP = "KEEP";
    public static final String WAKE = "WAKE";
    public static final String RING = "RING";
    public static final String ALARM = "ALARM";
    public static final String TIME_MILLI = "TIME_MILLI";
    private boolean isRang = true;
    private Calendar c = Calendar.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        callWaker();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //打印时间信息
//                LogUtils.d("LongRunningService", "executed at " + new Date().
//                        toString());
//                // TODO something need time and can be run background
//            }
//        }).start();
        LogUtils.e(TAG,"Remind that I'm alive.");
        String ring = intent.getStringExtra(ALARM);
        switch (ring){
            case RING:
                prepareAlarm(intent.getLongExtra(TIME_MILLI, 0));
                callWaker();
                break;
            case KEEP:
                callWaker();
                break;
            case WAKE:
                callWaker();
                break;
            default:
                break;
        }

        if (!isRang()) {
            wakeAlarm();
        }

        recordLiveTime();
        return START_STICKY_COMPATIBILITY;
    }

    private void recordLiveTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN, Locale.CHINA);
        FileUtils.write(this, sdf.format(new Date()), Constant.SERVICE_LIVE_TXT);
    }


    private void wakeAlarm() {
        if (c.getTimeInMillis() - System.currentTimeMillis() < 60000) {
            LogUtils.i(TAG, "Let's RING!!! " + System.currentTimeMillis());

            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pi);
            setIsRang(true);
        }
    }

    private void prepareAlarm(long millis) {
        if (millis > 0) {
            setIsRang(false);
            c.setTimeInMillis(millis);
            LogUtils.v(TAG, "Now Service receive the ring-time: " + c.getTimeInMillis());
        }
    }

    //pi 会覆盖之前的pi, 不必担心多个pi同时出现
    private void callWaker() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 30000;//每隔三十秒刷新一次
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, WakeServiceReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }


    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    public boolean isRang() {
        return isRang;
    }

    public void setIsRang(boolean isRang) {
        this.isRang = isRang;
    }
}
