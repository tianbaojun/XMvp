package com.zjhz.teacher.ui.view.calendar;

import java.util.Calendar;
import java.util.List;

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

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 定义月控件
 */
public class MyMonthView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint, fly;
    private int mDayColor = Color.parseColor("#000000");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mFly = Color.parseColor("#989898");
    private int mSelectBGColor = Color.parseColor("#ff0000");
    private int mCurrentColor = Color.parseColor("#EE452F");
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize;
    private DisplayMetrics mDisplayMetrics;
    private int mDaySize = 15;
    private int weekRow;
    private int[][] daysString;
    private int mCircleRadius = (int) getResources().getDimension(R.dimen.dp_2);
    private int selectCircleRadius = (int) getResources().getDimension(R.dimen.selectCircleRadius);
    private DateClick dateClick;      // 日期点击
    private int mCircleColor = Color.parseColor("#1FC2F3");  // 绘制事务圆圈的颜色
    private List<String> daysHasThingList;

    public MyMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        fly = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);    // 当月总天数
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
        LogUtil.e("MonthDateView", "mMonthDays: = " + mMonthDays);
        SharedPreferencesUtils.putSharePrefInteger("mMonthDays", mMonthDays);
        for (int day = 0; day < mMonthDays; day++) {
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
//			Toast.makeText(getContext(), row + "行数", 1).show();
            if (dayString.equals(mSelDay + "")) {
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                mPaint.setColor(mSelectBGColor);
//				canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
                canvas.drawCircle((startRecX + endRecX) / 2, (startRecY + endRecY) / 2, selectCircleRadius, mPaint); // 绘制圆
                //记录第几行，即第几周
                weekRow = row + 1;
//				Toast.makeText(getContext(), mMonthDays + "当月总天数", 1).show();
//				SharedPreferencesUtils.putSharePrefInteger("weekRow", row);
//				ToastUtils.toast("第" + weekRow  +"周");
                mOnScrollListener.setOnClickCustomListener(weekRow);
//				SharedPreferencesUtils.putSharePrefInteger("weekRow", weekRow);
            }
            fly.setColor(mFly);
//			fly.setStrokeWidth(1);
//			canvas.drawLine(0, mRowSize * row, 10000000, mRowSize * row, fly);// 画线
            //绘制事务圆形标志
            drawCircle(row, column, day + 1, canvas);
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth) {
                //正常月，选中其他日期，则今日为红色
                mPaint.setColor(mCurrentColor);
            } else {
                mPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
        }
    }

    /**
     * 绘制事务圆形标志
     */
    private void drawCircle(int row, int column, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            String manth, mmDay;
            if ((mSelMonth + 1) < 10) {
                manth = "0" + (mSelMonth + 1);
            } else {
                manth = String.valueOf(mSelMonth + 1);
            }
            if (day < 10) {
                mmDay = "0" + day;
            } else {
                mmDay = String.valueOf(day);
            }
            String mDay = String.valueOf(mSelYear) + "-" + manth + "-" + mmDay;
            if (!daysHasThingList.contains(mDay)) return;
            mPaint.setColor(mCircleColor);
            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;
            int endRecX = startRecX + mColumnSize;
            int endRecY = startRecY + mRowSize;
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
            float circley = (float) (mRowSize * row + mRowSize * 0.2);
//			canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
            canvas.drawCircle((startRecX + endRecX) / 2, ((startRecY + endRecY) / 2) + getResources().getDimension(R.dimen.dp_12), mCircleRadius, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//		detector.onTouchEvent(event);// 把手势识别器注册到触摸事件中
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }

                if ((downX - upX) > getWidth() / 3) {
                    LogUtil.e("进入下一个界面", getmSelMonthTwo() + "-----------" + SharedPreferencesUtils.getSharePrefString("school_subendtime"));
//				if (!getmSelMonthTwo().equals(SharedPreferencesUtils.getSharePrefString("school_subendtime"))) {
                    onRightClick();
                    mOnScrollListener.setOnScrollListener();
//				}else{
//					ToastUtils.toast("没有更多日期");
//				}
                } else if ((upX - downX) > getWidth() / 3) {
                    LogUtil.e("进入上一个界面", getmSelMonthTwo() + "-----------" + SharedPreferencesUtils.getSharePrefString("school_substarttime"));
                    // 进入上一个界面
//				if (!getmSelMonthTwo().equals(SharedPreferencesUtils.getSharePrefString("school_substarttime"))) {
                    onLeftClick();
                    mOnScrollListener.setOnScrollListener();
//				}else{
//					ToastUtils.toast("没有更多日期");
//				}
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        if (column >= 7) {
            column = 6;
            setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        }else{
            setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        }

//        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null && mSelDay != 0) {
            invalidate();
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左滑
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 右滑
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取选择的年份
     *
     * @return
     */
    public String getmSelYear() {
        return String.valueOf(mSelYear);
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public String getmSelMonth() {
        String month = null;
        if ((mSelMonth + 1) < 10) {
            month = "0" + String.valueOf(mSelMonth + 1);
        } else {
            month = String.valueOf(mSelMonth + 1);
        }
        return month;
    }

    /**
     * 获取选择的年月
     *
     * @return
     */
    public String getmSelMonthTwo() {
        String month = null;
        if ((mSelMonth + 1) < 10) {
            month = "0" + String.valueOf(mSelMonth + 1);
        } else {
            month = String.valueOf(mSelMonth + 1);
        }
        return String.valueOf(mSelYear) + "-" + month;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getmSelMonthOne() {
        return mSelMonth + 1;
    }

    /**
     * 获取选择的日期
     *
     * @param
     */
    public String getmSelDay() {
        String month = null;
        String day = null;
        if ((mSelMonth + 1) < 10) {
            month = "0" + String.valueOf(mSelMonth + 1);
        } else {
            month = String.valueOf(mSelMonth + 1);
        }
        if (mSelDay < 10) {
            day = "0" + String.valueOf(mSelDay);
        } else {
            day = String.valueOf(mSelDay);
        }
        return String.valueOf(mSelYear) + "-" + month + "-" + day;
    }

    /**
     * 获取选择的日期
     *
     * @param
     */
    public String getmSelDayOne() {
        String day = null;
        if (mSelDay < 10) {
            day = "0" + String.valueOf(mSelDay);
        } else {
            day = String.valueOf(mSelDay);
        }
        return day;
    }

    /**
     * 获取选择的日期
     *
     * @param
     */
    public int getmSelDayTwo() {
        return mSelDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     *
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     *
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认红色色
     *
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认蓝色
     *
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     *
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置事务天数
     *
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<String> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     *
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     *
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    private OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    public interface OnScrollListener {
        void setOnScrollListener();

        void setOnClickCustomListener(int number);
    }
}
