package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.EduStudentNameRequest;
import com.zjhz.teacher.NetworkRequests.request.GradeClassRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.ClassCurrentPositionAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Auditor;
import com.zjhz.teacher.ui.view.OptionsGradeView;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
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
 * Date: 2016-08-02
 * Time: 14:23
 * Description: 班级德育   选择督导员
 */
public class ClassMoralEducationStudentActivity extends BaseActivity implements AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = ClassMoralEducationStudentActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    public TextView titleTv;
//    public ArrayList<String> grades = new ArrayList<>();
//    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();
//    public ArrayList<StudentsCurrentPositionClassGrade> clazzsGrades = new ArrayList<>();
    public int index_grade;
    @BindView(R.id.activity_education_auditor_ll)
    LinearLayout linearLayout;
    @BindView(R.id.activity_education_auditor_ed)
    EditText editText;
    @BindView(R.id.activity_education_auditor_text)
    TextView textView;
    @BindView(R.id.activity_education_auditor_image)
    ImageView imageView;
    @BindView(R.id.refresh_listview)
    ListView listView;
    @BindView(R.id.refresh_layout)
    RefreshLayout swipeLayout;
    boolean whether = false;
    ClassCurrentPositionAdapter adapter;
    OptionsGradeView mOptionsGradeView;
    int currentPage = 1;
    boolean isName = true;
    boolean refresh = false;
    int index_clazz;
    private List<Auditor> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_auditor);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initRefreshLayout();
        titleTv.setText("请选择督导员");

        mOptionsGradeView = new OptionsGradeView(this);
//        mOptionsGradeView.setOnClickSubmit(this);
//        dialog.show();
        //获取所有班级年级
