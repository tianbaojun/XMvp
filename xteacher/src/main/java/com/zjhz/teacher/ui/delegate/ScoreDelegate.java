package com.zjhz.teacher.ui.delegate;

import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.ui.activity.ScoreActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
public class ScoreDelegate implements View.OnClickListener {
    int index_clazz;
    private ScoreActivity activity;
    private DrawerLayout scoreDrawerlayout;
    private LinearLayout score_drawer_layout;
    private TextView years_tvs;
    private TextView termTv;
    private TextView score_type_tv;
    private TextView subject_name;
    private TextView class_tv;
    private TextView drawer_layout_date_start;
    private TextView drawer_layout_date_end;
    private String yearId = "";
    private String termId = "";
    private String soceId = "";
    private String subId = "";
    private int index_grade;

    private String classId = "";
    public ScoreDelegate( ScoreActivity activity) {
        this.activity = activity;
    }

    public void initDelegate() {
        scoreDrawerlayout = (DrawerLayout) activity.findViewById(R.id.score_drawerlayout);
        score_drawer_layout = (LinearLayout) activity.findViewById(R.id.score_drawer_layout);
        years_tvs = (TextView) activity.findViewById(R.id.years_tvs);
        termTv = (TextView) activity.findViewById(R.id.term_tvs);
        score_type_tv = (TextView) activity.findViewById(R.id.score_type_tv);
        subject_name = (TextView) activity.findViewById(R.id.subject_name);
        class_tv = (TextView) activity.findViewById(R.id.class_tv);
        drawer_layout_date_start = (TextView) activity.findViewById(R.id.drawer_layout_date_start);
        drawer_layout_date_end = (TextView) activity.findViewById(R.id.drawer_layout_date_end);
        drawer_layout_date_start.setOnClickListener(this);
        drawer_layout_date_end.setOnClickListener(this);
        activity.findViewById(R.id.navigation_header_left).setOnClickListener(this);
        activity.findViewById(R.id.navigation_header_right).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_term).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_score).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_class).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_years).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.navigation_header_left:
                closeDrawLayout();
