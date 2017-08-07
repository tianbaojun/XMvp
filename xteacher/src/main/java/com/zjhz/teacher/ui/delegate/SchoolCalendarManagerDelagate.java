package com.zjhz.teacher.ui.delegate;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolCalendarManagerEventItem;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.SchoolCalendarManagerActivity;
import com.zjhz.teacher.ui.view.calendar.DateUtils;
import com.zjhz.teacher.ui.view.calendar.MonthView;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 校历
 */
public class SchoolCalendarManagerDelagate implements MonthView.OnScrollListener,View.OnClickListener {

    SchoolCalendarManagerActivity context;
    int number; // 显示的个数
    //周次
    private int weekNumber;

    //总周次
    int weeks;

    private List<View> views = new ArrayList<>();

    public SchoolCalendarManagerDelagate(SchoolCalendarManagerActivity context) {
        this.context = context;
    }

    public void initialize() {
        initView();
        initData();
//        postDelay();
    }

    public void initView() {
        context.myMonthView.setOnScrollListener(this);
        context.title_back_img.setOnClickListener(this);
        context.one.setOnClickListener(this);
        context.two.setOnClickListener(this);
        context.three.setOnClickListener(this);
        context.four.setOnClickListener(this);
        context.five.setOnClickListener(this);
        context.six.setOnClickListener(this);
        views.add(context.one);
        views.add(context.two);
        views.add(context.three);
        views.add(context.four);
        views.add(context.five);
        views.add(context.six);

        context.myMonthView.setDateClick(new MonthView.DateClick() {
            @Override
            public void onClickOnDate(int number) {

            }
        });

    }

    public void initData() {
        //获取weeks
        weeks =  SharedPreferencesUtils.getSharePrefInteger("total_weeknum");
        //当前月份的总天数
        int day = SharedPreferencesUtils.getSharePrefInteger("mMonthDays");
        //学期开始时间
        String startTime = SharedPreferencesUtils.getSharePrefString("school_starttimeone");
        //学期开始年份
        int startYear = DateUtils.getYouWant(startTime,Calendar.YEAR);
        //学期开始的月份
        int startMonth = DateUtils.getYouWant(startTime,Calendar.MONTH)+1;
        //学期结束时间
        String endTime = SharedPreferencesUtils.getSharePrefString("school_endtimeone");
        //学期结束年份
        int endYear = DateUtils.getYouWant(endTime,Calendar.YEAR);
        //学期结束的月份
        int endMonth = DateUtils.getYouWant(endTime,Calendar.MONTH)+1;
        context.myMonthView.setStartTime(startTime);
        context.myMonthView.setEndTime(endTime);
//        context.titleTv.setText(context.getResources().getText(R.string.school_calendar_manager)+"("+context.myMonthView.getmSelYear()+")");
        //计算当前学期共有多少天
        int totalDays = 0;
        if(startYear == endYear) {
            totalDays = DateUtils.getYouWant(endTime, Calendar.DAY_OF_YEAR) - DateUtils.getYouWant(startTime, Calendar.DAY_OF_YEAR) + 1;
        }else if((endYear-startYear)==1){
            totalDays = - DateUtils.getYouWant(startTime, Calendar.DAY_OF_YEAR) + 365 + DateUtils.getYouWant(endTime, Calendar.DAY_OF_YEAR) + 1;
            if(((startYear % 4 == 0) && (startYear % 100 != 0)) || (startYear % 400 == 0)){ //闰年加一天
                totalDays++;
            }
        }else{
            ToastUtils.showShort("网络数据异常...");
            context.finish();
        }
        //如果学期天数大于周数*7，则需要改变周数
        {
            int week = 0;
            int weekOfStart = DateUtils.getYouWant(startTime,Calendar.DAY_OF_WEEK);
            if(totalDays%7==0&&weekOfStart==Calendar.SUNDAY){
                week = totalDays/7;
            }else {
                week = (totalDays+weekOfStart)/7+1;
            }
            weeks = week;
        }
        //当前选择的月份
        int currentMonth =  context.myMonthView.getmSelMonth1();
        //当前选择月份的一号的"yy-mm-dd"字符串形式
        String firstDayOfSelMonth = context.myMonthView.getmSelYear()+ "-" + currentMonth + "-" + "01";
        //当前选择月份的一号为该年第几周
        int weekOfFirstDayInMonth = DateUtils.getYouWant(firstDayOfSelMonth,Calendar.WEEK_OF_YEAR);

        //学期开始时间是当年第几周
        int startWeek = DateUtils.getYouWant(startTime,Calendar.WEEK_OF_YEAR);
        if(startYear==endYear){
            weekNumber = weekOfFirstDayInMonth - startWeek + 1;
        }else if(endYear-startYear==1){
            if(startYear == context.myMonthView.getmSelYearInt()){
                weekNumber = weekOfFirstDayInMonth - startWeek + 1;
            }else if(endYear == context.myMonthView.getmSelYearInt()){
                String lastDayOfStartYear = startYear+"-"+ 12 +"-"+"31";
                int weekNum = DateUtils.getYouWant(lastDayOfStartYear,Calendar.DAY_OF_WEEK);
                int weeksInStartYear = DateUtils.getYouWant(lastDayOfStartYear,Calendar.WEEK_OF_YEAR) - startWeek + 1 ;
                if(weekNum == Calendar.SATURDAY){
                    weekNumber = weeksInStartYear + weekOfFirstDayInMonth;
                }else{
                    weekNumber = weeksInStartYear + weekOfFirstDayInMonth-1;
                }
            }
        }
        int week = DateUtils.getWeek(firstDayOfSelMonth);  // 星期几
        context.month.setText(judgeMonth(currentMonth));  // 显示月份
        //如果该月有31天并且1号在星期六或者星期五，有30天一号在星期6
        if (((day == 31)  && (week == 5 || week == 6))||((day == 30)&&(week == 6))) {
            number = 6;
        }else if(day == 28 && week == 7){//如果有28天并且1号在星期日
            number = 4;
        }else{
            number = 5;
        }
        if(startYear == endYear && currentMonth>=startMonth && currentMonth <= endMonth){
            setText(number);
        }else if(endYear-startYear==1){
            if(context.myMonthView.getmSelYearInt() == startYear && currentMonth>=startMonth){
                setText(number);
            }else if(context.myMonthView.getmSelYearInt() == endYear && currentMonth<=endMonth){
                setText(number);
            }
        }
    }

