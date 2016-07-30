package com.xuncl.selfimproveproject.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.Agenda;
import com.xuncl.selfimproveproject.service.Backlog;
import com.xuncl.selfimproveproject.service.Scheme;
import com.xuncl.selfimproveproject.service.Target;
import com.xuncl.selfimproveproject.utils.LogUtils;
import com.xuncl.selfimproveproject.utils.Tools;

public class DataFetcher
{
    @SuppressLint("SimpleDateFormat")
     public static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);

    private static int stackCount = Constant.SCHEME_PREV_DAY_MAX;
    /**
     * Get the day's scheme. If there is none, build a new scheme by the day
     * before. If the day before is still none, return null.
     * 
     * @param db
     * @param date
     * @return
     */
    public static Scheme fetchScheme(SQLiteDatabase db, Date date)
    {
        Scheme scheme;
        scheme = fetchOnceByDate(db, sdf.format(date));
//        if (null == scheme)
//        {
//            Scheme yesterdayScheme = fetchOnceByDate(db, sdf.format(Tools.prevDay(date)));
//            if (null != yesterdayScheme)
//            {
//                scheme = new Scheme(yesterdayScheme);
//                DataUpdater.insertScheme(db, scheme);
//            }
//            else
//            {
//                scheme = new Scheme();
//            }
//        }

        if (null == scheme)
        {
            scheme = new Scheme (getPreviousByScheme(db, Tools.prevDay(date)));
            stackCount = Constant.SCHEME_PREV_DAY_MAX;
            DataUpdater.insertScheme(db, scheme);
        }
        return scheme;
    }

    /**
     * 获取前一日期的scheme，如果没有，会继续向前迭代。
     * @param db
     * @param prevDay
     * @return
     */
    private static Scheme getPreviousByScheme(SQLiteDatabase db,Date prevDay)
    {
        stackCount--;
        if (stackCount<=0){
            return new Scheme();
        }
        LogUtils.e("scheme", "stackCount now-------："+stackCount+" - "+sdf.format(prevDay));

        Scheme scheme = fetchOnceByDate(db, sdf.format(prevDay));
        if (scheme==null) {
            scheme = new Scheme(getPreviousByScheme(db, Tools.prevDay(prevDay)));
            DataUpdater.insertScheme(db, scheme);
        }
        return scheme;
        
    }

    public static ArrayList<Scheme> getAllScheme(SQLiteDatabase db, Date today){
        LogUtils.e("scheme", "Start get All!");

        ArrayList<Scheme> allScheme = new ArrayList<>();
        Date thisDay = today;
        Date firstDay;
        try {
            firstDay = sdf.parse(Constant.THE_VERY_FIRST_DAY);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        while (thisDay.after(firstDay)){
            allScheme.add(fetchScheme(db,thisDay));
            thisDay = Tools.prevDay(thisDay);
        }
        LogUtils.e("scheme", "End get All!");

        return allScheme;
    }

    private static Scheme fetchOnceByDate(SQLiteDatabase db, String date)
    {
        String[] columns = null;
        String selection = Constant.COL_MDATE + "=?";
        String[] selectionArgs =
        {
                String.valueOf(date)
        };
        String orderBy = Constant.COL_STARTTIME + " ASC";

        Cursor cursor = db.query(Constant.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst())
        {
            Scheme scheme = buildSchemeByCursor(cursor);
            LogUtils.e("scheme", "Is return cursor Null? - "+scheme+" - "+date);
            return scheme;
        }
        else
        {
            LogUtils.e("scheme", "Actually return Null? "+date);

            return null;
        }
    }

    private static Scheme buildSchemeByCursor(Cursor cursor)
    {
        int todayValue = 0;
        int yesterdayValue = 0;
        Date date = new Date();
        ArrayList<Target> targets = new ArrayList<Target>();
        do
        {
            String name = cursor.getString(cursor.getColumnIndex(Constant.COL_NAME));
            String mDate = cursor.getString(cursor.getColumnIndex(Constant.COL_MDATE));
            String startTime = cursor.getString(cursor.getColumnIndex(Constant.COL_STARTTIME));
            String endTime = cursor.getString(cursor.getColumnIndex(Constant.COL_ENDTIME));
            String description = cursor.getString(cursor.getColumnIndex(Constant.COL_DESCRIPTION));
            int mValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_MVALUE));
            boolean isAgenda = (cursor.getInt(cursor.getColumnIndex(Constant.COL_ISAGENDA)) > 0);
            int mInterval = cursor.getInt(cursor.getColumnIndex(Constant.COL_MINTERVAL));
            int mMaxValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_MMAXVALUE));
            todayValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_TODAYVALUE));
            yesterdayValue = cursor.getInt(cursor.getColumnIndex(Constant.COL_YESTERDAYVALUE));
            boolean isDone = (cursor.getInt(cursor.getColumnIndex(Constant.COL_ISDONE)) > 0);
            if (isAgenda)
            {
                Agenda agenda = new Agenda(name, mDate, startTime, endTime, description, mValue, mInterval, mMaxValue,
                        isDone);
                targets.add(agenda);
                date = agenda.getTime();
            }
            else
            {
                Backlog backlog = new Backlog(name, mDate, startTime, endTime, description, mValue, isDone);
                targets.add(backlog);
                date = backlog.getTime();
            }
        }
        while (cursor.moveToNext());

        Scheme scheme = new Scheme(date, todayValue, yesterdayValue, targets);

        // May check yesterday's scheme
        scheme.check();

        return scheme;
    }

}
