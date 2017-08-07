package com.zjhz.teacher.ui.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 定义月控件
 */
public class MonthView extends View {
		private static final int NUM_COLUMNS = 7;
		private static final int NUM_ROWS = 6;
		private Paint mPaint,fly;
		private int mDayColor = Color.parseColor("#000000");
		private int mSelectDayColor = Color.parseColor("#ffffff");
		private int mFly = Color.parseColor("#989898");
	private int mSelectBGColor = Color.parseColor("#c4d600");
//	private int mCurrentColor = Color.parseColor("#1FC2F3");
	private int mCurrentColor = Color.parseColor("#EE452F");
	//当前年月日
	private int mCurrYear,mCurrMonth,mCurrDay;
	//选中的年月日
	private int mSelYear,mSelMonth,mSelDay;
	//该学期的开始和结束时间
	private String startTime,endTime;
	private Calendar endCalender = Calendar.getInstance();
	private Calendar startCalender = Calendar.getInstance();

	private int mColumnSize,mRowSize;
	private DisplayMetrics mDisplayMetrics;
	private int mDaySize = 15;
	private int weekRow;
	private int [][] daysString;
	private int mCircleRadius = 4;
	private int selectCircleRadius= (int) getResources().getDimension(R.dimen.selectCircleRadius);
	private DateClick dateClick;      // 日期点击
	private int mCircleColor = Color.parseColor("#1FC2F3");  // 绘制事务圆圈的颜色
	private List<String> daysHasThingList;

