package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.request.ClassAndGradeEducationListCheckScore;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.ClassAndGradeEducationListCheckAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.AddBean;
import com.zjhz.teacher.bean.AddListBean;
import com.zjhz.teacher.bean.Auditor;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-09-05
 * Time: 14:23
 * Description: 班级德育编辑   列表检查
 */
public class ClassAndGradeEducationListCheckActivity extends BaseActivity {

    public String studentId, studentName;  // 督导员id
    public String checkTime;
    public String subjectId;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_class_and_grade_education_list_check_project)
    TextView project;
    @BindView(R.id.activity_class_and_grade_education_list_check_time)
    TextView time1;
    @BindView(R.id.activity_class_and_grade_education_list_check_clazz)
    TextView clazz;
    @BindView(R.id.activity_class_and_grade_education_list_check_list)
    ListView listView;
    @BindView(R.id.activity_class_and_grade_education_list_check_submit)
    TextView submit;
    @BindView(R.id.title_ll)
    LinearLayout title_ll;
    ClassAndGradeEducationListCheckAdapter adapter;
    boolean isProject = false;
    boolean isSub = false;
    private List<ClassAndGradeEducationListCheckBean> lists = new ArrayList<>();
    private int index_classes = 0;
    private String gradeId = "";
    private int index_subject = 0;/*
    private List<Grade> grades = new ArrayList<>();
    private ArrayList classNames = new ArrayList();
    private ArrayList gradeNames = new ArrayList();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_class_and_grade_education_list_check);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        right_text.setVisibility(View.VISIBLE);
        MainActivity.getClassz();
        titleTv.setText("班级德育");
        time1.setText(TimeUtil.refFormatNowDate());
        adapter = new ClassAndGradeEducationListCheckAdapter(this, lists);
        listView.setAdapter(adapter);
        //设置默认的德育项目
        if(AppContext.getInstance().eProject.size()>0){
            project.setText(AppContext.getInstance().eduProjects.get(0).schemeName);
            isProject = true;
            subjectId = AppContext.getInstance().eduProjects.get(0).schemeId;
            index_subject = 0;
        }

        //显示默认年级，请求任课班级，获取之后判断在哪个年级
        NetworkRequest.request(new CenterParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey), 1, 100), CommonUrl.homeworkClassesList, "classList");
        dialog.show();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle_education_class");
        if(bundle == null) {
            NetworkRequest.request(null, CommonUrl.QUERYINITINSPECTOR, Config.QUERYINITINSPECTOR);
        }else{
            studentName = bundle.getString("EducationStudent_name");
            studentId = bundle.getString("EducationStudent_id_name");
            right_text.setText(studentName);
        }
        onScroll();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
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
            case Config.EDUCLASS_SCOREEmpty:
                dialog.dismiss();
                AppContext.getInstance().eduCheck.clear();
                lists.clear();
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("班级德育打分列表数据 = ", data.toString());
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                ClassAndGradeEducationListCheckBean bean = new ClassAndGradeEducationListCheckBean();
                                bean.classId = o.optString("classId");
                                bean.name = o.optString("name");
                                bean.moralId = o.optString("moralId");
                                bean.moralName = o.optString("moralName");
                                bean.scoreMode = o.optInt("scoreMode");
                                bean.meterMode = o.optInt("meterMode");
                                bean.meterRange = o.optString("meterRange");
                                if (o.optInt("scoreMode") == 0){//减分
                                    if (o.optInt("meterMode") == 0){ //打钩，固定值
                                        bean.min = "-"+ o.optString("meterUnit");
                                    }else {
                                        bean.min = "-"+ o.optString("meterRange");
                                    }
                                    bean.max = "0";
                                    bean.score = "0";
                                }else if (o.optInt("scoreMode") == 1){//加分
                                    if (o.optInt("meterMode") == 0){//打钩，固定值
                                        bean.max = o.optString("meterUnit");
                                        bean.score = o.optString("meterUnit");
                                    }else {
                                        bean.max = o.optString("meterRange");
                                        bean.score = o.optString("meterRange");
                                    }
                                    bean.min = "0";
                                }
                                AppContext.getInstance().eduCheck.add(bean);
                                lists.add(bean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        title_ll.setVisibility(View.VISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case Config.EDUCLASS_SCORE_SUB:
                dialog.dismiss();
                JSONObject sub = (JSONObject) event.getData();
                LogUtil.e("提交班级德育打分返回数据--------- = ", sub.toString());
                ToastUtils.showShort("提交成功");
                EventBus.getDefault().post(new EventCenter<>("complete",null));
                finish();
                break;
            case Config.QUERYINITINSPECTOR:
                Auditor auditor = null;
                try {
                    JSONObject jsonObject = ((JSONObject)event.getData()).getJSONObject("data");
                    auditor = GsonUtils.fromJson(jsonObject.toString(),Auditor.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(auditor!=null){
                    right_text.setText(auditor.studentName);
                    studentId = auditor.studentId;
                    studentName = auditor.studentName;
                }
                break;
            case "classList":
                dialog.dismiss();
                JSONObject os = (JSONObject) event.getData();
                List<ClassesBeans> classBeans = GsonUtils.toArray(ClassesBeans.class, os);
                for(ClassesBeans classesBeans:classBeans){
                    for(int i=0;i<AppContext.getInstance().gradeBeans.size();i++){
                        if(!TextUtils.isEmpty(classesBeans.getGradeId())) {
                            if (classesBeans.getGradeId().equals(AppContext.getInstance().gradeBeans.get(i).getGradeId())){
                                gradeId = classesBeans.getGradeId();
                                clazz.setText(AppContext.getInstance().gradeBeans.get(i).getName());
                                isSub = true;
                                break;
                            }
                        }
                    }
                }
                ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, subjectId,"1","1000");
                NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCOREEmpty, Config.EDUCLASS_SCOREEmpty);
                checkTime = time1.getText().toString();
                break;

            /*case Config.gradeClsList:  //获取年级信息
                dialog.dismiss();
                grades.clear();
                grades.addAll(GsonUtils.toArray(Grade.class, (JSONObject) event.getData()));
                if (null == grades || grades.size() == 0) {
                    ToastUtils.showShort(R.string.grade_data_not_found);
                } else {
                    classNames = getGradeClassName(grades).get("classNames");
                    gradeNames = getGradeClassName(grades).get("gradeNames");
                }
                break;*/
        }
    }

    @OnClick({R.id.title_back_img,
            R.id.right_text,
            R.id.activity_class_and_grade_education_list_check_project,
            R.id.activity_class_and_grade_education_list_check_time,
            R.id.activity_class_and_grade_education_list_check_clazz,
            R.id.activity_class_and_grade_education_list_check_submit})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:  //返回
                finish();
                break;
            case R.id.activity_class_and_grade_education_list_check_project:
                selectSubject();
                break;
            case R.id.activity_class_and_grade_education_list_check_time:
                BaseUtil.selectDate(this, time1);
                break;
            case R.id.activity_class_and_grade_education_list_check_clazz:
                if (isProject) {
                    checkTime = time1.getText().toString().trim();
                    selectClasses();
                } else {
                    ToastUtils.toast("请选择有效的德育方案");
                }
                break;
            case R.id.activity_class_and_grade_education_list_check_submit:
