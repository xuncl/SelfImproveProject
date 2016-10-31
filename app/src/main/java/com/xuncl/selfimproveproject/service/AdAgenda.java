package com.xuncl.selfimproveproject.service;

/**
 * Created by CLEVO on 2016/10/31.
 */
public class AdAgenda extends Agenda {

    private boolean ub = false;
    private boolean ob = false;

    public void setUb(boolean ub) {
        this.ub = ub;
    }

    public void setOb(boolean ob) {
        this.ob = ob;
    }

    @Override
    public int getValue() {
        if (ub&&isDone()) return 0;
        if (ob&&(!isDone())) return 0;
        return super.getValue();
    }

}
