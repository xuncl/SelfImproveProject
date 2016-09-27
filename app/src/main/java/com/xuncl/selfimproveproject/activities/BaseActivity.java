package com.xuncl.selfimproveproject.activities;


import android.app.Activity;
import android.os.Bundle;

//import com.umeng.analytics.MobclickAgent;
import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.utils.LogUtils;

public class BaseActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LogUtils.d(Constant.BASE_ACTIVITY_TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    // 友盟的会话统计
    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }
    
}
