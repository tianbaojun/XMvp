package com.zjhz.teacher.utils;

import android.content.Context;

import com.zjhz.teacher.ui.view.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-08
 * Time: 16:57
 * Description: 滚轮时间或者数据选择工具类    4列
 */
public class WheelUtil {
    public String flag;
    public String historyDate = "";
    public String historyDateInt;
    OnWheelClickSubmit mOnClickSubmit;
    private ArrayList<String> typeString = new ArrayList<>();
    private String tag;
    private int currentType = -1;

    public void setType(String tag) {
        this.tag = tag;
    }

    public void initDatas() {
        if ("1".equals(tag)) {
            typeString.add("上午");
            typeString.add("下午");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            if (hours > 12) {
                currentType = 1;
            } else {
                currentType = 0;
            }
        } else {
            typeString.add("上学");
            typeString.add("放学");
        }
    }

    /**
     * 时间选择器   年月日   类型
     * TimePickerView.Type.YEAR_MONTH_DAY_TYPE
     * @param context
     */
    public void selectData(Context context) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY_TYPE, typeString,1);

        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 1, calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        if (currentType != -1)
            pvTime.setCurrentType(currentType);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, String type) {
                flag = type;
                historyDate = TimeUtil.getYMD(date);
                historyDateInt = Integer.parseInt(historyDate.split("-")[0]) * 365 + Integer.parseInt(historyDate.split("-")[1]) * 30
                        + Integer.parseInt(historyDate.split("-")[2]) + "";
                mOnClickSubmit.onClickSubmit();
            }
        });
    }


//    /**
//     *
//     * @param context
//     * @param type 时间选择类型，控制选择年月日    TimePickerView.Type  里面选择
//     */
//    public void selectData(Context context, TimePickerView.Type type) {
//        TimePickerView pvTime = new TimePickerView(context, type, typeString, 1);
//        // 控制时间范围在2016年-20之间,去掉将显示全部
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
//        pvTime.setTime(new Date());
//        pvTime.setCyclic(false);// 是否滚动
//        pvTime.setCancelable(true);
//        // 弹出时间选择器
//        pvTime.show();
//        // 时间选择后回调
//        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, String type) {
//                flag = type;
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                historyDate = mDateFormat.format(date);
//                int m = historyDate.indexOf(":");
//                Double historyDateIntTmp = Integer.parseInt(historyDate.split("-")[0]) * 365 * 24 + Integer.parseInt(historyDate.split("-")[1]) * 30 * 24
//                        + Integer.parseInt(historyDate.split("-")[2].substring(0, 2)) * 24 + Integer.parseInt(historyDate.substring(m - 2, m))
//                        + Double.parseDouble(historyDate.split(":")[1]) / 60;
//                DecimalFormat df = new DecimalFormat("#0.0");
//                historyDateIntTmp = Double.parseDouble(df.format(historyDateIntTmp));
//                historyDateInt = historyDateIntTmp + "";
//                mOnClickSubmit.onClickSubmit();
//            }
//        });
//    }


    public void setOnClickSubmit(OnWheelClickSubmit onClickSubmit) {
        mOnClickSubmit = onClickSubmit;
    }

    public interface OnWheelClickSubmit {
        void onClickSubmit();
    }
}
//    private ArrayList<String> years = new ArrayList<>();
//    private ArrayList<String> days28= new ArrayList<>();
//    private ArrayList<String> days29= new ArrayList<>();
//    private  ArrayList<String> days30= new ArrayList<>();
//    private  ArrayList<String> days31 = new ArrayList<>();
//    private  ArrayList<String> months = new ArrayList<>();
//    private  ArrayList<ArrayList<String>> month = new ArrayList<>();
//    private  ArrayList<ArrayList<ArrayList<String>>> ymonths = new ArrayList<>();
//    ArrayList<String> type = new ArrayList<>();
//

    //
//    public void initDatas() {
//        if ("1".equals(tag)) {
//            type.add("上午");
//            type.add("下午");
//        }else{
//            type.add("上学");
//            type.add("放学");
//        }
//
//        for (int i = 2016 ; i < 2019 ; i ++){
//            years.add(i+"");
//        }
//        for (int i = 1 ; i <= 31 ; i ++){
//            days31.add(i+"");
//        }
//        for (int i = 1 ; i <= 30 ; i ++){
//            days30.add(i+"");
//        }
//        for (int i = 1 ; i <= 29 ; i ++){
//            days29.add(i+"");
//        }
//        for (int i = 1 ; i <= 28 ; i ++){
//            days28.add(i+"");
//        }
//        for (int i = 1 ; i <= 12 ; i ++){
//            months.add(i+"");
//        }
//        for (int i = 0 ; i < years.size() ; i++){
//            month.add(months);
//            ArrayList<ArrayList<String>> month = new ArrayList<>();
//            month.add(days31);
//            int year = Integer.parseInt(years.get(i));
//            if (year % 4== 0 && year % 100 != 0 || year % 400 == 0){
//                month.add(days29);
//            }else {
//                month.add(days28);
//            }
//            month.add(days31);
//            month.add(days30);
//            month.add(days31);
//            month.add(days30);
//            month.add(days31);
//            month.add(days31);
//            month.add(days30);
//            month.add(days31);
//            month.add(days30);
//            month.add(days31);
//            ymonths.add(month);
//        }
//    }
//


//    public String historyYear = "";
//    public String historyMonth = "";
//    public String historyDay = "";
//    private int index1 = 0;
//    private int index2 = 0;
//    private int index3 = 0;
//    private int index4 = 0;
//    public void selectData(Context context) {
////          index1 = 0;
////          index2 = 0;
////          index3 = 0;
////          index4 = 0;
//        OptionsPickerView optionsPickerView = new OptionsPickerView(context);
//        optionsPickerView.setPicker(years,month,ymonths,type,index1,index2,index3,index4,true);
//        optionsPickerView.setOnoptionsSubmitListener(new OptionsPickerView.OnOptionsSubmitListener() {
//            @Override
//            public void onOptionsSubmit() {
//                mOnClickSubmit.onClickSubmit();
//            }
//        });
//        optionsPickerView.setCyclic(false);
//        optionsPickerView.setCancelable(true);
//        optionsPickerView.setLabels("年","月","日");
//        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2,int options3,int options4) {
//                historyYear = years.get(options1);
//                if (Integer.parseInt(month.get(options1).get(options2)) < 10) {
//                    historyMonth = "0" + month.get(options1).get(options2);
//                }else{
//                    historyMonth = month.get(options1).get(options2);
//                }
//                if (Integer.parseInt(ymonths.get(options1).get(options2).get(options3)) < 10) {
//                    historyDay = "0" + ymonths.get(options1).get(options2).get(options3);
//                }else{
//                    historyDay = ymonths.get(options1).get(options2).get(options3);
//                }
//                historyDateInt = historyYear+historyMonth+historyDay;
//                historyDate = historyYear +"-"+ historyMonth +"-"+ historyDay;
//                flag = options4 + "";
//                LogUtil.e("时间工具类","historyDate：" + historyDate + "---" + flag);
//                index1 = options1;
//                index2 = options2;
//                index3 = options3;
//                index4 = options4;
////                mOnClickSubmit.onClickSubmit();
//            }
//        });
//        optionsPickerView.show();
//    }
