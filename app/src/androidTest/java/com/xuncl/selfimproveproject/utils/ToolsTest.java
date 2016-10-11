package com.xuncl.selfimproveproject.utils;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by CLEVO on 2016/10/11.
 */
public class ToolsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testNextDay() throws Exception {
        Date now = new Date();
        Date nextNow = Tools.nextDay(now);
        assertEquals(60*1000*60*24, nextNow.getTime()-now.getTime());
    }

    public void testPrevDay() throws Exception {

    }

    public void testIsEmpty() throws Exception {

    }

    public void testCheckTime() throws Exception {

    }

    public void testFormatTime() throws Exception {

    }

    public void testParseTimeByDate() throws Exception {

    }

    public void testParseTimeByDate1() throws Exception {

    }

    public void testGetTargetByScheme() throws Exception {

    }

    public void testDaysBetween() throws Exception {

    }
}