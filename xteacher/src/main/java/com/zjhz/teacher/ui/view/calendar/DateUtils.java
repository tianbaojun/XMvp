package com.zjhz.teacher.ui.view.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 日期工具类
 */
public class DateUtils {

	/**
	 * 判断当前日期是星期几
	 *
	 * @param pTime 设置的需要判断的时间  //格式如2016-06-08
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

//  String pTime = "2016-06-08";
	public static int getWeek(String pTime) {
		int Week = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
			Week += 7;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			Week += 1;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
			Week += 2;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			Week += 3;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			Week += 4;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			Week += 5;
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			Week += 6;
		}
		return Week;
	}

	/**
	 * 获取当前日期对应的周数
	 * @param date  设置的需要判断的时间  //格式如2016-06-08
	 * @return
	 */
	public static int getWeekNumber(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setFirstDayOfWeek(Calendar.SUNDAY);
			cl.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int week = cl.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	/**
	 * 根据日期字符串判断当月第几周
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static int getCurrWeek(String str) {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//第几周
		int week = calendar.get(Calendar.WEEK_OF_MONTH);
		//第几天，从周日开始
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return week;
	}

	/**
	 *
	 * @param str yyyy-MM-dd 格式的日期字符串
	 * @param type Calendar.WEEK_OF_MONTH 类型
     * @return
     */
	public static int getYouWant(String str,int type){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(type);
	}

	/**
	 * 获取当前日期对应的月数
	 * @param date  设置的需要判断的时间  //格式如2016-06-08
	 * @return
	 */
	public static int getMonthNumber(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setFirstDayOfWeek(Calendar.SUNDAY);
			cl.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int month = cl.get(Calendar.MONTH)+1;
		return month;
	}

	/**
	 * 获取当前日期对应的时间
	 * @param date  设置的需要判断的时间  //格式如2016-06-08
	 * @return
	 */
	public static  long getTimeNumber(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		try {
			cl.setFirstDayOfWeek(Calendar.SUNDAY);
			cl.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = cl.getTime().getTime();
		return time;
	}

	/**
	 * 通过年份和月份 得到当月的最后一天的日子
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
					return 29;
				}else{
					return 28;
				}
			default:
				return  -1;
		}
	}
	/**
	 * 返回当前月份1号位于周几
	 * @param year
	 * 		年份
	 * @param month
	 * 		月份，传入系统获取的，不需要正常的
	 * @return
	 * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
	 */
	public static int getFirstDayWeek(int year, int month){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
		return calendar.get(Calendar.DAY_OF_WEEK);
	}


	/**
	 * 计算当前日期是任意时间段内第几周
	 *
	 * @param year
	 * @param month
	 * @return
	 */
/*	public static int getMonthDays(String BeginDate, String EndDate, String InputDate) {
		int calculateWeekNo  = 0;
		int z = 0;
		int x = 0;
		for( long i =  getTimeNumber(BeginDate);  i< getTimeNumber(EndDate);)


		return calculateWeekNo;
	}*/
}
