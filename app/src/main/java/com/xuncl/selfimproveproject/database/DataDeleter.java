package com.xuncl.selfimproveproject.database;

import java.text.SimpleDateFormat;


import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.Target;

public class DataDeleter
{
    @SuppressLint("SimpleDateFormat")
    public static void deleteTarget(SQLiteDatabase db, Target target)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN);
        String[] whereArgs =
        {
                target.getName(), dateFormat.format(target.getTime())
        };
        db.delete(Constant.TABLE_NAME, Constant.COL_NAME + " = ? and " + Constant.COL_MDATE + " = ?", whereArgs);
    }

}
