package com.zshc.frank.zshclauncher.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/4/21 0021.
 * 获取时间的工具类
 * 传进来一个calendar对象，返回string类型的月份，日期，和星期
 */
public class GetDateUtil {
    public static String getDate(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String mDay = String.valueOf(calendar.DAY_OF_MONTH);
        String mWeek = String.valueOf(calendar.get(calendar.DAY_OF_WEEK));
        if ("1".equals(mWeek)) {
            mWeek = "日";
        } else if ("2".equals(mWeek)) {
            mWeek = "一";
        } else if ("3".equals(mWeek)) {
            mWeek = "二";
        } else if ("4".equals(mWeek)) {
            mWeek = "三";
        } else if ("5".equals(mWeek)) {
            mWeek = "四";
        } else if ("6".equals(mWeek)) {
            mWeek = "五";
        } else if ("7".equals(mWeek)) {
            mWeek = "六";
        }
        return " " + mMonth + "月" + mDay + "日" + "   " + "星期" + mWeek;
    }
}