//                String projectName = project.getText().toString().trim();
//                String time = time1.getText().toString().trim();
//                String clazzName = clazz.getText().toString().trim();
                if (isSub) {
                    dialog.show();
                    List<AddBean> items = adapter.items;
                    AddListBean addListBean = new AddListBean(items);
                    addListBean.studentId = studentId;
                    addListBean.studentName = studentName;
                    LogUtil.e("新增分数的请求参数 = ", GsonUtils.toJson(addListBean));
                    NetworkRequest.request(addListBean, CommonUrl.EDUCLASS_SCORE_SUB_ALL, Config.EDUCLASS_SCORE_SUB);
                }else{
                    ToastUtils.toast("德育方案和班级不能为空");
                }
                break;
            case R.id.right_text:
                startActivity(ClassMoralEducationStudentActivity.class);
                break;
        }
    }

    /**
     * 获取班级列表
     */
   /* private void getClassz() {
        NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsList);
        dialog.show();
    }*/

    /**
     * 将年级列表装换成年级班级名字的 string 列表
     *
     * @param grades
     * @return
     */
    /*private Map<String, ArrayList> getGradeClassName(List<Grade> grades) {
        Map<String, ArrayList> map = new HashMap<>();
        ArrayList<ArrayList<String>> gradeClassNames = new ArrayList<>();
        ArrayList<String> gradeNames = new ArrayList<>();
        if (grades != null && !grades.isEmpty()) {
            for (Grade grade : grades) {
                gradeNames.add(grade.getName());
                List<Classz> classzs = grade.getDetail();
                ArrayList<String> classNames = new ArrayList<>();
                for (Classz classz : classzs) {
                    classNames.add(classz.getName());
                }
                gradeClassNames.add(classNames);
            }
            map.put("classNames", gradeClassNames);
            map.put("gradeNames", gradeNames);
            return map;
        }
        return null;
    }*/


    /**
     * 选择年级
     */
    private void selectClasses() {
        lists.clear();
        listView.requestLayout();
        adapter.notifyDataSetChanged();
        if (AppContext.getInstance().gradeBeans!=null&&AppContext.getInstance().gradeBeans.size()>0) {
            OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker((ArrayList) AppContext.getInstance().grades, index_classes, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                    clazz.setText(AppContext.getInstance().grades.get(options1));
                    gradeId = AppContext.getInstance().gradeBeans.get(options1).getGradeId();
                    index_classes = options1;
                    isSub = true;
                    dialog.show();
                    ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, subjectId,"1","1000");
                    NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCOREEmpty, Config.EDUCLASS_SCOREEmpty);
                }
            });
            optionsPickerView.show();

           /* final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker(AppContext.getInstance().grades, AppContext.getInstance().clazzs, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                    if (AppContext.getInstance().clazzs != null && AppContext.getInstance().clazzs.get(options1).size() > 0) {
                        clazz.setText(AppContext.getInstance().clazzs.get(options1).get(options2));
                    }
                    gradeId = AppContext.getInstance().gradeBeans.get(options1).getGradeId();
                    index_classes = options1;
                    isSub = true;
                    dialog.show();
                    ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, subjectId,"1","1000");
                    NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCOREEmpty, Config.EDUCLASS_SCOREEmpty);
                }
            });
            optionsPickerView.show();*/
        } else {
            ToastUtils.showShort("没有数据");
        }
    }

    /**
     * 德育方案
     */
    private void selectSubject() {
        if (AppContext.getInstance().eProject.size() > 0) {
            OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker((ArrayList) AppContext.getInstance().eProject, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                    project.setText(AppContext.getInstance().eduProjects.get(options1).schemeName);
                    isProject = true;
                    subjectId = AppContext.getInstance().eduProjects.get(options1).schemeId;
                    index_subject = options1;

                    if(!gradeId.equals("")) {
                        dialog.show();
                        ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, subjectId,"1","1000");
                        NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCOREEmpty, Config.EDUCLASS_SCOREEmpty);
                    }
                }
            });
            optionsPickerView.show();
        } else {
            ToastUtils.showShort("没有数据");
        }
    }

    public void onScroll() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
//                        LogUtil.e("滚动停止 = ", SharedPreferencesUtils.getSharePrefInteger("fly_int",0) + "");
//                        LogUtil.e("滚动停止对应的分数 = ", AppContext.getInstance().eduCheck.get(SharedPreferencesUtils.getSharePrefInteger("fly_int",0)).meterUnit + "");
//                        adapter.notifyDataSetChanged();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}