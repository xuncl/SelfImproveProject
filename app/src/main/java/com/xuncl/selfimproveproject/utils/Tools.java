package com.xuncl.selfimproveproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

    /**

     * 计算两个日期之间相差的天数
     * @param date1 靠前的日期
     * @param date2 靠后的日期
     * @return 两个日期相隔的天数差
     */
    public static int daysBetween(Date date1,Date date2)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 初始化today变量，使其为今天的23点59分，用于右划边界的判断
     */
    public static Date setEndofDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN, Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN + " HH:mm", Locale.CHINA);
        String timeStr = sdf.format(date);
        timeStr = timeStr + " 23:59";
        try {
            date = sdf2.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