//                clearData();
                break;
            case R.id.navigation_header_right:
                if (isRequest()){
                    String startTime = drawer_layout_date_start.getText().toString();
                    String endTime = drawer_layout_date_end.getText().toString();
                    activity.yearId = yearId;
                    activity.termId = termId;
                    activity.soceId = soceId;
                    activity.subId = subId;
                    activity.classId = classId;
                    if(!TextUtils.isEmpty(startTime)) {
                        activity.startTime = startTime + " 00:00:00";
                    }else{
                        activity.startTime = "";
                    }
                    if (!TextUtils.isEmpty(endTime)) {
                        activity.endTime = endTime+" 59:59:59";
                    } else {
                        activity.endTime = "";
                    }
                    activity.page = 1;
                    activity.isFilter = true;
                    activity.dialog.setMessage("正在获取数据...");
                    activity.dialog.show();
                    activity.getList();
                    closeDrawLayout();
//                    clearData();
                }
                break;
            case R.id.drawer_layout_clear:
                clearData();
                activity.onRefresh();
                break;
            case R.id.drawer_layout_date_start:
                selectDate(drawer_layout_date_start);
                break;
            case R.id.drawer_layout_date_end:
                selectDate(drawer_layout_date_end);
                break;
            case R.id.drawer_layout_years:
                selectSingle(activity.yearStr,years_tvs,IntType.ONE);
                break;
            case R.id.drawer_layout_term:
                selectSingle(activity.tremStr,termTv,IntType.TWO);
                break;
            case R.id.drawer_layout_score:
                selectSingle(activity.scoreStr,score_type_tv,IntType.THREE);
                break;
            case R.id.drawer_layout_subject:
                selectSingle(activity.subject,subject_name,IntType.FOUR);
                break;
            case R.id.drawer_layout_class:
                selectClasses();
                break;
        }
    }

    private boolean isRequest() {
        int i = 0;
        if (SharePreCache.isEmpty(years_tvs.getText().toString().trim())){
//            ToastUtils.showShort("请选择学年");
            i++;
        }
        if (SharePreCache.isEmpty(termTv.getText().toString().trim())){
//            ToastUtils.showShort("请选择学期");
            i++;
        }
        if (SharePreCache.isEmpty(score_type_tv.getText().toString().trim())){
//            ToastUtils.showShort("请选择成绩类型");
            i++;
        }
        if (SharePreCache.isEmpty(subject_name.getText().toString().trim())){
//            ToastUtils.showShort("请选择科目");
            i++;
        }
        if (SharePreCache.isEmpty(class_tv.getText().toString().trim())){
//            ToastUtils.showShort("请选择班级");
            i++;
        }
        if (SharePreCache.isEmpty(drawer_layout_date_start.getText().toString().trim())){
//            ToastUtils.showShort("请选择起始时间");
            i++;
        }
        if (SharePreCache.isEmpty(drawer_layout_date_end.getText().toString().trim())){
//            ToastUtils.showShort("请选择结束时间");
            i++;
        }
        if(i>0) {
            return true;
        }else{
            ToastUtils.showShort("请选择条件");
            return false;
        }
    }

    private void clearData() {
        activity.yearId = "";
        activity.termId = "";
        activity.soceId = "";
        activity.subId = "";
        activity.classId = "";
        activity.startTime = "";
        activity.endTime = "";

        yearId = "";
        termId = "";
        soceId = "";
        subId = "";
        classId = "";
        years_tvs.setText(" ");
        termTv.setText(" ");
        score_type_tv.setText(" ");
        subject_name.setText(" ");
        class_tv.setText(" ");
        drawer_layout_date_start.setText("");
        drawer_layout_date_end.setText("");
        drawer_layout_date_start.setHint("请选择起始时间");
        drawer_layout_date_end.setHint("请选择截止时间");
    }

    private void  closeDrawLayout(){
        scoreDrawerlayout.closeDrawer(score_drawer_layout);
    }

    public void openDrawLayout() {
        scoreDrawerlayout.openDrawer(score_drawer_layout);
    }
    /**
     * 选择日期
     * @param tv
     */
    private void selectDate(final TextView tv) {
        TimePickerView pvTime = new TimePickerView(activity, TimePickerView.Type.YEAR_MONTH_DAY,null,0);
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
            public void onTimeSelect(Date date,String type) {
                tv.setText(TimeUtil.getYMD(date));
            }
        });
    }

    /**
     * 选择班级
     */
    private void selectClasses() {
        if (AppContext.getInstance().gradeBeans != null && AppContext.getInstance().gradeBeans.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker(AppContext.getInstance().grades,AppContext.getInstance().clazzs, index_grade, index_clazz, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    class_tv.setText(AppContext.getInstance().gradeBeans.get(options1).getDetail().get(option2).getName());
                    index_grade = options1;
                    index_clazz = option2;
                    classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }

    /**
     * 选择科目，成绩类型
     * @param arrayList
     * @param tv
     */
    private void selectSingle(final ArrayList<String> arrayList, final TextView tv, final IntType from) {
        if (arrayList != null && arrayList.size() > 0){
            OptionsPickerView  optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker(arrayList, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    tv.setText(arrayList.get(options1));
                    switch (from){
                        case ONE:
                            yearId = activity.yearBean.get(options1).getParamKey();
                            break;
                        case TWO:
                            termId = activity.tremBean.get(options1).getParamKey();
                            break;
                        case THREE:
                            soceId = activity.scoreBean.get(options1).getParamKey();
                            break;
                        case FOUR:
                            subId = activity.subObj.get(options1).getSubjectId();
                            break;
                    }
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }
    public enum IntType {
        ONE,TWO,THREE,FOUR
    }
}