	public MonthView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplayMetrics = getResources().getDisplayMetrics();
		Calendar calendar = Calendar.getInstance();
		mPaint = new Paint();
		fly = new Paint();
		mCurrYear = calendar.get(Calendar.YEAR);
		mCurrMonth = calendar.get(Calendar.MONTH);
//		if ((mCurrMonth +1) == 8) {
//			mCurrMonth = 8;
//		}else{
			mCurrMonth = calendar.get(Calendar.MONTH);
//		}
		mCurrDay = calendar.get(Calendar.DATE);
		setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		initSize();
		daysString = new int[6][7];
		mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
		String dayString;
		int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);    // 当月总天数
		int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
		LogUtil.e("MonthDateView", "mMonthDays: = " + mMonthDays);
		SharedPreferencesUtils.putSharePrefInteger("mMonthDays", mMonthDays);
		for(int day = 0;day < mMonthDays;day++){
			dayString = (day + 1) + "";
			int column = (day+weekNumber - 1) % 7;
			int row = (day+weekNumber - 1) / 7;
			daysString[row][column]=day + 1;
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
			int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
//			Toast.makeText(getContext(), row + "行数", 1).show();
			if(dayString.equals(mSelDay+"")){   //画圆
				int startRecX = mColumnSize * column;
				int startRecY = mRowSize * row;
				int endRecX = startRecX + mColumnSize;
				int endRecY = startRecY + mRowSize;
				mPaint.setColor(mSelectBGColor);
				canvas.drawCircle((startRecX + endRecX)/2, (startRecY + endRecY)/2, selectCircleRadius, mPaint); // 绘制圆
				//记录第几行，即第几周
				weekRow = row + 1;
				SharedPreferencesUtils.putSharePrefString("school_calendar_week_num",weekRow+"");
				LogUtil.e("MonthView周数 = " + weekRow);

				mOnScrollListener.setOnClickCustomListener(weekRow);
			}
			fly.setColor(mFly);
//设置画笔颜色

			//当前绘制的日期
			Calendar showCalendar = Calendar.getInstance();
			showCalendar.set(mSelYear, mSelMonth, Integer.valueOf(dayString));
			if (showCalendar.compareTo(startCalender)>0&&showCalendar.compareTo(endCalender)<0) {
				if(dayString.equals(mSelDay+"")){
                    mPaint.setColor(mSelectDayColor);
                }else if(dayString.equals(mCurrDay+"") && mCurrDay != mSelDay && mCurrMonth == mSelMonth){
                    mPaint.setColor(mCurrentColor);
                }else{
                    mPaint.setColor(mDayColor);
                }
			} else {
				mPaint.setColor(getResources().getColor(R.color.gray9));
			}
			canvas.drawText(dayString, startX, startY, mPaint);
		}
	}

	private int downX = 0,downY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventCode=  event.getAction();
		switch(eventCode){
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getX();
			int upY = (int) event.getY();
			if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
				performClick();
				doClickAction((upX + downX)/2,(upY + downY)/2);
			}
			
			if ((downX-upX)>getWidth()/3) {//右滑
				LogUtil.e("进入下一个界面",getmSelYearMonth() + "-----------" + SharedPreferencesUtils.getSharePrefString("school_subendtime"));
				String yearFly = getmSelYearMonth().trim();
				if (!yearFly.equals(SharedPreferencesUtils.getSharePrefString("school_subendtime"))) {
					onRightClick();
					mOnScrollListener.setOnScrollListener();
				}else{
					ToastUtils.toast("没有更多日期");
				}
			}else if((upX-downX)>getWidth()/3){//左滑
				LogUtil.e("进入上一个界面",getmSelYearMonth() + "-----------" + SharedPreferencesUtils.getSharePrefString("school_substarttime"));
				// 进入上一个界面
				if (!getmSelYearMonth().equals(SharedPreferencesUtils.getSharePrefString("school_substarttime"))) {
					onLeftClick();
					mOnScrollListener.setOnScrollListener();
				}else{
					ToastUtils.toast("没有更多日期");
				}
			}
			break;
		}
		return true;
	}

	/**
	 * 初始化列宽行高
	 */
	private void initSize(){
		mColumnSize = getWidth() / NUM_COLUMNS;
		mRowSize = getHeight() / NUM_ROWS;
	}
	
	/**
	 * 设置年月
	 * @param year
	 * @param month
	 */
	private void setSelectYearMonth(int year,int month,int day){
		mSelYear = year;
		mSelMonth = month;
		mSelDay = day;
	}
	/**
	 * 执行点击事件
	 * @param x
	 * @param y
	 */
	private void doClickAction(int x,int y){
		int row = y / mRowSize;
		int column = x / mColumnSize;
		if (column >= 7){
			column = 6;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(mSelYear,mSelMonth,daysString[row][column]);

		if(calendar.compareTo(startCalender)>0&&calendar.compareTo(endCalender)<0){
			setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
			invalidate();
			//执行activity发送过来的点击处理事件
			if(dateClick != null){
				dateClick.onClickOnDate(weekRow);
			}
		}

	}

	/**
	 * 左滑
	 */
	public void onLeftClick(){
		mOnScrollListener.scrollViewListener();
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if(month == 0){//若果是1月份，则变成12月份
			year = mSelYear - 1;
			month = 11;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一天，当向前推的时候，就需要改变选中的日期
			month = month-1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month-1;
		}
		if(month == (DateUtils.getMonthNumber(startTime)-1)&& day <DateUtils.getYouWant(startTime,Calendar.DAY_OF_MONTH)){
			day = DateUtils.getYouWant(startTime,Calendar.DAY_OF_MONTH);
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}
	
	/**
	 * 右滑
	 */
	public void onRightClick(){
		mOnScrollListener.scrollViewListener();
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if(month == 11){//若果是12月份，则变成1月份
			year = mSelYear+1;
			month = 0;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month + 1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month + 1;
		}
		if(month == (DateUtils.getMonthNumber(endTime)-1)&& day >DateUtils.getYouWant(endTime,Calendar.DAY_OF_MONTH)){
			day = DateUtils.getYouWant(endTime,Calendar.DAY_OF_MONTH);
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}
	
	/**
	 *
	 * @return 获取选择的年份
	 */
	public String getmSelYear() {
		return String.valueOf(mSelYear);
	}

	/**
	 * 获取年份
	 * @return int 年份 eg:2012
     */
	public int getmSelYearInt(){
		return mSelYear;
	}
	/**
	 *
	 * @return 获取选择的月份（01,02形式）String
	 */
	public String getmSelMonth() {
		String month = null;
		if ((mSelMonth + 1) < 10) {
			month = "0"+String.valueOf(mSelMonth + 1);
		}else{
			month = String.valueOf(mSelMonth + 1);
		}
		return month;
	}

	/**
	 *
	 * @return int 获取选择的月份（1-12）
	 */
	public int getmSelMonth1() {
		return mSelMonth + 1;
	}

	/**
	 *
	 * @return 获取选择的年月(2012-12)
	 */
	public String getmSelYearMonth() {
		String month = null;
		if ((mSelMonth + 1) < 10) {
			month = "0"+String.valueOf(mSelMonth + 1);
		}else{
			month = String.valueOf(mSelMonth + 1);
		}
		return String.valueOf(mSelYear) + "-" + month;
	}
	
	/**
	 * @return  获取选择的日期(2012-02-12)
	 *
	 */
	public String getmSelDay() {
		String month = null;
		String day = null;
		if ((mSelMonth + 1) < 10) {
			month = "0"+String.valueOf(mSelMonth + 1);
		}else{
			month = String.valueOf(mSelMonth + 1);
		}
		if (mSelDay < 10) {
			day = "0"+String.valueOf(mSelDay);
		}else{
			day = String.valueOf(mSelDay);
		}
		return String.valueOf(mSelYear) + "-" + month + "-" + day;
	}
	
	/**
	 * 获取选择的日期
	 * @return 只获取日期  02
	 */
	public String getmSelDayOne() {
		String day = null;
		if (mSelDay < 10) {
			day = "0"+String.valueOf(mSelDay);
		}else{
			day = String.valueOf(mSelDay);
		}
		return day;
	}

	/**
	 * 获取选择的日期
	 * @return int 日期
	 */
	public int getmSelDayTwo() {
		return mSelDay;
	}
	
	/**
	 * 普通日期的字体颜色，默认黑色
	 * @param mDayColor
	 */
	public void setmDayColor(int mDayColor) {
		this.mDayColor = mDayColor;
	}
	
	/**
	 * 选择日期的颜色，默认为白色
	 * @param mSelectDayColor
	 */
	public void setmSelectDayColor(int mSelectDayColor) {
		this.mSelectDayColor = mSelectDayColor;
	}

	/**
	 * 选中日期的背景颜色，默认红色色
	 * @param mSelectBGColor
	 */
	public void setmSelectBGColor(int mSelectBGColor) {
		this.mSelectBGColor = mSelectBGColor;
	}
	/**
	 * 当前日期不是选中的颜色，默认蓝色
	 * @param mCurrentColor
	 */
	public void setmCurrentColor(int mCurrentColor) {
		this.mCurrentColor = mCurrentColor;
	}

	/**
	 * 日期的大小，默认18sp
	 * @param mDaySize
	 */
	public void setmDaySize(int mDaySize) {
		this.mDaySize = mDaySize;
	}

	/**
	 * 设置事务天数
	 * @param daysHasThingList
	 */
	public void setDaysHasThingList(List<String> daysHasThingList) {
		this.daysHasThingList = daysHasThingList;
	}

	/***
	 * 设置圆圈的半径，默认为6
	 * @param mCircleRadius
	 */
	public void setmCircleRadius(int mCircleRadius) {
		this.mCircleRadius = mCircleRadius;
	}
	
	/**
	 * 设置圆圈的半径
	 * @param mCircleColor
	 */
	public void setmCircleColor(int mCircleColor) {
		this.mCircleColor = mCircleColor;
	}
	
	/**
	 * 设置日期的点击回调事件
	 * @author shiwei.deng
	 *
	 */
	public interface DateClick{
		public void onClickOnDate(int number);
	}

	/**
	 * 设置日期点击事件
	 * @param dateClick
	 */
	public void setDateClick(DateClick dateClick) {
		this.dateClick = dateClick;
	}
	
	private OnScrollListener mOnScrollListener;
	
	public void setOnScrollListener(OnScrollListener mOnScrollListener){
		this.mOnScrollListener = mOnScrollListener;
	}
	
	public interface OnScrollListener{
		void setOnScrollListener();
		void setOnClickCustomListener(int number);
		void scrollViewListener();
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			startCalender.setTime(format.parse(startTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		invalidate();
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			endCalender.setTime(format.parse(endTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		invalidate();
	}
}
