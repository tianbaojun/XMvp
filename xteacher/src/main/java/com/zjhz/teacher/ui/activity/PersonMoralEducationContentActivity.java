package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.GradeClassRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationContentRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationListRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonProjectParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Auditor;
import com.zjhz.teacher.bean.PersonMoralEducationInputeBean;
import com.zjhz.teacher.bean.PersonMoralEducationStudentNumberBean;
import com.zjhz.teacher.ui.dialog.SelectStudentDialog;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

import static com.zjhz.teacher.R.id.activity_person_moral_education_score;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-02
 * Time: 14:23
 * Description: 个人德育   个人德育录入
 */
public class PersonMoralEducationContentActivity extends BaseActivity {
    private final static String TAG = PersonMoralEducationContentActivity.class.getSimpleName();/*
    public ArrayList<String> grades = AppContext.getInstance().grades;
    public ArrayList<ArrayList<String>> clazzs = AppContext.getInstance().clazzs;
    public List<Grade> gradeAndClass = new ArrayList<>();*/
    public ArrayList<String> subStrs = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();
    public Map<String, Integer> meterModeMaps = new HashMap<>();
    public Map<String, String> meterRangeMaps = new HashMap<>();
    public Map<String, Integer> scoreModeMaps = new HashMap<>();
    public Map<String, String> meterUnitMaps = new HashMap<>();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    //检查时间输入框
    @BindView(R.id.activity_person_moral_education_content_check_time)
    TextView activityPersonMoralEducationContentCheckTime;
    //选择德育项目
    @BindView(R.id.activity_person_moral_education_content_check_project)
    TextView activityPersonMoralEducationContentCheckProject;
    //学生号
    @BindView(R.id.activity_person_moral_education_content_check_ed)
    EditText activityPersonMoralEducationContentCheckEd;
    //选择年级班级
    @BindView(R.id.activity_person_moral_education_content_check_clazz)
    TextView activityPersonMoralEducationContentCheckClazz;
    //头像图片
    @BindView(R.id.activity_person_moral_education_content_check_head)
    ImageView activityPersonMoralEducationContentCheckHead;
    //提交按钮
    @BindView(R.id.activity_person_moral_education_content_check_submit)
    TextView activityPersonMoralEducationContentCheckSubmit;  // 提交
    //    督导人
    @BindView(R.id.activity_person_moral_education_content_check_name)
    TextView activityPersonMoralEducationContentCheckName;
    //督导日期
    @BindView(R.id.activity_person_moral_education_content_check_time_time)
    TextView activityPersonMoralEducationContentCheckTimeTime;
    //得分输入框
    @BindView(activity_person_moral_education_score)
    EditText editTextScore;
    //    输入分数的布局
    @BindView(R.id.activity_person_moral_education_score_rl)
    RelativeLayout activityPersonMoralEducationScoreRl;
    @BindView(R.id.activity_person_moral_education_content_between)
    TextView activityPersonMoralEducationContentBetween;
    //    分之区间
    @BindView(R.id.activity_person_moral_education_content_between_rl)
    RelativeLayout activityPersonMoralEducationContentBetweenRl;
    //    选择的学生姓名
    @BindView(R.id.student_name)
    TextView student_name;
    //    标示加分还是减分
    @BindView(R.id.is_add_delete)
    TextView is_add_delete;
    int index1 = 0, index2 = 0;
    int index_subject = 0;
    int isSubmit = 0;
    private String educationStudentName;
    private List<PersonMoralEducationStudentNumberBean> studentNumbers = new ArrayList<>();
    private String nameId;
    private int i_fly = 0;
    private String project;//德育项目name
    //    计量模式，0-固定加减分  1-评分加减分
    private Integer meterModer;
    //      0-减分  1-加分
    private int scoreMode;
    //    分数范围
    private double valueBetween;
    //    计量单元
    private double meterUnit;
    //    分数
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_moral_education_content);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle_education");
        if (bundle == null) {
            NetworkRequest.request(null, CommonUrl.QUERYINITINSPECTOR, Config.QUERYINITINSPECTOR);
        } else {
            educationStudentName = bundle.getString("EducationStudent");
            if(educationStudentName == null){
                educationStudentName = "";
            }

            nameId = bundle.getString("EducationStudent_id");
        }

        PersonProjectParams personProjectParams = new PersonProjectParams("1", "1", "1", "100");
        NetworkRequest.request(personProjectParams, CommonUrl.PERSON_EDUCATION_SPECIAL_LIST, Config.PERSON_EDUCATION_SPECIAL_LIST);
        MainActivity.getClassz();
        initView();
    }

    private void initView() {
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(educationStudentName);
        if(educationStudentName==null) {
            activityPersonMoralEducationContentCheckName.setText("");
        }else{
            activityPersonMoralEducationContentCheckName.setText("检查人：" + educationStudentName);
        }
        activityPersonMoralEducationContentCheckTimeTime.setText("检查时间：" + TimeUtil.refFormatNowDate());
        activityPersonMoralEducationContentCheckTime.setText("检查时间："+TimeUtil.getNowYMDHMTime(new Date()));
        titleTv.setText("德育专项检查");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.title_back_img, R.id.right_text, R.id.activity_person_moral_education_content_check_time, R.id.activity_person_moral_education_content_check_project, R.id.activity_person_moral_education_content_check_clazz, R.id.activity_person_moral_education_content_check_submit, R.id.activity_person_moral_education_content_check_ed_search})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:  //返回
                finish();
                break;
            case R.id.right_text:   //选择督导员
                startActivity(PersonMoralEducationStudentActivity.class);
                break;
            case R.id.activity_person_moral_education_content_check_time:
                selectTime(this, activityPersonMoralEducationContentCheckTime);
                break;
            case R.id.activity_person_moral_education_content_check_project:
                selectSubject();
                break;
            case R.id.activity_person_moral_education_content_check_clazz:
                selectGradeCls();
                break;
            case R.id.activity_person_moral_education_content_check_submit:
                if (studentNumbers != null && studentNumbers.size() > 0 && isSubmit == 1) {
                    String time_re = activityPersonMoralEducationContentCheckTime.getText().toString().replace("检查时间：", "").trim();
                    score = editTextScore.getText().toString();
                    boolean isNumber = isScore();
                    LogUtil.e("角标 = ", i_fly + "");
                    String studentId = studentNumbers.get(i_fly).id;
                    if (TextUtils.isEmpty(score)) {
                        ToastUtils.showShort("得分不能为空");
                    } else {
                        if (isNumber) {
                            PersonMoralEducationContentRequest mPersonMoralEducationContentRequest = new PersonMoralEducationContentRequest(time_re, maps.get(project), nameId, studentId, score);
                            LogUtil.e("德育录入提交的请求参数", GsonUtils.toJson(mPersonMoralEducationContentRequest));
                            runOnUiThread(new Runnable() {

                                              @Override
                                              public void run() {
                                                  dialog.show();
                                              }
                                          });
                            NetworkRequest.request(mPersonMoralEducationContentRequest, CommonUrl.PERSON_EDUCATION_INPUT_SUB, Config.PERSON_EDUCATION_INPUT_SUB);
                            finish();
                        } else {
                            ToastUtils.showShort("请输入分值区间的数字");
                        }
                    }
                } else {
                    ToastUtils.toast("所选学生和德育项目不存在");
                }
                break;
            case R.id.activity_person_moral_education_content_check_ed_search:
                String str = activityPersonMoralEducationContentCheckEd.getText().toString();
                LogUtil.e("德育录入，学生学号查询", str);
                boolean isNum = str.matches("[0-9]+");
                if (isNum) {
                    dialog.show();
                    LogUtil.e("德育录入，学生学号查询", activityPersonMoralEducationContentCheckEd.getText().toString());
                    PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest("1", "10", str);
                    NetworkRequest.request(personMoralEducationListRequest, CommonUrl.MORALEDUCATIONSTUDENTLIST, Config.PERSON_EDUCATION_INPUTE);
                } else {
                    ToastUtils.showShort("输入学号不正确");
                }
                break;
        }


    }

    private boolean isScore() {
        if (meterModer == 0) {
            //设置得分为固定分
            score = String.valueOf(meterUnit);
            if (scoreMode == 0) {
                score = "-" + score;
            }
            return true;
        } else {
            if(!SharePreCache.isEmpty(score)) {
                if (scoreMode == 0) {
                    if (score.equals(".") || Float.valueOf(score) > valueBetween) {
                        return false;
                    }
                    score = "-" + score;
                    return true;
                } else if (scoreMode == 1) {
                    return !(score.equals(".") || Float.parseFloat(score) > valueBetween);
                }
            }
        }
        return false;
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
                JSONObject jsonObject1 = (JSONObject) event.getData();
                gradeAndClass = GsonUtils.toArray(Grade.class, jsonObject1);
                if (null != gradeAndClass && gradeAndClass.size() > 0) {
                    this.clazzs.clear();
                    this.grades.clear();
                    for (Grade grade : gradeAndClass) {
                        this.grades.add(grade.getName());
                        ArrayList<String> list = new ArrayList<>();
                        for (Classz classz : grade.getDetail()) {
                            list.add(classz.getName());
                        }
                        this.clazzs.add(list);
                    }
                    selectGradeCls();
                } else {
                    ToastUtils.showShort(R.string.data_not_found);
                }
                break;*/
            case Config.PERSON_EDUCATION_INPUTE:  // 根据学号获取的数据
                dialog.dismiss();
                JSONObject studentNumber = (JSONObject) event.getData();
                LogUtil.e("根据学号获取的数据 = ", studentNumber.toString() + "");
                if (studentNumber != null) {
                    JSONArray data = studentNumber.optJSONArray("data");
                    studentNumbers.clear();
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                PersonMoralEducationStudentNumberBean studentNumberBean = new PersonMoralEducationStudentNumberBean();
                                studentNumberBean.photoUrl = o.optString("photoUrl");
                                studentNumberBean.id = o.optString("studentId");  // 被检查学生id
                                studentNumbers.add(studentNumberBean);
                                ToastUtils.showShort("获取成功");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ToastUtils.showShort("没有数据");
                    }
                } else {

                }
                if (studentNumbers != null && studentNumbers.size() > 0) {
                    GlideUtil.loadImageHead(studentNumbers.get(0).photoUrl, activityPersonMoralEducationContentCheckHead);
                }
                break;
            case Config.PERSON_EDUCATION_SPECIAL_LIST:
                JSONObject data = (JSONObject) event.getData();
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                LogUtil.e(o.toString());
                                PersonMoralEducationInputeBean input = new PersonMoralEducationInputeBean();
                                input.name = o.optString("moralName");
                                input.id = o.optString("moralId");
                                subStrs.add(o.optString("moralName"));
                                maps.put(o.optString("moralName"), o.optString("moralId"));
                                LogUtil.e("计分模式" + o.optString("meterMode"));
                                meterModeMaps.put(o.optString("moralName"), o.optInt("meterMode"));
                                meterRangeMaps.put(o.optString("moralName"), o.optString("meterRange"));
                                scoreModeMaps.put(o.optString("moralName"), o.optInt("scoreMode"));
