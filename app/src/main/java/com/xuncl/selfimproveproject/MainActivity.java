package com.xuncl.selfimproveproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import com.xuncl.selfimproveproject.utils.FileUtils;
import com.xuncl.selfimproveproject.utils.HttpUtils;
import com.xuncl.selfimproveproject.utils.LogUtils;
import com.xuncl.selfimproveproject.utils.Tools;

public class MainActivity extends BaseActivity implements OnClickListener {

    private PopupMenu popupMenu;
    private Scheme scheme = new Scheme();
    private static Date today = new Date();
    private MyDatabaseHelper dbHelper;
    private String path = Environment.getExternalStorageDirectory().getPath()+"/scheme";
    // 记录ProgressBar的完成进度
    int progressStatus = 0;
    TextView titleText;
    ProgressBar bar;


    /**
     * 初始化today变量
     */
    static {
        initToday();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    /**
     * 初始化today变量，使其为今天的23点59分，用于右划边界的判断
     */
    private static void initToday() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN, Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN +" HH:mm", Locale.CHINA);
        String timeStr = sdf.format(today);
        timeStr = timeStr + " 23:59";
        try {
            today = sdf2.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化标题栏的资源
     */
    private void initTitle() {
        ImageView titleBack = (ImageView) findViewById(R.id.title_back);
        titleBack.setOnClickListener(this);
        ImageView titleMore = (ImageView) findViewById(R.id.title_more);
        titleMore.setOnClickListener(this);
        ImageView titleAdd = (ImageView) findViewById(R.id.title_add);
        titleAdd.setOnClickListener(this);
        ImageView titleRefresh = (ImageView) findViewById(R.id.title_refresh);
        titleRefresh.setOnClickListener(this);
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setOnClickListener(this);
        initPopupMenu(titleMore);
        initBar();
    }

    private void initPopupMenu(ImageView titleMore) {
        popupMenu = new PopupMenu(this, titleMore);
        Menu menu = popupMenu.getMenu();

        // 通过XML文件添加菜单项
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu, menu);

        // 监听事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.upload_btn:
                        Toast.makeText(MainActivity.this, "upload",
                                Toast.LENGTH_LONG).show();
                        startUpdate();
                        break;
                    case R.id.download_btn:
                        Toast.makeText(MainActivity.this, "download",
                                Toast.LENGTH_LONG).show();
//                        startDownload();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 初始化进度条
     */
    private void initBar(){
        bar = (ProgressBar) findViewById(R.id.bar);
    }


    /**
     * 初始化今日的计划
     */
    private void initTargets() {
        dbHelper = new MyDatabaseHelper(this, Constant.DB_NAME, null, 2);
        refreshTargets();
    }

    /**
     * 刷新页面
     */
    private void refreshTargets() {
        Date today = scheme.getDate();
        setHomeSchemeByDate(today);
    }

    /**
     * 将指定的日期的计划显示在主页上
     * @param today 日期
     */
    private void setHomeSchemeByDate(Date today) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme = DataFetcher.fetchScheme(db, today);
        titleText.setText(scheme.toShortString());
        initList();
        db.close();
    }

    /**
     * 初始化计划列表
     */
    private void initList() {
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

    /**
     * 保存当前主页的计划
     */
    private void saveTodayTargets() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataUpdater.updateScheme(db, scheme);
        db.close();
    }

    private void saveScheme(Scheme s){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataUpdater.updateScheme(db, s);
        db.close();
    }