//        NetworkRequest.request(null, CommonUrl.gradeClsList,Config.gradeClsList);
        MainActivity.getClassz();

        listView.setOnItemClickListener(this);
        adapter = new ClassCurrentPositionAdapter(this,lists);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(final Editable editable) {
                lists.clear();
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isName = false;
                        String str = editable.toString();
                        LogUtil.e("个人德育，学生姓名查询",editable.toString());
                        if (!TextUtils.isEmpty(str)) {
                            boolean isNum = str.matches("[0-9]+");
                            if (isNum){
                                EduStudentNameRequest studentNameRequest = new EduStudentNameRequest("",str);
                                NetworkRequest.request(studentNameRequest, CommonUrl.SCHOOLSTUDENTNAME_EDUCLASS,Config.SCHOOLSTUDENTNAME_EDUCLASS);
                            }else {
                                EduStudentNameRequest studentNameRequest = new EduStudentNameRequest(str,"");
                                NetworkRequest.request(studentNameRequest, CommonUrl.SCHOOLSTUDENTNAME_EDUCLASS,Config.SCHOOLSTUDENTNAME_EDUCLASS);
                            }
                        }
                    }
                }, 100);
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            /*case Config.gradeClsList:  // 年级班级
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
                                                strings.add(o1.optString("name"));
                                            }
                                        }
                                    }else{
                                        strings.add(" ");
                                    }
                                    clazzs.add(strings);
                                    AppContext.getInstance().clazzs.add(strings);
                                    clazzsGrades.add(mStudentsCurrentPositionClassGrade);
                                    AppContext.getInstance().clazzsGrades.add(mStudentsCurrentPositionClassGrade);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.e("班级年级","班级 = " + clazzs.size() + "年级 = " + grades.size());
                }
                break;*/
            case Config.RfidList_edu_class:  // 根据年级班级获取学生列表
                dialog.dismiss();
                if (refresh) {
                    stopRefresh();
                }
                JSONObject student = (JSONObject) event.getData();
                if (student != null) {
                    JSONArray data = student.optJSONArray("data");
                    LogUtil.e("德育督导员数据", "student = " + student);
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                Auditor currentPositionOne = new Auditor();
                                currentPositionOne.studentName = o.optString("studentName");
                                currentPositionOne.name = o.optString("name");
                                currentPositionOne.studentId = o.optString("studentId");
                                currentPositionOne.gradeId = o.optString("gradeId");
                                currentPositionOne.classId = o.optString("classId");
                                currentPositionOne.certificateId = o.optString("certificateId");
                                lists.add(currentPositionOne);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                break;
            case Config.SCHOOLSTUDENTNAME_EDUCLASS:  // 根据学生姓名和学籍号码查询学生信息
                dialog.dismiss();
                JSONObject studentOne = (JSONObject) event.getData();
                if (studentOne != null) {
                    JSONArray data = studentOne.optJSONArray("data");
                    LogUtil.e("督导员学生", "student = " + studentOne);
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                Auditor currentPositionOne = new Auditor();
                                currentPositionOne.studentName = o.optString("studentName");
                                currentPositionOne.name = o.optString("name");
                                currentPositionOne.studentId = o.optString("studentId");
                                currentPositionOne.gradeId = o.optString("gradeId");
                                currentPositionOne.classId = o.optString("classId");
                                currentPositionOne.certificateId = o.optString("certificateId");
                                lists.add(currentPositionOne);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                break;
            case "complete":
                EventBus.getDefault().post(new EventCenter<>("updatecomplete",null));
                finish();
                break;
        }
    }

    @OnClick({R.id.title_back_img, R.id.activity_education_auditor_text, R.id.activity_education_auditor_image})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.activity_education_auditor_text:
                selectGrade();
                break;
            case R.id.activity_education_auditor_image:
                if (whether) {
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.mipmap.extramural_up);
                    whether = false;
                }else{
                    imageView.setImageResource(R.mipmap.extramural_down);
                    linearLayout.setVisibility(View.GONE);
                    whether = true;
                }
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i < lists.size()){
            Intent intent = new Intent(this,ClassAndGradeEducationListCheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("EducationStudent_name",lists.get(i).studentName);
            bundle.putString("EducationStudent_id_name",lists.get(i).studentId);
            intent.putExtra("bundle_education_class",bundle);
            startActivity(intent);// 跳转到德育
            finish();
        }
    }

    public  void selectGrade() {
        if (!AppContext.getInstance().gradeBeans.isEmpty() && !AppContext.getInstance().clazzs.isEmpty()){
            mOptionsGradeView.setPicker(AppContext.getInstance().grades,AppContext.getInstance().clazzs, index_grade, index_clazz, 0);
            mOptionsGradeView.setCyclic(false);
            mOptionsGradeView.setCancelable(true);
            mOptionsGradeView.setOnoptionsSelectListener(new OptionsGradeView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3) {
                    if (AppContext.getInstance().clazzs.get(options1).size() > 0 && option2 < AppContext.getInstance().clazzs.get(options1).size()){
                        textView.setText(AppContext.getInstance().grades.get(options1)+ AppContext.getInstance().clazzs.get(options1).get(option2));
                        index_grade = options1;
                        index_clazz = option2;
                        LogUtil.e("index_grade = " + index_grade + " index_clazz = " + index_clazz);
                        isName = true;
                        String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
                        String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
                        LogUtil.e("年级IDindex_grade = " + index_grade + " 班级IDindex_clazz = " + index_clazz);
                        LogUtil.e("年级ID = " + gradeId + " 班级ID = " + classId);
                        lists.clear();
                        dialog.show();
                        listView.requestLayout();
                        adapter.notifyDataSetChanged();
                        GradeClassRequest gradeClassRequest = new GradeClassRequest("1","100",gradeId,classId);
                        LogUtil.e("获取班级督导员的请求参数 = ", GsonUtils.toJson(gradeClassRequest));
                        NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList_edu_class,Config.RfidList_edu_class);
                    }else {
                        textView.setText("请选择年级/班级");
                        index_grade = 0;
                        index_clazz = 0;
                        ToastUtils.showShort("班级为空，重新选择");
                    }
                }
            });
            mOptionsGradeView.show();
        }else {
            ToastUtils.toast("没有数据");
        }
    }

    @Override
    public void onRefresh() {
        if (isName) {
            lists.clear();
            listView.requestLayout();
            adapter.notifyDataSetChanged();
            refresh = true;
            String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
            String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
            GradeClassRequest gradeClassRequest = new GradeClassRequest("1","100",gradeId,classId);
            NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList,Config.RfidList);
        }else{
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoad() {
        if (isName) {
            ++currentPage;
            refresh = true;
            String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
            String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
            GradeClassRequest gradeClassRequest = new GradeClassRequest(String.valueOf(currentPage),"10",gradeId,classId);
            NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList,Config.RfidList);
        }else{
            stopRefresh();
        }
    }

    private void stopRefresh(){
        if (swipeLayout != null){
            swipeLayout.setRefreshing(false);
            swipeLayout.setLoading(false);
        }
    }
    private void initRefreshLayout() {
//        swipeLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(true);
//            }
//        }));
        swipeLayout.setColorSchemeResources(Constance.colors);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
    }
}
