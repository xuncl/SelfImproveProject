package com.xuncl.selfimproveproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听开关机，模式转换等，然后唤醒后台服务
 * Created by CLEVO on 2016/9/27.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        i.putExtra(MyService.ALARM, MyService.KEEP);
        context.startService(i);
    }
}
