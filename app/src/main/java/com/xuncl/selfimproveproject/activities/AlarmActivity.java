package com.xuncl.selfimproveproject.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.xuncl.selfimproveproject.R;

import java.io.IOException;


public class AlarmActivity extends Activity {


    private MediaPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        player = MediaPlayer.create(this, R.raw.alarm_fairytail);

        //显示对话框
        new AlertDialog.Builder(AlarmActivity.this).
                setTitle("IT'S TIME TO").//设置标题
                setMessage("CHALLENGE YOURSELF！").//设置内容
                setPositiveButton("WILL DO", new OnClickListener(){//设置按钮
            public void onClick(DialogInterface dialog, int which) {
                //TODO Record what challenge user has accepted.
                stopRing();
                AlarmActivity.this.finish();//关闭Activity
            }
        }).create().show();

        ringAlarm();
    }

    private void stopRing() {
        player.stop();
    }

    private void ringAlarm() {
        player.start();
    }


}