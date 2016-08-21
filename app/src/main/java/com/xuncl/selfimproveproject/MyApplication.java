package com.xuncl.selfimproveproject;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application
{
    private static Context context;
    public static RequestQueue queue;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static Context getContext()
    {
        return context;
    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }

}
