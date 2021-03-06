package com.xuncl.selfimproveproject;

public final class Constant
{
    // 默认的空计划数据
    public static final String UNNAMED_AGENDA = "未名日常事项";
    public static final String UNNAMED_BACKLOG = "未名待办事项";
    public static final String DEFAULT_DATE = "2015/10/26";
    public static final String DEFAULT_TIME = "23:30";
    public static final String DEFAULT_TIME_AFTER = "23:35";
    public static final String DEFAULT_DESCRIPTION = "";
    
    public static final int BASED_VALUE = 2;
    public static final int BASED_INTERVAL = 0;
    
    public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd";
    public static final String TIME_FORMAT_PATTERN = "HH:mm";
    public static final String THE_VERY_FIRST_DAY = "2016/07/01";
    // 目前是模拟器测试参数，在现实环境里注意修改这个参数
//    public static final String THE_VERY_FIRST_DAY = "2015/10/08";

    
    public static final String BASE_ACTIVITY_TAG = "BaseActivity";
    public static final String DB_TAG = "DB_TAG";
    public static final String SERVICE_TAG = "SERVICE_TAG";
    
    public static final String TABLE_NAME = "Target";
    public static final String DB_NAME = "MyTarget.db";
    
    public static final String COL_RESOURCEID = "resourceid";
    public static final String COL_NAME = "name";
    public static final String COL_MDATE = "mdate";
    public static final String COL_STARTTIME = "starttime";
    public static final String COL_ENDTIME = "endtime";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_MVALUE = "mvalue";
    public static final String COL_ISAGENDA = "isagenda";
    public static final String COL_MINTERVAL = "minterval";
    public static final String COL_MMAXVALUE = "mmaxvalue";
    public static final String COL_TODAYVALUE = "todayvalue";
    public static final String COL_YESTERDAYVALUE = "yesterdayvalue";
    public static final String COL_ISDONE = "isdone";
    
    public static final String NAME_PARA = "name";
    public static final String DESCRIPTION_PARA = "description";
    public static final String START_PARA = "start";
    public static final String END_PARA = "end";
    public static final String VALUE_PARA = "value";
    public static final String ISDONE_PARA = "isdone";
    public static final String ISAGENDA_PARA = "isagenda";
    public static final String INTERVAL_PARA = "interval";
    public static final String MAXVALUE_PARA = "maxvalue";
    
    public static final String DIALOG_MODIFY = "修改";
    public static final String DIALOG_DELETE = "删除";
    
    public static final int RESULT_ADD_TAG = 1;
    public static final int RESULT_MOD_TAG = 2;
    
    public static final int DEFAULT_ICONID = R.drawable.target_icon;

    public static final String UMENG_APPKEY = "5790e94e67e58e1d6900121b";
    public static final String UMENG_CHANNELID = "Manual";

    public static final String BASE_URL = "http://www.kuaimei56.com";

    public static final int SCHEME_PREV_DAY_MAX = 21;

    public static final String UPLOAD_FILE_NAME = "upload.txt";
    public static final String DOWNLOAD_FILE_NAME = "download.txt";
    public static final String ALARM_FILE_NAME = "alarm.txt";
    public static final String SERVICE_LIVE_TXT = "service_live.txt";

}
