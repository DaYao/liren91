package com.liren91.liren91.utils;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by smallQ on 15-3-28.
 */
public class MyLog
{
    public static HashMap<String, Boolean> logTrigger = new HashMap<>();

    //--------------添加类的Log开关值----------------------
    {
        logTrigger.put("HomePageFragment", false);
        logTrigger.put("TestActivity",false);
        logTrigger.put("LoginActivity",false);
        logTrigger.put("MainActivity",false);
        logTrigger.put("UserDetail",false);
        logTrigger.put("HomePageFragment",false);
        logTrigger.put("RecruitPageFragment",false);
        logTrigger.put("ActivitiesPageFragment",false);
    }

    //---------------------------------------------------
    boolean mFlag;

    public MyLog(Object object)
    {
        this.mFlag = logTrigger.get(object.getClass().getSimpleName()) != null ?
                logTrigger.get(object.getClass().getSimpleName()) : false;
    }

    //-----------------------------------------------------
    public String tag(Object o)
    {
        return o.getClass().getSimpleName();
    }

    //--------------------实现Log类的各个方法---------------------
    public void v(Object o, String s)
    {
        if (mFlag) Log.v(tag(o), s);
    }

    public void d(Object o, String s)
    {
        if (mFlag) Log.d(tag(o), s);
    }

    public void i(Object o, String s)
    {
        if (mFlag) Log.i(tag(o), s);
    }

    public void w(Object o, String s)
    {
        if (mFlag) Log.w(tag(o), s);
    }

    public void e(Object o, String s)
    {
        if (mFlag) Log.e(tag(o), s);
    }

    public void wtf(Object o, String s)
    {
        if (mFlag) Log.wtf(tag(o), s);
    }
}
