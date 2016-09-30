package com.xuncl.selfimproveproject.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuncl.selfimproveproject.MyService;
import com.xuncl.selfimproveproject.activities.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }
}