//                                小数点记分
                                meterUnitMaps.put(o.optString("moralName"), o.optString("meterUnit"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        activityPersonMoralEducationContentCheckProject.setText(subStrs.get(0));
                        project = subStrs.get(0).trim();
                        isSubmit = 1;
                        index_subject = 0;
                        handlerMeterMode();
                    }
                }
                break;
            case Config.PERSON_EDUCATION_INPUTE_CLASS:
                JSONObject clazz = (JSONObject) event.getData();
                if (clazz != null) {
                    JSONArray data1 = clazz.optJSONArray("data");
                    studentNumbers.clear();
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                PersonMoralEducationStudentNumberBean studentNumberBean = new PersonMoralEducationStudentNumberBean();
                                studentNumberBean.moralName = o.optString("moralName");
                                studentNumberBean.photoUrl = o.optString("photoUrl");
                                studentNumberBean.studentName = o.optString("studentName");
                                studentNumberBean.score = o.optString("score");
                                studentNumberBean.id = o.optString("studentId");
                                studentNumbers.add(studentNumberBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                new SelectStudentDialog(this, studentNumbers);
                break;
            case Config.STUDENT_EDUCATION:
                String index = (String) event.getData();
                if (!TextUtils.isEmpty(index)) {
                    i_fly = Integer.parseInt(index);
                    LogUtil.e("德育弹窗选中的角标对应的头像 = ", studentNumbers.get(i_fly).photoUrl);
                    // TODO: 2017/3/3 显示选中的人员
                    GlideUtil.loadImageHead(studentNumbers.get(i_fly).photoUrl, activityPersonMoralEducationContentCheckHead);
                    student_name.setText(studentNumbers.get(i_fly).studentName);
                }
                LogUtil.e("德育弹窗选中的角标 = ", index);
                break;
            case Config.PERSON_EDUCATION_INPUT_SUB:
                dialog.dismiss();
                ToastUtils.toast("提交成功");
                EventBus.getDefault().post(new EventCenter<>("percomplete", null));
                finish();
                break;
            case Config.RFIDLIST_FLY:
                dialog.dismiss();
                studentNumbers.clear();
                JSONObject student = (JSONObject) event.getData();
                if (student != null) {
                    JSONArray dataF = student.optJSONArray("data");
                    LogUtil.e("德育督导员数据", "student = " + student);
                    if (dataF != null && dataF.length() > 0) {
                        for (int i = 0; i < dataF.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) dataF.get(i);
                                PersonMoralEducationStudentNumberBean studentNumberBean = new PersonMoralEducationStudentNumberBean();
//                                studentNumberBean.moralName = o.optString("moralName");
                                studentNumberBean.photoUrl = o.optString("photoUrl");
                                studentNumberBean.studentName = o.optString("name");
//                                studentNumberBean.score = o.optString("score");
                                studentNumberBean.id = o.optString("studentId");
                                studentNumbers.add(studentNumberBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                new SelectStudentDialog(this, studentNumbers);
                break;
            case "isUpade":
                break;
            case Config.QUERYINITINSPECTOR:
                Auditor auditor = null;
                try {
                    JSONObject jsonObject = ((JSONObject) event.getData()).getJSONObject("data");
                    auditor = GsonUtils.fromJson(jsonObject.toString(), Auditor.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (auditor != null) {

                    if(auditor.studentName!=null) {
                        right_text.setVisibility(View.VISIBLE);
                        right_text.setText(auditor.studentName);
                        activityPersonMoralEducationContentCheckName.setText("检查人：" + auditor.studentName);
                        nameId = auditor.studentId;
                    }else{
                        activityPersonMoralEducationContentCheckName.setText("");
                        right_text.setText("");
                        right_text.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    /**
     * 处理记分模式
     */
    private void handlerMeterMode() {

        LogUtil.e("判断meterModer时的德育项目名字" + project);
        meterModer = meterModeMaps.get(project);
        LogUtil.e("判断meterModer时的德育项目计分模式" + meterModer);
        scoreMode = scoreModeMaps.get(project);
//        评分范围
        valueBetween = TextUtils.isEmpty(meterRangeMaps.get(project)) ? 0 : Double.valueOf(meterRangeMaps.get(project));
//        固定评分单元
        meterUnit = TextUtils.isEmpty(meterUnitMaps.get(project)) ? 0 : Double.valueOf(meterUnitMaps.get(project));

        if (meterModer == 0) {//固定评分
            activityPersonMoralEducationScoreRl.setVisibility(View.GONE);
            activityPersonMoralEducationContentBetweenRl.setVisibility(View.GONE);
            student_name.setVisibility(View.VISIBLE);
        } else if (meterModer == 1) {//输入评分
            activityPersonMoralEducationScoreRl.setVisibility(View.VISIBLE);
            //显示评分范围
            handlerMeterRange();
        } else
            ToastUtils.showShort("没有计分模式");

    }

    private void handlerMeterRange() {
        activityPersonMoralEducationContentBetweenRl.setVisibility(View.VISIBLE);

        if (scoreMode == 0) {
            is_add_delete.setText("扣分(-)：");
            editTextScore.setHint("请输入扣分");
            if (valueBetween == 0) {
                activityPersonMoralEducationContentBetween.setText("分值区间:" + valueBetween + "-0");
            } else {
                activityPersonMoralEducationContentBetween.setText("分值区间:-" + valueBetween + "-0");
            }
        } else if (scoreMode == 1) {
            is_add_delete.setText("加分(+)：");
            editTextScore.setHint("请输入加分");
            if (valueBetween == 0) {
                activityPersonMoralEducationContentBetween.setText("分值区间:" + valueBetween + "-0");
            } else {
                activityPersonMoralEducationContentBetween.setText("分值区间:0-" + valueBetween);
            }
        }
    }

    /**
     * 选择班级
     */
    private void selectGradeCls() {
        if (AppContext.getInstance().grades.size() > 0 && AppContext.getInstance().clazzs.size() > 0 ) {
            final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker(AppContext.getInstance().grades, AppContext.getInstance().clazzs, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                    if ( AppContext.getInstance().clazzs.get(options1).size() > 0 && options2 <  AppContext.getInstance().clazzs.get(options1).size()) {
                        activityPersonMoralEducationContentCheckClazz.setText( AppContext.getInstance().clazzs.get(options1).get(options2));
                        index1 = options1;
                        index2 = options2;
                        String gradeId =  AppContext.getInstance().gradeBeans.get(options1).getGradeId();
                        String clazzId =  AppContext.getInstance().gradeBeans.get(options1).getDetail().get(options2).getClassId();
                        GradeClassRequest gradeClassRequest = new GradeClassRequest("1", "50", gradeId, clazzId);
                        NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList, Config.RFIDLIST_FLY);
                    } else {
                        activityPersonMoralEducationContentCheckClazz.setText("");
                        index1 = 0;
                        index2 = 0;
                        ToastUtils.showShort("班级为空，不能选择");
                    }
                }
            });
            optionsPickerView.show();
        } else {
//            NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsList);
            MainActivity.getClassz();
        }
    }

    /**
     * 选择检查项目
     */
    private void selectSubject() {
        if (subStrs.size() > 0) {
            OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker((ArrayList) subStrs, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                    activityPersonMoralEducationContentCheckProject.setText(subStrs.get(options1));
                    project = activityPersonMoralEducationContentCheckProject.getText().toString().trim();
//                    activity.subjectId = activity.subObj.get(options1).getSubjectId();
                    isSubmit = 1;
                    index_subject = options1;
                    handlerMeterMode();
                }
            });
            optionsPickerView.show();
        } else {
            ToastUtils.showShort("没有数据");
        }
    }

    //选择时间
    public void selectTime(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.MONTH_DAY_HOUR_MIN, null, 0);
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
//                时间设置为年月日时分格式
                textView.setText("检查时间：" + TimeUtil.getNowYMDHMTime(date));
                activityPersonMoralEducationContentCheckTimeTime.setText(textView.getText());
            }
        });
    }
}
