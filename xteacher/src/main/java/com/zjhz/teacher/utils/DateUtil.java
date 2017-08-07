package com.zjhz.teacher.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.SUNDAY;

/**
 * Created by xiangxue on 2016/6/16.
 */
public class DateUtil {
    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
//        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(timeStr);
            Date d2 = new Date(System.currentTimeMillis());
            long diff = d2.getTime() - d1.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            LogUtil.e("日期转换","" + days + "天" + hours + "小时" + minutes + "分");
            if (days >= 1) {
                LogUtil.e("日期",timeStr.substring(0,16));
                return timeStr.substring(0,16);
            } else {
                if (hours >= 1 && hours <= 24) {
                    LogUtil.e("小时前",hours + "小时前");
                    return hours + "小时前";
                } else if (hours < 1 && hours > 0) {
                    LogUtil.e("分钟前",minutes + "分钟前");
                    return minutes + "分钟前";
                }else if (hours <= 0){
                    return Math.abs(minutes) + "分钟前";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
        LogUtil.e("日期转换---","" + timeStr);
        return timeStr;
    }

    /**
     * 根据日期判断是星期几
     */
    public static  int dayForWeek(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int dayForweek = 0 ;
        try {
            c.setTime(format.parse(data));
            if (c.get(Calendar.DAY_OF_WEEK) == SUNDAY){
                dayForweek = 7;
            }else {
                dayForweek = c.get(Calendar.DAY_OF_WEEK) - 1 ;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayForweek;
    }

    public static String getStandardTime(String timeStr) {
//        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(timeStr);
            Date d2 = new Date(System.currentTimeMillis());
            long diff = d2.getTime() - d1.getTime();

            if (diff>0) {
                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                LogUtil.e("日期转换","" + days + "天" + hours + "小时" + minutes + "分");
                if (days >= 1) {
                    LogUtil.e("日期",timeStr.substring(0,16));
                    return timeStr.substring(0,16);
                } else {
                    if (hours >= 1 && hours <= 24) {
                        LogUtil.e("小时前",hours + "小时前");
                        return hours + "小时前";
                    } else if (hours < 1 && hours > 0) {
                        LogUtil.e("分钟前",minutes + "分钟前");
                        return minutes + "分钟前";
                    }else if (hours <= 0){
                        return Math.abs(minutes) + "分钟前";
                    }
                }
            } else if(diff<0) {
                diff*=(-1);
                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                LogUtil.e("日期转换","" + days + "天" + hours + "小时" + minutes + "分");
                if (days >= 1) {
                    LogUtil.e("日期",timeStr.substring(0,16));
                    return timeStr.substring(0,16);
                } else {
                    if (hours >= 1 && hours <= 24) {
                        LogUtil.e("小时后",hours + "小时后");
                        return hours + "小时后";
                    } else if (hours < 1 && hours > 0) {
                        LogUtil.e("分钟后",minutes + "分钟后");
                        return minutes + "分钟后";
                    }else if (hours <= 0){
                        return Math.abs(minutes) + "分钟后";
                    }
                }
            }else{
                return "现在";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
        return timeStr.substring(0,timeStr.length()-3);
    }

    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getWeekOfDate(Date date) {
        if(date == null)
            return null;
        String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
