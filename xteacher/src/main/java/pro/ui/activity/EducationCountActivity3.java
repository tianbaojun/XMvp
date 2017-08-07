package pro.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.EduCountClassTimeParam;
import com.zjhz.teacher.NetworkRequests.request.EduCountTermParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.EduClassTimeCountAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClassMoral;
import com.zjhz.teacher.bean.EduClassTimeCount;
import com.zjhz.teacher.bean.IndividualMoral;
import com.zjhz.teacher.bean.MonthOfTerm;
import com.zjhz.teacher.bean.MonthWeekOfTerm;
import com.zjhz.teacher.bean.WeekOfTerm;
import com.zjhz.teacher.ui.activity.MainActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**
 * Created by Tabjin on 2017/4/7.
 * 以班级为查询条件的德育统计
 */

public class EducationCountActivity3 extends BaseActivity {
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    //标题
    @BindView(R.id.title_tv)
    TextView titleTv;
    //返回
    @BindView(R.id.title_back_img)
    TextView title_back_img;

    //数据视图，没数据时隐藏
    @BindView(R.id.source)
    ScrollView source;
    //班级选择
    @BindView(R.id.edu_choose_class)
    TextView edu_choose_class;
    //周次选择
    @BindView(R.id.edu_choose_week)
    TextView edu_choose_week;
    //月份选择
    @BindView(R.id.edu_choose_month)
    TextView edu_choose_month;
    //开始日期选择
    @BindView(R.id.edu_choose_start_date)
    TextView edu_choose_start_date;
    //结束日期选择
    @BindView(R.id.edu_choose_end_date)
    TextView edu_choose_end_date;
    //班级总分
    @BindView(R.id.show_class)
    TextView show_class;
    //班级详情列表
    @BindView(R.id.classz_listview)
    ScrollViewWithListView classz_listview;
    //个人总分
    @BindView(R.id.show_person)
    TextView show_person;
    //个人详情列表
    @BindView(R.id.person_listview)
    ScrollViewWithListView person_listview;
    //总分
    @BindView(R.id.show_total)
    TextView show_total;

    //返回的月份周次信息
    private MonthWeekOfTerm monthWeekOfTerm;
    //月份信息
    private List<MonthOfTerm> monthOfTerms;
    //周次信息
    private List<WeekOfTerm> weekOfTerms;
    //德育信息
    private EduClassTimeCount count;
    //个人德育详情列表
    private List<IndividualMoral> individualMorals = new ArrayList<>();
    //个人列表adapter
    private EduClassTimeCountAdapter<IndividualMoral> individualAdapter;
    //班级德育详情列表
    private List<ClassMoral> classMorals = new ArrayList<>();
    //班级列表adapter
    private EduClassTimeCountAdapter<ClassMoral> classAdapter;

    //月份请求参数
    private EduCountTermParams termParams = new EduCountTermParams("1");
    //德育信息请求参数
    private EduCountClassTimeParam countParams;
    //请求的班级id
    private String classzId = "";
    //请求的开始结束时间
    private String startTime = "", endTime = "";

    //是否展示班级选择框
    private boolean isShowClassPicker = false;
    //是否展示周次选择框
    private boolean isShowWeekPicker = false;
    //是否展示月份选择框
    private boolean isShowMonthPicker = false;


    private DefaultLoadingLayout loadingLayout;