    /**
     * 添加一个新的计划项目到本页的计划组
     * @param target 要加入的计划
     */
    private void addNewTarget(Target target) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        scheme.getTargets().add(target);
        DataUpdater.insertTarget(db, scheme, target);
        db.close();
    }

    @Override
    public void onPause() {
        saveAll();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                // will saveFile data at life cycle
                ActivityCollector.finishAll();
                break;
            case R.id.title_add:
                onAddTarget();
                break;
            case R.id.title_refresh:
                saveAll();
                fetchAll();
                break;
            case R.id.title_more:
                popupMenu.show();
                break;
            case R.id.title_text:
                showSchemeDetail();
                break;
            default:
                break;
        }
    }


    /**
     * 上传所有历史scheme
     */
    private void startUpdate(){
        // 创建一个复杂更新进度的Handler
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    bar.setProgress(progressStatus);
                }
            }
        };

        // 启动线程来执行任务
        new Thread() {
            public void run() {
                String fromDate = FileUtils.read(MainActivity.this,Constant.UPLOAD_FILE_NAME);
                Date veryFirstDay = Tools.parseTimeByDate(fromDate,Constant.DEFAULT_TIME);
                Date thisDay = Tools.parseTimeByDate(today,Constant.DEFAULT_TIME_AFTER); // 比默认时间晚一点
                int intervalDays = Tools.daysBetween(veryFirstDay, thisDay);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN,Locale.CHINA);
                while (thisDay.after(veryFirstDay)) {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    // 获取耗时的完成百分比
                    int interval = Tools.daysBetween(veryFirstDay, thisDay);
                    progressStatus = 100*(intervalDays-interval)/intervalDays;
                    Scheme thisScheme = DataFetcher.fetchScheme(db,thisDay);
                    boolean isEmpty = false;
                    if (thisScheme!=null){
                        if (thisScheme.getTargets().size()<1){
//                            LogUtils.e("update", ""+sdf.format(thisDay)+"'s target's is EMPTY!");
                            isEmpty = true;
                        }
                    }else{
                        LogUtils.e("update", ""+sdf.format(thisDay)+"'s target's is NULLLLLLL!");
                    }
                    HttpUtils.postSchemeJson(thisScheme);
                    final String showing = ""+interval+"/"+intervalDays+" "+sdf.format(thisDay)
                            +(isEmpty?" EMPTY":" updating");
                    runOnUiThread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            titleText.setText(showing);
                        }
                    });

                    thisDay=Tools.prevDay(thisDay);
                    Message m = new Message();
                    m.what = 0x111;
                    // 发送消息到Handler
                    handler.sendMessage(m);
                }
                FileUtils.write(MainActivity.this, sdf.format(today), Constant.UPLOAD_FILE_NAME);
                db.close();
            }
        }.start();


    }

    private void startDownload() {
        // TODO 改为由天数更新的方式，本地没有哪天则更新哪天
        // 创建一个复杂更新进度的Handler
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    bar.setProgress(progressStatus);
                }
            }
        };

        // 启动线程来执行任务
        new Thread() {
            public void run() {
                String fromDate = FileUtils.read(MainActivity.this,Constant.DOWNLOAD_FILE_NAME);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                for (int i=1; i<5496;i++){
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    // 获取耗时的完成百分比
                    progressStatus = 100*(i)/5495;
                    HttpUtils.getTargetJson(db, i);
                    final String showing = "download "+i+"/5495";
                    runOnUiThread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            titleText.setText(showing);
                        }
                    });

                    Message m = new Message();
                    m.what = 0x111;
                    // 发送消息到Handler
                    handler.sendMessage(m);
                }
                db.close();
            }
        }.start();


    }


    int lastX;
    int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = x;
                lastY = y;

                break;

            case MotionEvent.ACTION_MOVE:

                //计算移动的距离
                int offX = x - lastX;
                int offY = y - lastY;
                //调用layout方法来重新放置它的位置
               // 左划右划载入昨天或明天的计划组。
                if (offX>200){
                    lastX = x;
                    setHomeSchemeByDate(Tools.prevDay(scheme.getDate()));
                }else if (offX<-200){
                    if (!(Tools.nextDay(scheme.getDate()).after(today))){
                        lastX = x;
                        setHomeSchemeByDate(Tools.nextDay(scheme.getDate()));
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 获取添加计划的回调函数
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getStringExtra(Constant.NAME_PARA);
        String des = data.getStringExtra(Constant.DESCRIPTION_PARA);
        String start = data.getStringExtra(Constant.START_PARA);
        String end = data.getStringExtra(Constant.END_PARA);
        int value = data.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE);
        boolean isdone = data.getBooleanExtra(Constant.ISDONE_PARA, false);
        boolean isagenda = data.getBooleanExtra(Constant.ISAGENDA_PARA, false);
        int interval = data.getIntExtra(Constant.INTERVAL_PARA, Constant.BASED_INTERVAL);
        int maxvalue = data.getIntExtra(Constant.MAXVALUE_PARA, Constant.BASED_VALUE);
        switch (requestCode) {
            case Constant.RESULT_ADD_TAG:
                if (resultCode == RESULT_OK) {

                    if (!isagenda) {
                        Backlog backlog = new Backlog(scheme.getDate(), name, start, end, des, value, isdone);
                        addNewTarget(backlog);
                    } else {
                        Agenda agenda = new Agenda(scheme.getDate(), name, start, end, des, value, interval, maxvalue, isdone);
                        addNewTarget(agenda);
                    }
                    fetchAll();
                }
                break;
            case Constant.RESULT_MOD_TAG:
                if (resultCode == RESULT_OK) {
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

    /**
     * 显示当前计划组的而完成情况
     */
    private void showSchemeDetail() {
        Toast.makeText(MainActivity.this, scheme.toLongString(), Toast.LENGTH_LONG).show();
    }

    /**
     * 从数据库里再取一次数据放在主页，目前相当于刷新
     * @return
     */
    private boolean fetchAll() {
//        Toast.makeText(MainActivity.this, "fetching...", Toast.LENGTH_SHORT).show();
        refreshTargets();
        return false;
    }

    /**
     * 发起添加计划
     */
    private void onAddTarget() {
        LogUtils.d(Constant.SERVICE_TAG, "into onAddTarget()");
        TargetActivity.actionStart(MainActivity.this, null, null, null, null, 0, false, false, 0, 0,
                Constant.RESULT_ADD_TAG);

    }

    /**
     * 删除某计划并刷新主页
     * @param target 待删除的计划
     */
    private void deleteTarget(Target target) {
//        Toast.makeText(MainActivity.this, "deleting...", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DataDeleter.deleteTarget(db, target);
        scheme.getTargets().remove(target);
        scheme.check();
        db.close();
        fetchAll();
    }

    /**
     * 保存所有，目前是保存当前页面的计划组
     * @return
     */
    private boolean saveAll() {
//        Toast.makeText(MainActivity.this, "saving...", Toast.LENGTH_SHORT).show();
        saveTodayTargets();
        return false;
    }

    /**
     * 弹出删除的对话框
     * @param target 待删除的计划
     */
    private void deleteDialog(final Target target) {
        AlertDialog.Builder builder = new Builder(MainActivity.this);
        builder.setMessage("确认删除" + target.getName() + "吗？");
        builder.setTitle("删除");
        builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteTarget(target);
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 弹出 列表的点击后 选择操作的对话框
     * @param id 列表序号
     * @return 对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
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
                        TargetActivity.actionStart(MainActivity.this, agenda.getName(), agenda.getDescription(),
                                Tools.formatTime(agenda.getTime()), Tools.formatTime(agenda.getEndTime()),
                                agenda.getValue(), agenda.isDone(), true, agenda.getInterval(), agenda.getMaxValue(),
                                Constant.RESULT_MOD_TAG);
                    } else {
                        TargetActivity.actionStart(MainActivity.this, target.getName(), target.getDescription(),
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

    /**
     * 将所有数据保存成文件
     * @param object 传进来的数据
     */
    public void saveFile(Serializable object) {
        FileOutputStream out = null;
        ObjectOutputStream oos = null;
        ArrayList<Scheme> arrayList = (ArrayList<Scheme>)object;
        LogUtils.e("savefile",""+arrayList.size());
        try {
            // sdcard时会报分隔符错误
//            out = openFileOutput(path, Context.MODE_PRIVATE);
            out = new FileOutputStream(path);
            oos = new ObjectOutputStream(out);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 将所有数据保存成文件
     * @param fileDir 文件路径
     */
    public Object loadFile(String fileDir) {
        FileInputStream in = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            // sdcard时会报分隔符错误
//            in = openFileInput(fileDir);
            in = new FileInputStream(fileDir);
            ois = new ObjectInputStream(in);
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
