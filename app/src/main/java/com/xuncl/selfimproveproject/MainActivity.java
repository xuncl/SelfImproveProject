package com.xuncl.selfimproveproject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xuncl.selfimproveproject.activities.ActivityCollector;
import com.xuncl.selfimproveproject.activities.BaseActivity;
import com.xuncl.selfimproveproject.activities.TargetActivity;
import com.xuncl.selfimproveproject.activities.TargetAdapter;
import com.xuncl.selfimproveproject.database.DataDeleter;
import com.xuncl.selfimproveproject.database.DataFetcher;
import com.xuncl.selfimproveproject.database.DataUpdater;
import com.xuncl.selfimproveproject.database.MyDatabaseHelper;
import com.xuncl.selfimproveproject.service.Agenda;
import com.xuncl.selfimproveproject.service.Backlog;
import com.xuncl.selfimproveproject.service.Scheme;
import com.xuncl.selfimproveproject.service.Target;
import com.xuncl.selfimproveproject.utils.LogUtils;
import com.xuncl.selfimproveproject.utils.Tools;

public class MainActivity extends BaseActivity implements OnClickListener
{

    private Scheme scheme = new Scheme();

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initTitle();
        initTargets();

        // 集成友盟统计
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this,
                Constant.UMENG_APPKEY, Constant.UMENG_CHANNELID);
        MobclickAgent.startWithConfigure(config);
    }

    private void initTitle()
    {
        ImageView titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(this);
        ImageView titleAdd = (ImageView) findViewById(R.id.title_add);
        titleAdd.setOnClickListener(this);
        ImageView titleRefresh = (ImageView) findViewById(R.id.title_refresh);
        titleRefresh.setOnClickListener(this);
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setOnClickListener(this);
    }

    private void initTargets()
    {
        dbHelper = new MyDatabaseHelper(this, Constant.DB_NAME, null, 1);
        refreshTargets();
    }

    private void refreshTargets()
    {
        Date today = scheme.getDate();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme = DataFetcher.fetchScheme(db, today);
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(scheme.toShortString());
        initList();
        db.close();
    }

    private void initList()
    {
        TargetAdapter adapter = new TargetAdapter(MainActivity.this, R.layout.target_item, scheme.getTargets());
        ListView listView = (ListView) findViewById(R.id.target_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Because of existence of image button, event will not focus on
                // parents.
                Target target = scheme.getTargets().get(position);
                // deleteDialog(target);
                MainActivity.this.showDialog(scheme.getTargets().indexOf(target));
            }
        });
    }

    private void saveTodayTargets()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataUpdater.updateScheme(db, scheme);
        db.close();
    }

    private void addNewTarget(Target target)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme.getTargets().add(target);
        DataUpdater.insertTarget(db, scheme, target);
        db.close();
    }

    @Override
    public void onPause()
    {
        saveAll();
        super.onPause();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.title_back:
            // will save data at life cycle
            ActivityCollector.finishAll();
            break;
        case R.id.title_add:
            onAddTarget();
            break;
        case R.id.title_refresh:
            saveAll();
            fetchAll();
            break;
        case R.id.title_text:
            showSchemeDetail();
            //点击标题则存数据
            save(DataFetcher.getAllScheme(dbHelper.getWritableDatabase(),new Date()));
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String name = data.getStringExtra(Constant.NAME_PARA);
        String des = data.getStringExtra(Constant.DESCRIPTION_PARA);
        String start = data.getStringExtra(Constant.START_PARA);
        String end = data.getStringExtra(Constant.END_PARA);
        int value = data.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE);
        boolean isdone = data.getBooleanExtra(Constant.ISDONE_PARA, false);
        boolean isagenda = data.getBooleanExtra(Constant.ISAGENDA_PARA, false);
        int interval = data.getIntExtra(Constant.INTERVAL_PARA, Constant.BASED_INTERVAL);
        int maxvalue = data.getIntExtra(Constant.MAXVALUE_PARA, Constant.BASED_VALUE);
        switch (requestCode)
        {
        case Constant.RESULT_ADD_TAG:
            if (resultCode == RESULT_OK)
            {

                if (!isagenda)
                {
                    Backlog backlog = new Backlog(scheme.getDate(), name, start, end, des, value, isdone);
                    addNewTarget(backlog);
                }else{
                    Agenda agenda = new Agenda(scheme.getDate(), name, start, end, des, value, interval, maxvalue, isdone);
                    addNewTarget(agenda);
                }
                fetchAll();
            }
            break;
        case Constant.RESULT_MOD_TAG:
            if (resultCode == RESULT_OK)
            {
                Target target = Tools.getTargetByScheme(scheme, name);
                target.setDescription(des);
                target.setValue(value);
                target.setTime(Tools.parseTimeByDate(new Date(), start));
                target.setEndTime(Tools.parseTimeByDate(new Date(), end));
                saveAll();
                fetchAll();
            }
            break;

        }
    }

    private void showSchemeDetail()
    {
        Toast.makeText(MainActivity.this, scheme.toLongString(), Toast.LENGTH_LONG).show();
    }

    private boolean fetchAll()
    {
//        Toast.makeText(MainActivity.this, "fetching...", Toast.LENGTH_SHORT).show();
        refreshTargets();
        return false;
    }

    private void onAddTarget()
    {
        LogUtils.d(Constant.SERVICE_TAG, "into onAddTarget()");
        TargetActivity.anctionStart(MainActivity.this, null, null, null, null, 0, false, false, 0, 0,
                Constant.RESULT_ADD_TAG);

    }

    private void deleteTarget(Target target)
    {
//        Toast.makeText(MainActivity.this, "deleting...", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataDeleter.deleteTarget(db, target);
        scheme.getTargets().remove(target);
        scheme.check();
        db.close();
        fetchAll();
    }

    private boolean saveAll()
    {
//        Toast.makeText(MainActivity.this, "saving...", Toast.LENGTH_SHORT).show();
        saveTodayTargets();
        return false;
    }

    private void deleteDialog(final Target target)
    {
        AlertDialog.Builder builder = new Builder(MainActivity.this);
        builder.setMessage("确认删除" + target.getName() + "吗？");
        builder.setTitle("删除");
        builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                deleteTarget(target);
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        final int index = id;
        Dialog dialog = null;
        Builder builder = new android.app.AlertDialog.Builder(this);
        // 设置对话框的标题
        builder.setTitle("选择您的操作");
        // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
        builder.setItems(R.array.dialog_actions, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String action = getResources().getStringArray(R.array.dialog_actions)[which];
                if (action.equals("删除")) {
                    dialog.dismiss();
                    deleteDialog(scheme.getTargets().get(index));
                } else if (action.equals("修改")) {
                    dialog.dismiss();
                    Target target = scheme.getTargets().get(index);
                    if (target instanceof Agenda) {
                        Agenda agenda = (Agenda) target;
                        TargetActivity.anctionStart(MainActivity.this, agenda.getName(), agenda.getDescription(),
                                Tools.formatTime(agenda.getTime()), Tools.formatTime(agenda.getEndTime()),
                                agenda.getValue(), agenda.isDone(), true, agenda.getInterval(), agenda.getMaxValue(),
                                Constant.RESULT_MOD_TAG);
                    } else {
                        TargetActivity.anctionStart(MainActivity.this, target.getName(), target.getDescription(),
                                Tools.formatTime(target.getTime()), Tools.formatTime(target.getEndTime()),
                                target.getValue(), target.isDone(), false, 0, 0, Constant.RESULT_MOD_TAG);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        // 创建一个列表对话框
        dialog = builder.create();
        return dialog;
    }

    public void save(Serializable object){
        LogUtils.e("scheme","Start save!");
        FileOutputStream out = null;
        ObjectOutputStream oos = null;

        try {
            out = openFileOutput("SchemeData", Context.MODE_PRIVATE);
            oos  = new ObjectOutputStream(out);
            oos.writeObject(object);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtils.e("scheme","End save!");

    }
}
