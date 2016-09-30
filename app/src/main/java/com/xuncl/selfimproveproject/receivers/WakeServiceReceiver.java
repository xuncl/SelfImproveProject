package com.xuncl.selfimproveproject.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xuncl.selfimproveproject.MyService;
import com.xuncl.selfimproveproject.utils.LogUtils;

/**
 * Created by CLEVO on 2016/9/30.
 */
public class WakeServiceReceiver extends BroadcastReceiver {
    private static String TAG = "WakeServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //更加流氓的写法，保持service永远在线
        Intent intent2 = new Intent(context, MyService.class);
        intent2.putExtra(MyService.ALARM, MyService.WAKE);
        context.startService(intent2);
    }
}
