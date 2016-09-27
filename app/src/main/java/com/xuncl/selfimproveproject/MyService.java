package com.xuncl.selfimproveproject;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by CLEVO on 2016/9/27.
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.alarm_fairytail_short);

//        //显示对话框
//        new AlertDialog.Builder(MyService.this).
//                setTitle("IT'S TIME TO").//设置标题
//                setMessage("CHALLENGE YOURSELF！").//设置内容
//                setPositiveButton("WILL DO", new DialogInterface.OnClickListener(){//设置按钮
//            public void onClick(DialogInterface dialog, int which) {
//                //TODO Record what challenge user has accepted.
//                stopRing();
//            }
//        }).create().show();

        ringAlarm();
    }

    private void stopRing() {
        player.stop();
    }

    private void ringAlarm() {
        player.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // 在service中重写下面的方法，这个方法有三个返回值，
        // START_STICKY（或START_STICKY_COMPATIBILITY）是service被kill掉后自动重写创建
        return START_STICKY_COMPATIBILITY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

}