    private final static String TAG = EducationCountActivity3.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_count_three);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, source);
        titleTv.setText(getIntent().getExtras().getString("title"));
        //默认空数据
        loadingLayout.onEmpty();
        //获取年级信息
        MainActivity.getClassz();
        //获取月份周次信息
        getMonthOfTerm();
        individualAdapter = new EduClassTimeCountAdapter<>(individualMorals, this);
        classAdapter = new EduClassTimeCountAdapter<>(classMorals, this);
        person_listview.setAdapter(individualAdapter);
        classz_listview.setAdapter(classAdapter);
        ViewTools.tvAppendImage(edu_choose_week,getString(R.string.education_choose_weeks),R.mipmap.edu_count_week);
        ViewTools.tvAppendImage(edu_choose_month,getString(R.string.education_choose_month),R.mipmap.edu_count_month);
        ViewTools.tvAppendImage(edu_choose_class,getString(R.string.education_choose_class),R.mipmap.edu_count_class);
    }

    @OnClick({R.id.edu_choose_class, R.id.edu_choose_week, R.id.edu_choose_month, R.id.edu_choose_start_date,R.id.edu_choose_end_date, R.id.btn_search, R.id.title_back_img})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.edu_choose_class:  //选择班级
                selectGradeCls();
                break;
            case R.id.edu_choose_week://选择周次
                selectWeek();
                break;
            case R.id.edu_choose_month://选择月
                selectMonth();
                break;
            case R.id.edu_choose_start_date://选择开始日期
                selectDate((TextView) v,0);
                break;
            case R.id.edu_choose_end_date://选择开始日期
                selectDate((TextView) v,1);
                break;
            case R.id.btn_search: //提交
                submit();
                break;
            case R.id.title_back_img:      //返回
                finish();
                break;
        }
    }

    /**
     * 提交的处理，判断是否可以提交
     */
    private void submit() {
        if (classzId.equals("")) {
            ToastUtils.showShort(R.string.education_choose_class);
            return;
        } else if (startTime.equals("") || endTime.equals("")) {
            ToastUtils.toast("请选择要查询的时间");
        } else {
            countParams = new EduCountClassTimeParam(startTime, endTime, classzId);
            getEduCount();
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "noSuccess":
                break;
            /*case Config.gradeClsList:  //获取年级信息
                grades.clear();
                grades = toArray(Grade.class, (JSONObject) event.getData());
                if (null == grades || grades.size() == 0) {
                    ToastUtils.showShort(R.string.grade_data_not_found);
                } else {
//                    classNames = getGradeClassName(grades).get("classNames");
//                    gradeNames = getGradeClassName(grades).get("gradeNames");
                    getGradeClassName(grades);
                    if (isShowClassPicker) {
                        selectGradeCls();
                        isShowClassPicker = false;
                    }
                }
                break;*/
            case Config.EDUCOUNTGETALLTERM:  //学期月份周次
                JSONObject o = (JSONObject) event.getData();
                monthWeekOfTerm = GsonUtils.toObject(o.optString("data"), MonthWeekOfTerm.class);
                if (monthWeekOfTerm != null) {
                    monthOfTerms = monthWeekOfTerm.getMonthBetween();
                    weekOfTerms = monthWeekOfTerm.getWeek();
                    if (isShowWeekPicker) { //显示周次选择
                        selectWeek();
                        isShowWeekPicker = false;
                    }
                    if (isShowMonthPicker) { //显示月份选择
                        selectMonth();
                        isShowMonthPicker = false;
                    }
                }
                break;
            case Config.EDUCOUNTGETCOUNT:
                JSONObject o2 = (JSONObject) event.getData();
                count = GsonUtils.toObject(o2.optString("data"), EduClassTimeCount.class);
                if (count != null) {
                    loadingLayout.onDone();
                    show_person.setText(count.getIndividualMoralTotal());
                    show_class.setText(count.getClassMoralTotal());
                    show_total.setText(count.getTotal());
                    classMorals.clear();
                    classMorals.addAll(count.getClassMoral());
                    individualMorals.clear();
                    individualMorals.addAll(count.getIndividualMoral());
                    individualAdapter.notifyDataSetChanged();
                    classAdapter.notifyDataSetChanged();
                } else {
                    loadingLayout.onEmpty();
                }
                break;

        }

    }

   /* *//**
     * 获取班级列表
     *//*
    private void getClassz() {
        NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsList);
        dialog.show();
    }
*/
    /**
     * 获取该学期月份和周次
     */
    private void getMonthOfTerm() {
        NetworkRequest.request(null, CommonUrl.EDUCOUNTGETALLTERM, Config.EDUCOUNTGETALLTERM);
        dialog.show();
    }

    /**
     * 获取该班级的德育信息
     */
    private void getEduCount() {
        NetworkRequest.request(countParams, CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
        dialog.show();
    }

   /* *//**
     * 将年级列表装换成年级班级名字的 string 列表
     *
     * @param grades
     * @return
     *//*
    private void getGradeClassName(List<Grade> grades) {
//        Map<String, ArrayList> map = new HashMap<>();
        ArrayList<ArrayList<String>> gradeClassNames = new ArrayList<>();
        ArrayList<String> gradeNames = new ArrayList<>();
        if (grades != null && !grades.isEmpty()) {
            for (Grade grade : grades) {
                gradeNames.add(grade.getName());
                List<Classz> classzs = grade.getDetail();
                ArrayList<String> classNames = new ArrayList<>();
                if(classzs!=null&&classzs.size()>0) {
                    for (Classz classz : classzs) {
                        classNames.add(classz.getName());
                    }
                }
                gradeClassNames.add(classNames);
            }
            classNames = gradeClassNames;
            this.gradeNames = gradeNames;*//*
            map.put("classNames", gradeClassNames);
            map.put("gradeNames", gradeNames);
            return map;*//*
        }
//        return null;
    }*/

    /**
     * 判断年级数据是否正常
     *
     * @return
     */
   /* private boolean isGradeNotNull() {
        if (grades == null) {
            return false;
        }
        if (grades.size() < 1) {
            return false;
        }
        if (gradeNames == null || gradeNames.size() < 1) {
            return false;
        }
        if (classNames == null || classNames.size() < 1) {
            return false;
        }
        return true;
    }*/

    /**
     * 选择班级
     */
    private void selectGradeCls() {
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().grades, AppContext.getInstance().clazzs, 0, 0, 0);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                if (AppContext.getInstance().clazzs.get(options1) != null &&
                        AppContext.getInstance().clazzs.get(options1).size() > 0 &&
                        options2 < AppContext.getInstance().clazzs.get(options1).size()) {
                    edu_choose_class.setText(AppContext.getInstance().clazzs.get(options1).get(options2));
                    classzId = AppContext.getInstance().gradeBeans.get(options1).getDetail().get(options2).getClassId();
                } else {
                    edu_choose_class.setText("");
                    ToastUtils.showShort("班级为空，不能选择");
                }
            }
        });
        optionsPickerView.show();
    }


    /**
     * 选择周次
     */
    private void selectWeek() {

        if (weekOfTerms != null && weekOfTerms.size() > 0) {
            final ArrayList<String> names = new ArrayList<>();
            for (WeekOfTerm week : weekOfTerms) {
                names.add(week.getWeek());
            }
            final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker(names, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                    edu_choose_week.setText(names.get(options1));
                    edu_choose_month.setText(R.string.education_choose_month);
                    edu_choose_start_date.setText(R.string.education_choose_start_date);
                    edu_choose_end_date.setText(R.string.education_choose_end_date);
                    startTime = weekOfTerms.get(options1).getStartTime();
                    endTime = weekOfTerms.get(options1).getEndTime();
                }
            });
            optionsPickerView.show();
        } else {
            getMonthOfTerm();
            dialog.show();
            isShowWeekPicker = true;
        }

    }


    /**
     * 选择月份
     */
    private void selectMonth() {
        if (monthOfTerms != null && monthOfTerms.size() > 0) {
            final ArrayList<String> names = new ArrayList<>();
            for (MonthOfTerm week : monthOfTerms) {
                names.add(week.getTitle());
            }
            final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker(names, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                    edu_choose_month.setText(names.get(options1));
                    edu_choose_week.setText(R.string.education_choose_weeks);
                    edu_choose_start_date.setText(R.string.education_choose_start_date);
                    edu_choose_end_date.setText(R.string.education_choose_end_date);
                    startTime = monthOfTerms.get(options1).getStartTime();
                    endTime = monthOfTerms.get(options1).getEndTime();
                }
            });
            optionsPickerView.show();
        } else {
            getMonthOfTerm();
            dialog.show();
            isShowMonthPicker = true;
        }
    }

    /**
     * 选择日期
     */
    private void selectDate(final TextView tv, final int code) {
        TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY, null, 0);
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        pvTime.show();
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, String type) {
                tv.setText(TimeUtil.getYMD(date));
                edu_choose_week.setText(R.string.education_choose_weeks);
                edu_choose_month.setText(R.string.education_choose_month);
                if(code == 0) {
                    startTime = TimeUtil.getYMD(date) + " 00:00:00";
                }else if(code == 1){
                    endTime =  TimeUtil.getYMD(date) + " 23:59:59";
                }
            }
        });
    }


}
