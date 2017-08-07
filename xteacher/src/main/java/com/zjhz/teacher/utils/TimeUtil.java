package com.zjhz.teacher.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.zjhz.teacher.ui.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class TimeUtil {

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMDHMSTime(Date dates) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = mDateFormat.format(dates);
        return date;
    }

    /**
     * yyyy-MM-dd HH:mm
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMDHMTime(Date dates) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String date = mDateFormat.format(dates);
        return date;
    }

    /**
     * MM-dd HH:mm:ss
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowMDHMSTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "MM-dd HH:mm:ss");
        String date = mDateFormat.format(new Date());
        return date;
    }

    /**
     * MM-dd
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMD() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = mDateFormat.format(new Date());
        return date;
    }

    public static String getNowYMDHMS() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:MM:SS");
        String date = mDateFormat.format(new Date());
        return date;
    }

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYMD(Date date) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateS = mDateFormat.format(date);
        return dateS;
    }
    public static String getYMDSKEW(Date date) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yy/MM/dd");
        String dateS = mDateFormat.format(date);
        return dateS;
    }
    public static String getM(Date date) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "M");
        String dateS = mDateFormat.format(date);
        return dateS;
    }

    public static String getYMDHM(Date date) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String dateS = mDateFormat.format(date);
        return dateS;
    }

    /**
     * 获取当前时间
     */
    public static String refFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String retStrFormatNowDate = sdFormatter.format(nowTime);

        return retStrFormatNowDate;
    }

    /**
     * 获取当前时间 时分
     */
    public static String getDateTime() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("HH:mm");
        String retStrFormatNowDate = sdFormatter.format(nowTime);

        return retStrFormatNowDate;
    }

    /**
     * 获取当前时间 年月日时
     */
    public static String getDateTimeWithMinute() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);

        return retStrFormatNowDate;
    }
//	public static long getTimestamp() throws ParseException {
//		Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//				.parse("2009/12/11 00:00:00");
//		Date date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//				.parse("1970/01/01 08:00:00");
//		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
//				- date2.getTime() : date2.getTime() - date1.getTime();
//		long rand = (int)(Math.random()*1000);
//
//		return rand;
//	}

    /**
     * 滚轮选择日期
     */
    public static void selectDate(Context context, final TextView view) {

        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY, null,0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, String type) {
                view.setText(getYMD(date));
            }
        });
    }

    /**
     * 滚轮选择日期
     */
    public static void selectDateYMDHM(Context context, final TextView view) {

        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN, null, 0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, String type) {
                view.setText(getYMDHM(date));
            }
        });
    }


    public void setDate(int startYear, int endYear) {

    }
}
