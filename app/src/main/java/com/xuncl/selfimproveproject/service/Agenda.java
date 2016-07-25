package com.xuncl.selfimproveproject.service;

import java.util.Date;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.utils.Tools;

import android.annotation.SuppressLint;

public class Agenda extends Target
{

    private static final long serialVersionUID = -8290120837139948531L;

    private int interval;
    private int maxValue;

    public Agenda()
    {
        this.setName(Constant.UNNAMED_AGENDA);
        this.setIsdone(false);
        this.setValue(Constant.BASED_VALUE);
        this.setTime(new Date());
        this.setIconId(Constant.DEFAULT_ICONID);
        this.setEndTime(this.getTime());
        this.setDescription("");
        this.setInterval(1);
        this.setMaxValue(5);
    }

    public Agenda(Agenda agenda)
    {
        this.setName(agenda.getName());
        this.setIsdone(false);
        if (agenda.isDone())
        {
            if ((agenda.getValue() + agenda.getInterval()) > agenda.getMaxValue())
            {
                this.setValue(agenda.getMaxValue());
            }
            else
            {
                this.setValue(agenda.getValue() + agenda.getInterval());
            }
        }
        else
        {
            this.setValue(Constant.BASED_VALUE);
        }
        this.setDescription(agenda.getDescription());
        this.setTime(Tools.nextDay(agenda.getTime()));
        this.setEndTime(Tools.nextDay(agenda.getEndTime()));
        this.setIconId(agenda.getIconId());
        this.setInterval(agenda.getInterval());
        this.setMaxValue(agenda.getMaxValue());
    }

    @SuppressLint("SimpleDateFormat")
    public Agenda(Date date, String name, String startTime, String endTime, String description, int mValue,
            int mInterval, int mMaxValue, boolean isDone)
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
        this.setInterval(mInterval);
        this.setMaxValue(mMaxValue);
        this.setIsdone(isDone);
    }
    
    @SuppressLint("SimpleDateFormat")
    public Agenda(String name, String mdate, String startTime, String endTime, String description, int mValue,
            int mInterval, int mMaxValue, boolean isDone)
    {
        if (Tools.isEmpty(name))
            name = Constant.UNNAMED_BACKLOG;
        if (Tools.isEmpty(mdate))
            mdate = Constant.DEFAULT_DATE;
        if (Tools.isEmpty(startTime))
            startTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(endTime))
            endTime = Constant.DEFAULT_TIME;
        if (Tools.isEmpty(description))
            endTime = Constant.DEFAULT_DESCRIPTION;
        this.setName(name);
        Date time = Tools.parseTimeByDate(mdate, startTime);
        Date endtime = Tools.parseTimeByDate(mdate, endTime);
        this.setTime(time);
        this.setEndTime(endtime);
        this.setIconId(Constant.DEFAULT_ICONID);
        this.setDescription(description);
        this.setValue(mValue);
        this.setInterval(mInterval);
        this.setMaxValue(mMaxValue);
        this.setIsdone(isDone);
    }

    public int getInterval()
    {
        return interval;
    }

    public void setInterval(int interval)
    {
        this.interval = interval;
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

}
