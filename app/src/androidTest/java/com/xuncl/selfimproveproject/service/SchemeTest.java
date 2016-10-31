package com.xuncl.selfimproveproject.service;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by CLEVO on 2016/10/28.
 */
public class SchemeTest extends TestCase {

    private Scheme myScheme;

    public void setUp() throws Exception {
        super.setUp();
        Agenda agenda1 = new Agenda(new Date(), "test1", "08:00", "09:00", "description", 3, 1, 5, true);
        Agenda agenda2 = new Agenda(new Date(), "test2", "08:00", "09:00", "description", 4, 1, 5, false);
        Agenda agenda3 = new Agenda(new Date(), "test3", "08:00", "09:00", "description", 9, 2, 12, true);
        ArrayList<Target> al = new ArrayList<Target>();
        al.add(agenda1);
        al.add(agenda2);
        al.add(agenda3);
        myScheme = new Scheme(new Date(), 3000, 2000, al);
    }

    public void testGetValue() throws Exception {
        assertEquals(16, myScheme.getValue());
    }

    public void testCheck() throws Exception {
        assertEquals(false, myScheme.check());
        assertEquals(2000+3+9-4, myScheme.getTodayValue());
    }

    public void testGetCurrentValue() throws Exception {
        assertEquals(3+9-4, myScheme.getCurrentValue());
    }

    public void testGetTodayValue() throws Exception {
        assertEquals(2000+3+9-4, myScheme.getTodayValue());
    }
}