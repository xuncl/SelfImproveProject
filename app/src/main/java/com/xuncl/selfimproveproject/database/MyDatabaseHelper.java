package com.xuncl.selfimproveproject.database;

import com.xuncl.selfimproveproject.Constant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper
{

    public static final String CREAT_TARGET = "create table " + Constant.TABLE_NAME + " (" + Constant.COL_RESOURCEID
            + " integer primary key autoincrement, " + Constant.COL_NAME + " text, " + Constant.COL_MDATE + " text, "
            + Constant.COL_STARTTIME + " text, " + Constant.COL_ENDTIME + " text, " + Constant.COL_DESCRIPTION
            + " text, " + Constant.COL_MVALUE + " integer, " + Constant.COL_ISAGENDA + " integer, "
            + Constant.COL_MINTERVAL + " integer, " + Constant.COL_MMAXVALUE + " integer, " + Constant.COL_TODAYVALUE
            + " integer, " + Constant.COL_YESTERDAYVALUE + " integer, " + Constant.COL_ISDONE + " integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREAT_TARGET);
        Toast.makeText(mContext, "create succeeded", Toast.LENGTH_SHORT).show();
        FirstRunDataInput frdi = new FirstRunDataInput(db);
        frdi.insertYesterday();
        Toast.makeText(mContext, "insert succeeded", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // switch (oldVersion)
        // {
        // case 1:
        // db.execSQL("alter table Target add column m_type integer");
        // default:
        // }
    }

}
