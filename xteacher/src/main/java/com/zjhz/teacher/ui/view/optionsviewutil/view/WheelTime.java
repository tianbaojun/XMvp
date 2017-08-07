package com.zjhz.teacher.ui.view.optionsviewutil.view;

import android.content.Context;
import android.view.View;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.ui.view.optionsviewutil.adapter.ArrayWheelAdapter;
import com.zjhz.teacher.ui.view.optionsviewutil.adapter.NumericWheelAdapter;
import com.zjhz.teacher.ui.view.optionsviewutil.lib.WheelView;
import com.zjhz.teacher.ui.view.optionsviewutil.listener.OnItemSelectedListener;
import com.zjhz.teacher.utils.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WheelTime {
    public static final int DEFULT_START_YEAR = 1990;
    public static final int DEFULT_END_YEAR = 2100;
    public static DateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    String[] months_big;
    String[] months_little;
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    private WheelView typewl;
    private ArrayList<String> typeString = new ArrayList<>();
    private int mType;
    private TimePickerView.Type type;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    private int current_month;
    private int current_day;
    private int currentType;


    public WheelTime(View view) {
        super();
        this.view = view;
        type = TimePickerView.Type.ALL;
        setView(view);
    }

    public WheelTime(View view, TimePickerView.Type type, ArrayList<String> typeStrings, int mType) {
        super();
        this.view = view;
        this.type = type;
        this.typeString = typeStrings;
        this.mType = mType;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
        if (currentType < typewl.getItemsCount()) {
            typewl.setCurrentItem(currentType);
        }
    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void setPicker(int year, int month, int day, int h, int m) {

        Calendar c = Calendar.getInstance();
        current_month = c.get(Calendar.MONTH) + 1;
        current_day = c.get(Calendar.DATE);

        LogUtil.e("当前时间月" + current_month + "日：" + current_day);
        if (mType != ConstantKey.chooseYesterday) {
            // 添加大小月月份并将其转换为list,方便之后的判断
            months_big = new String[]{"1", "3", "5", "7", "8", "10", "12"};
            months_little = new String[]{"4", "6", "9", "11"};
        } else {
            handlerMonths();
        }
        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        if (mType != ConstantKey.chooseYesterday) {
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        } else {
            wv_month.setAdapter(new NumericWheelAdapter(1, current_month));
        }
        wv_month.setLabel(context.getString(R.string.pickerview_month));
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            if (mType != ConstantKey.chooseYesterday) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else {
                wv_day.setAdapter(new NumericWheelAdapter(1, current_day - 1));
            }
        } else if (list_little.contains(String.valueOf(month + 1))) {
            if (mType != ConstantKey.chooseYesterday) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                wv_day.setAdapter(new NumericWheelAdapter(1, current_day - 1));
            }
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                if (mType != ConstantKey.chooseYesterday) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, current_day - 1));
                }
            } else {
                if (mType != ConstantKey.chooseYesterday) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, current_day - 1));
                }
            }
        }
        wv_day.setLabel(context.getString(R.string.pickerview_day));
        if (mType != ConstantKey.chooseYesterday) {
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setCurrentItem(current_day - 1);
        }


        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_mins.setCurrentItem(m);

        typewl = (WheelView) view.findViewById(R.id.typewl);
        if (typeString != null) {
            if (mType == 1) {
                typewl.setAdapter(new ArrayWheelAdapter(typeString, 4));
                typewl.setCurrentItem(0);
            } else if (mType == ConstantKey.chooseMonth) {
                typewl.setAdapter(new ArrayWheelAdapter(typeString, 12));
                typewl.setLabel(context.getString(R.string.pickerview_month));
                typewl.setCurrentItem(month);
            } else if (mType == ConstantKey.chooseWeek) {
                typewl.setAdapter(new ArrayWheelAdapter(typeString, typeString.size()));
                typewl.setLabel(
                        context.getString(R.string.school_calendar_manager_week));
                typewl.setCurrentItem(0);
            }
        }
        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                int maxItem = 30;
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;
                int maxItem = 30;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                            .getCurrentItem() + startYear) % 100 != 0)
                            || (wv_year.getCurrentItem() + startYear) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 8;
        switch (type) {
            case ALL:
                textSize = textSize * 2;
                typewl.setVisibility(View.GONE);
                break;
            case YEAR_MONTH_DAY:
                textSize = textSize * 2;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                typewl.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                textSize = textSize * 2;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                typewl.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
                textSize = textSize * 2;
                wv_year.setVisibility(View.GONE);
                typewl.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                textSize = textSize * 2;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                typewl.setVisibility(View.GONE);
                break;
            case YEAR_MONTH_DAY_TYPE:
                textSize = textSize * 2;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case ONLY_ONE:
                textSize = textSize * 2;
                wv_year.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                typewl.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                typewl.setVisibility(View.VISIBLE);
                break;
            case YEAR_MONTH_DAY_HOUR_MIN:
                textSize = (int) (textSize * 1.6f);
                typewl.setVisibility(View.GONE);
                break;
        }
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);
        typewl.setTextSize(textSize);
    }

    private void handlerMonths() {
        if (current_month == 9) {
            months_big = new String[]{"1", "3", "5", "7", "8"};
            months_little = new String[]{"4", "6", "9"};
        } else if (current_month == 10) {
            months_big = new String[]{"1", "3", "5", "7", "8", "10"};
            months_little = new String[]{"4", "6", "9"};
        } else if (current_month == 11) {
            months_big = new String[]{"1", "3", "5", "7", "8", "10"};
            months_little = new String[]{"4", "6", "9", "11"};
        } else if (current_month == 12) {
            months_big = new String[]{"1", "3", "5", "7", "8", "10", "12"};
            months_little = new String[]{"4", "6", "9", "11"};
        } else if (current_month == 1) {
            months_big = new String[]{"1"};
            months_little = new String[]{};
        } else if (current_month == 2) {
            months_big = new String[]{"1"};
            months_little = new String[]{};
        } else if (current_month == 3) {
            months_big = new String[]{"1", "3"};
            months_little = new String[]{};
        } else if (current_month == 4) {
            months_big = new String[]{"1", "3"};
            months_little = new String[]{"4"};
        } else if (current_month == 5) {
            months_big = new String[]{"1", "3", "5"};
            months_little = new String[]{"4"};
        } else if (current_month == 6) {
            months_big = new String[]{"1", "3", "5"};
            months_little = new String[]{"4", "6"};
        } else if (current_month == 7) {
            months_big = new String[]{"1", "3", "5", "7"};
            months_little = new String[]{"4", "6"};
        } else if (current_month == 8) {
            months_big = new String[]{"1", "3", "5", "7", "8"};
            months_little = new String[]{"4", "6"};
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
        typewl.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1)).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem()).append(" ");
        return sb.toString();
    }

    public String getType() {
        return typewl.getCurrentItem() + "";
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
