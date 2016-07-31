package com.xuncl.selfimproveproject.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.annotation.SuppressLint;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.interfaces.Evalueable;
import com.xuncl.selfimproveproject.utils.Tools;

public class Scheme implements Evalueable, Serializable
{

    Date date = new Date();
    private static final long serialVersionUID = 4978167984550096421L;
    boolean isAllDone = false;
    boolean checked = false;
    int sumValue = 0;
    ArrayList<Target> targets = new ArrayList<Target>();
    private int yesterdayValue = 0;
    private int todayValue = 0;
    boolean isVerified = false;


    public Scheme()
    {
    }

    public Scheme(Scheme yesterdayScheme)
    {
        yesterdayScheme.check();
        this.setDate(Tools.nextDay(yesterdayScheme.getDate()));
        this.setYesterdayValue(yesterdayScheme.getTodayValue());
        this.setTodayValue(yesterdayScheme.getTodayValue());
        for (Target target : yesterdayScheme.getTargets())
        {
            if (target instanceof Agenda)
            {
                // get next day agenda
                Agenda agenda = new Agenda((Agenda) target);
                this.targets.add(agenda);
            }
        }
    }

    public Scheme(Date date2, int todayValue2, int yesterdayValue2, ArrayList<Target> targets2)
    {
        this.setDate(date2);
        this.setTodayValue(todayValue2);
        this.setYesterdayValue(yesterdayValue2);
        this.setTargets(targets2);
    }

    @Override
    public int getValue()
    {
        sumValues();
        return sumValue;
    }

    private void sumValues()
    {
        sumValue = 0;
        for (Target target : targets)
        {
            sumValue += target.getValue();
        }
    }

    //一条大河波浪宽，风吹稻花香两岸
    public boolean check()
    {
        isAllDone = true;
        todayValue = yesterdayValue;
        for (Target target : targets)
        {
            if (target.isDone())
            {
                todayValue += target.getValue();
            }
            else
            {
                todayValue -= target.getValue();
            }
            isAllDone = isAllDone && target.isDone();
        }
        checked = true;
        return isAllDone;
    }

    public int getCurrentValue()
    {
        if (!checked)
            check();
        return todayValue - yesterdayValue;
    }

    public boolean isAllDone()
    {
        return isAllDone;
    }

    public ArrayList<Target> getTargets()
    {
        return targets;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getYesterdayValue()
    {
        return yesterdayValue;
    }

    public void setYesterdayValue(int yesterdayValue)
    {
        this.yesterdayValue = yesterdayValue;
    }

    public int getTodayValue()
    {
        if (!checked)
            this.check();
        return todayValue;
    }

    public void setTodayValue(int todayValue)
    {
        this.todayValue = todayValue;
    }

    public void setTargets(ArrayList<Target> targets)
    {
        this.targets = targets;
    }

    public boolean isVerified()
    {
        return isVerified;
    }

    public void setVerified(boolean isVerified)
    {
        this.isVerified = isVerified;
    }

    @SuppressLint("SimpleDateFormat")
    public String toLongString()
    {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN);
        String timeStr = sdf.format(getDate());
        int count = 0;
        int doneValue = 0;
        for (Target target : targets)
        {
            if (target.isDone())
            {
                count++;
                doneValue += target.getValue();
            }
        }
        sb.append("Scheme: ");
        sb.append(timeStr);
        sb.append(" total:");
        sb.append(todayValue);
        sb.append(" delta:");
        sb.append(todayValue - yesterdayValue);
        sb.append(" targets:");
        sb.append(targets.size());
        sb.append(" done:");
        sb.append(count);
        sb.append(" percentage:");
        sb.append(1.00 * doneValue / getValue());
        return sb.toString();
    }

    @SuppressLint("SimpleDateFormat")
    public String toShortString()
    {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN);
        String timeStr = sdf.format(getDate());
        sb.append(timeStr);
        sb.append(" today:");
        sb.append(todayValue);
        sb.append(" yesterday:");
        sb.append(yesterdayValue);
        sb.append(" delta:");
        sb.append(todayValue - yesterdayValue);
        return sb.toString();
    }

}
