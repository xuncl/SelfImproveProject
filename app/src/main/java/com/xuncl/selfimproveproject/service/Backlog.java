package com.xuncl.selfimproveproject.service;

import java.util.Date;


import android.annotation.SuppressLint;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.utils.Tools;

public class Backlog extends Target
{

    private static final long serialVersionUID = 5527808887821484283L;

    public Backlog()
    {
        this.setName(Constant.UNNAMED_BACKLOG);
        this.setIsdone(false);
        this.setValue(Constant.BASED_VALUE);
        this.setTime(new Date());
        this.setEndTime(getTime());
        this.setDescription("");
        this.setIconId(Constant.DEFAULT_ICONID);
    }

    /**
     * make today's new backlog
     * 
     * @param name
     * @param startTime
     * @param endTime
     * @param description
     * @param mValue
     * @param isDone
     */
    @SuppressLint("SimpleDateFormat")
    public Backlog(Date date, String name, String startTime, String endTime, String description, int mValue,
            boolean isDone)
    {
        if (Tools.isEmpty(name))
            name = Constant.UNNAMED_BACKLOG;
        if (Tools.isEmpty(startTime))
            startTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(endTime))
            endTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(description))
            endTime = Constant.DEFAULT_DESCRIPTION;
        this.setName(name);
        Date time = Tools.parseTimeByDate(date, startTime);
        Date endtime = Tools.parseTimeByDate(date, endTime);
        this.setTime(time);
        this.setEndTime(endtime);
        this.setIconId(Constant.DEFAULT_ICONID);
        this.setDescription(description);
        this.setValue(mValue);
        this.setIsdone(isDone);
    }

    @SuppressLint("SimpleDateFormat")
    public Backlog(String name, String mDate, String startTime, String endTime, String description, int mValue,
            boolean isDone)
    {
        if (Tools.isEmpty(name))
            name = Constant.UNNAMED_BACKLOG;
        if (Tools.isEmpty(mDate))
            mDate = Constant.DEFAULT_DATE;
        if (Tools.isEmpty(startTime))
            startTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(endTime))
            endTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(description))
            endTime = Constant.DEFAULT_DESCRIPTION;
        this.setName(name);
        Date time = Tools.parseTimeByDate(mDate, startTime);
        Date endtime = Tools.parseTimeByDate(mDate, endTime);
        this.setTime(time);
        this.setEndTime(endtime);
        this.setIconId(Constant.DEFAULT_ICONID);
        this.setDescription(description);
        this.setValue(mValue);
        this.setIsdone(isDone);
    }

    /**
     * will not be used
     * 
     * @param backlog
     */
    public Backlog(Backlog backlog)
    {
        this.setName(backlog.getName());
        this.setIsdone(false);
        this.setValue(backlog.getValue());
        this.setDescription(backlog.getDescription());
        this.setTime(Tools.nextDay(backlog.getTime()));
        this.setEndTime(Tools.nextDay(backlog.getEndTime()));
        this.setIconId(backlog.getIconId());
    }

}
