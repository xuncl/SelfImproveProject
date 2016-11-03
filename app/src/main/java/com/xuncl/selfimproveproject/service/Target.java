package com.xuncl.selfimproveproject.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.service.interfaces.Checkable;
import com.xuncl.selfimproveproject.service.interfaces.Evalueable;

public abstract class Target implements Checkable, Evalueable, Serializable
{




    private static final long serialVersionUID = 3738319298768505925L;
    
    private int value = 0;
    
    private Date time;
    
    private String name = "";
    
    private boolean isDone = false;
    
    private int iconId;
    
    private Date endTime;
    
    private String description;
    
    
    @Override
    public void check()
    {
        if (isDone){
            setUndone();
        }else{
            setDone();
        }
    }

    public int getRealValue(){
        if (isDone){
            return value;
        }else{
            return -value;
        }
    }

    @Override
    public void setDone()
    {
        setIsdone(true);
    }


    @Override
    public void setUndone()
    {
        setIsdone(false);
    }

    @Override
    public boolean isDone()
    {
        return isIsdone();
    }
    
    @Override
    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public boolean isIsdone()
    {
        return isDone;
    }

    public void setIsdone(boolean isDone)
    {
        this.isDone = isDone;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Date getTime()
    {
        return time;
    }


    public void setTime(Date time)
    {
        this.time = time;
    }


    public int getIconId()
    {
        return iconId;
    }


    public void setIconId(int iconId)
    {
        this.iconId = iconId;
    }
    

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN);
        String timeStr = sdf.format(time);
        sb.append(timeStr);
        sb.append(" ");
        sb.append(name);
        sb.append(" ");
        sb.append(value);
        return sb.toString();
    }
    
    @SuppressLint("SimpleDateFormat")
    public String toLongString()
    {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN);
        String timeStr = sdf.format(time);
        sb.append(timeStr);
        sb.append(" ");
        sb.append(name);
        sb.append(" ");
        sb.append(value);
        sb.append(" ");
        sb.append(isDone);
        sb.append(" \n");
        sb.append(description);
        return sb.toString();
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
