package com.xuncl.selfimproveproject.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.MyApplication;
import com.xuncl.selfimproveproject.database.DataFetcher;
import com.xuncl.selfimproveproject.database.DataUpdater;
import com.xuncl.selfimproveproject.service.Agenda;
import com.xuncl.selfimproveproject.service.Backlog;
import com.xuncl.selfimproveproject.service.Scheme;
import com.xuncl.selfimproveproject.service.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by CLEVO on 2016/7/25.
 */
public class HttpUtils {


    private static final String TAG = "HTTP_UTILS";


    public static void postSchemeJson(Scheme scheme) {
        String url = "/Raw/targets";
        JsonObjectRequest jsonObjectRequest;
        ArrayList<Target> targets = scheme.getTargets();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT_PATTERN, Locale.CHINA);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constant.TIME_FORMAT_PATTERN, Locale.CHINA);
        for (Target target : targets) {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(Constant.COL_NAME, target.getName());
                jsonObject.put(Constant.COL_MDATE, dateFormat.format(target.getTime()));
                jsonObject.put(Constant.COL_STARTTIME, timeFormat.format(target.getTime()));
                jsonObject.put(Constant.COL_ENDTIME, timeFormat.format(target.getEndTime()));
                jsonObject.put(Constant.COL_DESCRIPTION, target.getDescription());
                jsonObject.put(Constant.COL_MVALUE, target.getValue());
                if (target instanceof Agenda) {
                    Agenda agenda = (Agenda) target;
                    jsonObject.put(Constant.COL_ISAGENDA, 1);
                    jsonObject.put(Constant.COL_MINTERVAL, agenda.getInterval());
                    jsonObject.put(Constant.COL_MMAXVALUE, agenda.getMaxValue());
                } else {
                    jsonObject.put(Constant.COL_ISAGENDA, 0);
                    jsonObject.put(Constant.COL_MINTERVAL, 0);
                    jsonObject.put(Constant.COL_MMAXVALUE, target.getValue());
                }
                jsonObject.put(Constant.COL_TODAYVALUE, scheme.getTodayValue());
                jsonObject.put(Constant.COL_YESTERDAYVALUE, scheme.getYesterdayValue());
                jsonObject.put(Constant.COL_ISDONE, target.isDone() ? 1 : 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, Constant.BASE_URL + url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //打印请求后获取的json数据
                                LogUtils.e(TAG, "Response:" + response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        LogUtils.e(TAG, "ErrorResponse:" + arg0.toString());
                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                }
                ;
                MyApplication.getHttpQueue().add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        MyApplication.getHttpQueue().start(); //不需要
        }
    }

    public static void getTargetJson(final SQLiteDatabase db, int id) {
        String url = "/Raw/pull_target?id=" + id;
        JsonObjectRequest jsonObjectRequest;
        try {
            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, Constant.BASE_URL + url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //打印请求后获取的json数据
                            LogUtils.e(TAG, "Response:" + response.toString());
                            try {
//                                    "result_code":"201", "name":"\u5355\u8bcd", "mdate":
//                                    "2016\/08\/27", "starttime":"08:30", "endtime":
//                                    "09:00", "description":
//                                    "\u767e\u8bcd\u65a9\uff0c\u8003\u7814\u8bcd\u6c47\uff0c\u81f3\u5c1130\u4e2a", "mvalue":
//                                    "5", "isagenda":"1", "minterval":"1", "mmaxvalue":
//                                    "5", "todayvalue":"7521", "yesterdayvalue":"7550", "isdone":"0"
                                if ("201".equals(response.getString("result_code"))){
                                    try {
                                        String name = response.getString("name");
                                        String mDate = response.getString("mdate");
                                        String startTime = response.getString("starttime");
                                        String endTime = response.getString("endtime");
                                        String description = response.getString("description");
                                        boolean isDone = response.getString("isdone").equals("1");
                                        boolean isagenda = response.getString("isagenda").equals("1");
                                        int mValue = Integer.parseInt(response.getString("mvalue"));
                                        int mInterval = Integer.parseInt(response.getString("minterval"));
                                        int mMaxValue = Integer.parseInt(response.getString("mmaxvalue"));
                                        int todayValue = Integer.parseInt(response.getString("todayvalue"));
                                        int yesterdayValue = Integer.parseInt(response.getString("yesterdayvalue"));
                                        Target target;
                                        if (isagenda){
                                            target = new Agenda(name, mDate, startTime, endTime, description, mValue, mInterval, mMaxValue,
                                                    isDone);
                                        }else {
                                            target = new Backlog(name, mDate, startTime, endTime, description, mValue, isDone);
                                        }
                                        DataUpdater.insertRawTarget(db,todayValue,yesterdayValue,target);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    LogUtils.e(TAG, "ErrorResponse:" + arg0.toString());
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            }
            ;
            MyApplication.getHttpQueue().add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
