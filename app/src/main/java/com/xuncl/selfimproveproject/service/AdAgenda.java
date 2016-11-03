package com.xuncl.selfimproveproject.service;

import java.util.Date;

/**
 * Created by CLEVO on 2016/10/31.
 */
public class AdAgenda extends Agenda {

    private boolean ub = false;
    private boolean ob = false;

    public AdAgenda(){
        super();
    }

    public AdAgenda(Agenda agenda){
        super(agenda);
    }

    public AdAgenda(Date date, String name, String startTime, String endTime, String description, int mValue,
                    int mInterval, int mMaxValue, boolean isDone){
        super(date, name, startTime, endTime, description, mValue, mInterval, mMaxValue, isDone);
    }

    public void setUb(boolean ub) {
        this.ub = ub;
    }

    public void setOb(boolean ob) {
        this.ob = ob;
    }

    @Override
    public int getRealValue() {
        if (ub&&isDone()) return 0;
        if (ob&&(!isDone())) return 0;
        return super.getRealValue();
    }

}