    /**
     * 获取当前周数的显示字符串
     * @param weekNumber 当前校历周数
     * @return
     */
    public String getStrOfWeek(int weekNumber){
        return (weekNumber>0&&weekNumber<=weeks)?String.valueOf(weekNumber) + "周":"";
    }

    /**
     * 设置周次
     * @param number 当前月份包含的周数
     */
    public void setText(int number) {

        context.one.setText(getStrOfWeek(weekNumber));
        context.two.setText(getStrOfWeek(weekNumber+1));
        context.three.setText(getStrOfWeek(weekNumber+2));
        context.four.setText(getStrOfWeek(weekNumber+3));
        if (6 == number) {
            context.six.setVisibility(View.VISIBLE);
            context.five.setVisibility(View.VISIBLE);
            context.five.setText(getStrOfWeek(weekNumber+4));
            context.six.setText(getStrOfWeek(weekNumber+5));
        }else if (5 == number){
            context.six.setVisibility(View.INVISIBLE);
            context.five.setVisibility(View.VISIBLE);
            context.five.setText(getStrOfWeek(weekNumber+4));
        }else{
            context.six.setVisibility(View.INVISIBLE);
            context.five.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 转换月份为月份字符串
     * @param month 月份数字
     * @return
     */
    public String judgeMonth(int month){
        switch (month) {
            case 1:
                return "一月";
            case 2:
                return "二月";
            case 3:
                return "三月";
            case 4:
                return "四月";
            case 5:
                return "五月";
            case 6:
                return "六月";
            case 7:
                return "七月";
            case 8:
                return "八月";
            case 9:
                return "九月";
            case 10:
                return "十月";
            case 11:
                return "十一月";
            case 12:
                return "十二月";
            default:
                break;
        }
        return null;
    }

    @Override
    public void setOnScrollListener() {
        postDelay();
    }

    /**
     * @param number monthview 周数
     */
    @Override
    public void setOnClickCustomListener(int number) { // 日期点击
        //获取点击的周数
        TextView view = (TextView) views.get(number - 1);
        LogUtil.e("校历日期点击");
        request(view);
    }

    @Override
    public void scrollViewListener() {    }

    public void postDelay(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                context.finish();
                break;
            case R.id.activity_school_calendar_manager_one:
                LogUtil.e("校历日期点击one");
                request(context.one);
                break;
            case R.id.activity_school_calendar_manager_two:
                LogUtil.e("校历日期点击two");
                request(context.two);
                break;
            case R.id.activity_school_calendar_manager_three:
                LogUtil.e("校历日期点击three");
                request(context.three);
                break;
            case R.id.activity_school_calendar_manager_four:
                LogUtil.e("校历日期点击four");
                request(context.four);
                break;
            case R.id.activity_school_calendar_manager_five:
                LogUtil.e("校历日期点击five");
                request(context.five);
                break;
            case R.id.activity_school_calendar_manager_six:
                LogUtil.e("校历日期点击six");
                request(context.six);
                break;
        }
    }

    private void request(final TextView view){
        context.lists.clear();
        final String str = view.getText().toString().trim();
        if (str !=null&&str.length()!=0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                        String substring = str.substring(0, str.length()-1);
                        LogUtil.e("校历管理","周次 = " + substring);
                        if (context.isFirst) {

                        }else{
                            LogUtil.e("校历boolean = ",context.isFirst + "");
                            context.dialog.show();
                            context.lists.clear();
                            context.listView.requestLayout();
                            context.adapter.notifyDataSetChanged();
                            NetworkRequest.request(new SchoolCalendarManagerEventItem(substring,String.valueOf(1),"80"), CommonUrl.SCHOOLEVENTITEM, Config.SCHOOLEVENTITEM);
                        }
                }
            });
        }
    }
}
