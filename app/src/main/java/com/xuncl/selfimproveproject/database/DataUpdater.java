package com.xuncl.selfimproveproject.database;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.Agenda;
import com.xuncl.selfimproveproject.service.Scheme;
import com.xuncl.selfimproveproject.service.Target;
import com.xuncl.selfimproveproject.utils.LogUtils;

public class DataUpdater
{
    @SuppressLint("SimpleDateFormat")
    public static boolean updateScheme(SQLiteDatabase db, Scheme scheme)
    {
        boolean saved = false;
        scheme.check();
        List<Target> targets = scheme.getTargets();
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constant.TIME_FOMMAT_PATTERN);

        String whereClause = " " + Constant.COL_NAME + " = ? and " + Constant.COL_MDATE + " = ? ";
        db.beginTransaction();
        for (Target target : targets)
        {
            saved = true;
            values.put(Constant.COL_STARTTIME, timeFormat.format(target.getTime()));
            values.put(Constant.COL_ENDTIME, timeFormat.format(target.getEndTime()));
            values.put(Constant.COL_DESCRIPTION, target.getDescription());
            values.put(Constant.COL_MVALUE, target.getValue());
            if (target instanceof Agenda)
            {
                Agenda agenda = (Agenda) target;
                values.put(Constant.COL_ISAGENDA, 1);
                values.put(Constant.COL_MINTERVAL, agenda.getInterval());
                values.put(Constant.COL_MMAXVALUE, agenda.getMaxValue());
            }
            else
            {
                values.put(Constant.COL_ISAGENDA, 0);
                values.put(Constant.COL_MINTERVAL, 0);
                values.put(Constant.COL_MMAXVALUE, target.getValue());
            }
            values.put(Constant.COL_TODAYVALUE, scheme.getTodayValue());
            values.put(Constant.COL_YESTERDAYVALUE, scheme.getYesterdayValue());
            values.put(Constant.COL_ISDONE, target.isDone() ? 1 : 0);
            String[] whereArgs =
            {
                    target.getName(), dateFormat.format(target.getTime())
            };
            db.update(Constant.TABLE_NAME, values, whereClause, whereArgs);
            values.clear();
            LogUtils.d(Constant.DB_TAG, "update: " + target.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        
        return saved;
    }

    /**
     * 
     * @param db
     * @param scheme
     * @return if scheme is empty return false.
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean insertScheme(SQLiteDatabase db, Scheme scheme)
    {
        boolean saved = false;
        scheme.check();
        List<Target> targets = scheme.getTargets();
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constant.TIME_FOMMAT_PATTERN);
        db.beginTransaction();
        for (Target target : targets)
        {
            saved = true;
            values.put(Constant.COL_NAME, target.getName());
            values.put(Constant.COL_MDATE, dateFormat.format(target.getTime()));
            values.put(Constant.COL_STARTTIME, timeFormat.format(target.getTime()));
            values.put(Constant.COL_ENDTIME, timeFormat.format(target.getEndTime()));
            values.put(Constant.COL_DESCRIPTION, target.getDescription());
            values.put(Constant.COL_MVALUE, target.getValue());
            if (target instanceof Agenda)
            {
                Agenda agenda = (Agenda) target;
                values.put(Constant.COL_ISAGENDA, 1);
                values.put(Constant.COL_MINTERVAL, agenda.getInterval());
                values.put(Constant.COL_MMAXVALUE, agenda.getMaxValue());
            }
            else
            {
                values.put(Constant.COL_ISAGENDA, 0);
                values.put(Constant.COL_MINTERVAL, 0);
                values.put(Constant.COL_MMAXVALUE, target.getValue());
            }
            values.put(Constant.COL_TODAYVALUE, scheme.getTodayValue());
            values.put(Constant.COL_YESTERDAYVALUE, scheme.getYesterdayValue());
            values.put(Constant.COL_ISDONE, target.isDone() ? 1 : 0);
            db.insert(Constant.TABLE_NAME, null, values);
            values.clear();
            LogUtils.d(Constant.DB_TAG, "insert: " + target.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return saved;
    }

    /**
     * 
     * @param db
     * @param scheme
     * @return if scheme is empty return false.
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean insertTarget(SQLiteDatabase db, Scheme scheme, Target target)
    {
        boolean saved = false;
        scheme.check();
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FOMMAT_PATTERN);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constant.TIME_FOMMAT_PATTERN);

        saved = true;
        values.put(Constant.COL_NAME, target.getName());
        values.put(Constant.COL_MDATE, dateFormat.format(target.getTime()));
        values.put(Constant.COL_STARTTIME, timeFormat.format(target.getTime()));
        values.put(Constant.COL_ENDTIME, timeFormat.format(target.getEndTime()));
        values.put(Constant.COL_DESCRIPTION, target.getDescription());
        values.put(Constant.COL_MVALUE, target.getValue());
        if (target instanceof Agenda)
        {
            Agenda agenda = (Agenda) target;
            values.put(Constant.COL_ISAGENDA, 1);
            values.put(Constant.COL_MINTERVAL, agenda.getInterval());
            values.put(Constant.COL_MMAXVALUE, agenda.getMaxValue());
        }
        else
        {
            values.put(Constant.COL_ISAGENDA, 0);
            values.put(Constant.COL_MINTERVAL, 0);
            values.put(Constant.COL_MMAXVALUE, target.getValue());
        }
        values.put(Constant.COL_TODAYVALUE, scheme.getTodayValue());
        values.put(Constant.COL_YESTERDAYVALUE, scheme.getYesterdayValue());
        values.put(Constant.COL_ISDONE, target.isDone() ? 1 : 0);
        db.beginTransaction();
        db.insert(Constant.TABLE_NAME, null, values);
        values.clear();
        db.setTransactionSuccessful();
        db.endTransaction();
        LogUtils.d(Constant.DB_TAG, "insert: " + target.toString());
        return saved;
    }
    
//    @SuppressLint("SimpleDateFormat")
//    public static boolean updateTarget(SQLiteDatabase db, String name, String date, String des, String startTime, String endTime, int value, int isdone)
//    {
//        return false;
//    }
}
