package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.StatisticsOfMoralEducationListBean;
import com.zjhz.teacher.ui.adapter.StatisticsOfMoralEducationAdapter;
import com.zjhz.teacher.ui.view.OptionsGradeView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by order on 2016/9/23.
 */
public class StatisticsOfMoralEducationMainActivity extends BaseActivity {
    private final static String TAG = StatisticsOfMoralEducationMainActivity.class.getSimpleName();
//    public ArrayList<String> grades = new ArrayList<>();
//    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();
//    public ArrayList<StudentsCurrentPositionClassGrade> clazzsGrades = new ArrayList<>();
    @BindView(R.id.activity_statistics_of_moraleducation_class)
    public TextView Class;
    @BindView(R.id.activity_statistics_of_moraleducation_person)
    public TextView Person;
    public int index_grade;
    public int index_clazz;
    @BindView(R.id.activity_statistics_of_moraleducation_date)
    TextView Date;
    @BindView(R.id.activity_statistics_of_moraleducation_week)
    TextView Week;
    @BindView(R.id.activity_statistics_of_moraleducation_month)
    TextView Month;
    @BindView(R.id.activity_statistics_of_moraleducation_style)
    TextView TimeStyle;
    @BindView(R.id.activity_statistics_of_moraleducation_date_start)
    TextView DateStart;
    @BindView(R.id.activity_statistics_of_moraleducation_date_end)
    TextView DateEnd;
    @BindView(R.id.activity_statistics_of_moraleducation_date_choose)
    LinearLayout DateChoosell;
    @BindView(R.id.ranking_of_the_whole_school)
    TextView rankingOfTheWholeSchool;
    @BindView(R.id.ranking_of_the_grade)
    TextView rankingOfTheGrade;
    @BindView(R.id.the_total_score)
    TextView theTotalScore;
    @BindView(R.id.activity_statistics_of_moraleducation_content_class_list)
    ListView ClassList;
    @BindView(R.id.activity_statistics_of_moraleducation_content_person_list)
    ListView PersonList;
    OptionsGradeView mOptionsGradeView;
    StatisticsOfMoralEducationAdapter adapter;
    private TextView titleTv;
    private TextView title_back_img;
    private TextView right_text;
    private String gradeId = "";
    private List<StatisticsOfMoralEducationListBean> datas = new ArrayList<>();
    private boolean isStyle = true;
    private ArrayList<String> MonthS = new ArrayList<>();
    private ArrayList<String> WeekS = new ArrayList<>();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_of_moraleducation_main);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initView();
        initData();
    }

    private void initData() {
//        dialog.show();
        //获取所有班级年级
//        NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsList);
        MainActivity.getClassz();
        for (int i = 0; i < 5; i++) {
            StatisticsOfMoralEducationListBean b = new StatisticsOfMoralEducationListBean();
            b.name = i + "姓名";
            b.score = i + "00";
            datas.add(b);
        }
        adapter = new StatisticsOfMoralEducationAdapter(datas, this, 0);
        ClassList.setAdapter(adapter);
        ClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StatisticsOfMoralEducationMainActivity.this, StatisticsOfMoralEducationClassActivity.class);
                startActivity(intent);
            }
        });


        adapter = new StatisticsOfMoralEducationAdapter(datas, this, 1);
        PersonList.setAdapter(adapter);
        PersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StatisticsOfMoralEducationMainActivity.this, StatisticsOfMoralEducationPersonActivity.class);
                startActivity(intent);
            }
        });
        initMouthDatas();
        initWeekDatas();
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText("德育统计");
        title_back_img = (TextView) findViewById(R.id.title_back_img);
        title_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_text = (TextView) findViewById(R.id.right_text);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText("请选择班级");
        mOptionsGradeView = new OptionsGradeView(this);

    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.toast("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            /*case Config.gradeClsList:
                dialog.dismiss();
                JSONObject json = (JSONObject) event.getData();
                if (json != null) {
                    JSONArray data = json.optJSONArray("data");
                    AppContext.getInstance().grades.clear();
                    AppContext.getInstance().clazzs.clear();
                    AppContext.getInstance().clazzsGrades.clear();
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                if (o != null) {
                                    StudentsCurrentPositionClassGrade mStudentsCurrentPositionClassGrade = new StudentsCurrentPositionClassGrade();
                                    mStudentsCurrentPositionClassGrade.gradeId = o.optString("gradeId");
                                    mStudentsCurrentPositionClassGrade.gradeName = o.optString("name");
                                    grades.add(o.optString("name"));
                                    AppContext.getInstance().grades.add(o.optString("name"));
                                    JSONArray detail = o.optJSONArray("detail");
                                    ArrayList<String> strings = new ArrayList<>();
                                    if (detail != null && detail.length() > 0) {
                                        for (int j = 0; j < detail.length(); j++) {
                                            JSONObject o1 = (JSONObject) detail.get(j);
                                            if (o1 != null) {
                                                StudentsCurrentPositionClass mStudentsCurrentPositionClass = new StudentsCurrentPositionClass();
                                                mStudentsCurrentPositionClass.clazzId = o1.optString("classId");
                                                mStudentsCurrentPositionClass.clazzName = o1.optString("name");
                                                mStudentsCurrentPositionClassGrade.clazzs.add(mStudentsCurrentPositionClass);
                                                if (!TextUtils.isEmpty(o1.optString("name"))) {
                                                    strings.add(o1.optString("name"));
                                                }
                                            }
                                        }
                                    } else {
                                        strings.add(" ");
                                    }
                                    clazzs.add(strings);
                                    AppContext.getInstance().clazzs.add(strings);
                                    clazzsGrades.add(mStudentsCurrentPositionClassGrade);
                                    AppContext.getInstance().clazzsGrades.add(mStudentsCurrentPositionClassGrade);
//                                    handlerTextWithHeight();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.e("班级年级", "班级 = " + clazzs.size() + "年级 = " + grades.size() + clazzs.toString());
                }
                break;*/
        }
    }

    @OnClick({R.id.activity_statistics_of_moraleducation_date, R.id.activity_statistics_of_moraleducation_week, R.id.activity_statistics_of_moraleducation_month, R.id.activity_statistics_of_moraleducation_style, R.id.activity_statistics_of_moraleducation_date_start, R.id.activity_statistics_of_moraleducation_date_end, R.id.right_text})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.activity_statistics_of_moraleducation_date:
                DateChoosell.setVisibility(View.GONE);
                isStyle = true;
                selectDate(this, Date);
                break;
            case R.id.activity_statistics_of_moraleducation_week:
                DateChoosell.setVisibility(View.GONE);
                isStyle = true;
                selectWeek(this, Week);
                break;
            case R.id.activity_statistics_of_moraleducation_month:
                DateChoosell.setVisibility(View.GONE);
                isStyle = true;
                selectMouth(this, Month);
                break;
            case R.id.activity_statistics_of_moraleducation_style:
                if (isStyle) {
                    DateChoosell.setVisibility(View.VISIBLE);
                    isStyle = false;
                } else {
                    DateChoosell.setVisibility(View.GONE);
                    isStyle = true;
                }
                break;
            case R.id.activity_statistics_of_moraleducation_date_start:
                BaseUtil.selectDate(this, DateStart);
                break;
            case R.id.activity_statistics_of_moraleducation_date_end:
                BaseUtil.selectDate(this, DateEnd);
                break;
            case R.id.right_text:
                selectGrade();
                break;
        }
    }

    /**
     * 选择班级
     */
    public void selectGrade() {
        if (!AppContext.getInstance().gradeBeans.isEmpty() && !AppContext.getInstance().clazzs.isEmpty()) {
            mOptionsGradeView.setPicker((ArrayList) AppContext.getInstance().gradeBeans, AppContext.getInstance().clazzs, index_grade, index_clazz, 0);
            mOptionsGradeView.setCyclic(false);
            mOptionsGradeView.setCancelable(true);
            mOptionsGradeView.setOnoptionsSelectListener(new OptionsGradeView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    if (AppContext.getInstance().clazzs.get(options1).size() > 0 && option2 < AppContext.getInstance().clazzs.get(options1).size()) {
                        right_text.setText(AppContext.getInstance().clazzs.get(options1).get(option2));
                        index_grade = options1;
                        index_clazz = option2;
                        LogUtil.e("index_grade = " + index_grade + " index_clazz = " + index_clazz);
                        String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
                        String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
                        LogUtil.e("年级IDindex_grade = " + index_grade + " 班级IDindex_clazz = " + index_clazz);
                        LogUtil.e("年级ID = " + gradeId + " 班级ID = " + classId);
                    } else {
                        right_text.setText("请选择班级");
                        index_grade = 0;
                        index_clazz = 0;
                        ToastUtils.showShort("班级为空，重新选择");
                    }
                }
            });
            mOptionsGradeView.show();
        } else {
            ToastUtils.toast("没有数据");
        }
    }

    /**
     * 选择昨天以前的日期
     */
    public void selectDate(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY, null, ConstantKey.chooseYesterday);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, String type) {
                textView.setText(TimeUtil.getYMDSKEW(date));
            }
        });
    }

    /**
     * 选择月份
     */
    public void selectMouth(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.ONLY_ONE, MonthS, ConstantKey.chooseMonth);
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, String type) {
                textView.setText(Integer.parseInt(type) + 1 + "");

            }
        });
    }

    /**
     * 选择周次
     */
    public void selectWeek(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.ONLY_ONE, MonthS, ConstantKey.chooseWeek);
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, String type) {
                textView.setText(Integer.parseInt(type) + 1 + "");

            }
        });
    }

    public void initMouthDatas() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        for (int i = 1; i < month + 2; i++) {
            MonthS.add(i + "");
        }
    }

    public void initWeekDatas() {
        for (int i = 1; i < 9; i++) {
            WeekS.add(i + "");
        }
    }
}
