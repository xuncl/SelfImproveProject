package com.xuncl.selfimproveproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.SuppressLint;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.Scheme;
import com.xuncl.selfimproveproject.service.Target;

public class Tools
{
    public static Date nextDay(Date theDay)
    {
        return new Date(theDay.getTime() + 1000 * 60 * 60 * 24);
    }

    public static Date prevDay(Date theDay)
    {
        return new Date(theDay.getTime() - 1000 * 60 * 60 * 24);
    }

    public static boolean isEmpty(String str)
    {
        boolean res = false;
        if ((null == str) || (str.trim().length() == 0))
            res = true;
        return res;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean checkTime(String time)
    {
        boolean res = true;
        if (isEmpty(time))
            return true; // can be empty
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN);
        try
        {
            sdf.parse(time);
        }
        catch (ParseException e)
        {
            res = false;
        }
        return res;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(Date time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN);
        return sdf.format(time);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parseTimeByDate(Date date, String time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN + Constant.TIME_FORMAT_PATTERN);
        String mdate = sdf.format(date);
        Date stime = new Date();
        try
        {
            stime = sdf2.parse(mdate + time);
        }
        catch (ParseException e)
        {
        }
        return stime;
    }
    
    @SuppressLint("SimpleDateFormat")
    public static Date parseTimeByDate(String date, String time)
    {
        SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN + Constant.TIME_FORMAT_PATTERN);
        Date stime = new Date();
        try
        {
            stime = sdf2.parse(date + time);
        }
        catch (ParseException e)
        {
        }
        return stime;
    }

    public static Target getTargetByScheme(Scheme scheme, String name)
    {
        if ((null != scheme) && (null != name))
        {
            for (Target target : scheme.getTargets())
            {
                if (name.equals(target.getName()))
                {
                    return target;
                }
            }
        }
        return null;
    }


}
