package com.xuncl.selfimproveproject.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.MyApplication;
import com.xuncl.selfimproveproject.service.Agenda;
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

    public static void showJson(Scheme scheme) {
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

            LogUtils.e(TAG, jsonObject.toString());
        }
    }

    public static void postJson(Scheme scheme) {
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


    public static void volley_Post(final Context context) {
        String url = "http://apis.juhe.cn/mobile/get?";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        Toast.makeText(context, arg0,
                                Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Toast.makeText(context, "网络请求失败",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone", "13666666666");
                map.put("key", "335adcc4e891ba4e4be6d7534fd54c5d");
                return map;
            }
        };
        request.setTag("abcPost");
        MyApplication.getHttpQueue().add(request);
    }

    public void volley_Get(final Context context) {
        String url = "http://apis.juhe.cn/mobile/get?phone=13666666666&key=335adcc4e891ba4e4be6d7534fd54c5d";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        Toast.makeText(context, arg0,
                                Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Toast.makeText(context, "网络请求失败",
                        Toast.LENGTH_LONG).show();
            }
        });
        request.setTag("abcGet");
        MyApplication.getHttpQueue().add(request);
    }
}
