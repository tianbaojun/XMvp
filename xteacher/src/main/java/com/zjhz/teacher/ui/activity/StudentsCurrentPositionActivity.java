package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.GradeClassRequest;
import com.zjhz.teacher.NetworkRequests.request.StudentNameRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.StudentsCurrentPositionsAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.StudentEvent;
import com.zjhz.teacher.bean.StudentsCurrentPosition;
import com.zjhz.teacher.ui.view.OptionsGradeView;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.WheelUtil;

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
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询
 */
public class StudentsCurrentPositionActivity extends BaseActivity implements WheelUtil.OnWheelClickSubmit, AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = StudentsCurrentPositionActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    public TextView titleTv;/*
    public ArrayList<String> grades = new ArrayList<>();
    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();
    public ArrayList<StudentsCurrentPositionClassGrade> clazzsGrades = new ArrayList<>();*/
    public int index_grade;
    @BindView(R.id.activity_students_current_position_ed)
    EditText editText;
    @BindView(R.id.search_image)
    ImageView search_image;
    @BindView(R.id.activity_students_current_position_text)
    TextView textView;
    @BindView(R.id.activity_students_current_position_image)
    ImageView imageView;
    @BindView(R.id.refresh_listview)
    ListView listView;
    @BindView(R.id.activity_students_current_position_ll)
    LinearLayout linearLayout;
    @BindView(R.id.refresh_layout)
    RefreshLayout swipeLayout;
    @BindView(R.id.activity_students_current_position_text_time)
    TextView hestoryTime;  // 历史时间
    boolean whether = false;
    StudentsCurrentPositionsAdapter adapter;
    OptionsGradeView mOptionsGradeView;
    int currentPage = 1;
    boolean isName = true;
    boolean refresh = false;
    WheelUtil mWheelUtil;
    int index_clazz;
    private List<StudentsCurrentPosition> lists = new ArrayList<>();
    private int from;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_current_position);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initRefreshLayout();
        titleTv.setText("学生位置查询");
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getIntExtra("from", 0);
        }
        if (100 == from || from == 2) {
            hestoryTime.setVisibility(View.VISIBLE);
        }
        mWheelUtil = new WheelUtil();
        mWheelUtil.setOnClickSubmit(this);
        if (from == 2) {
            mWheelUtil.setType("1");
        }
        mWheelUtil.initDatas();

        mOptionsGradeView = new OptionsGradeView(this);
//        mOptionsGradeView.setOnClickSubmit(this);
//        dialog.show();
        //获取所有班级年级
//        NetworkRequest.request(null, CommonUrl.gradeClsList, "gradeClsList");
        MainActivity.getClassz();
        listView.setOnItemClickListener(this);
        adapter = new StudentsCurrentPositionsAdapter(this, lists);
        listView.setAdapter(adapter);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null) {
                hideSoftInput();
            }
        }
        return super.onTouchEvent(event);
    }*/

    /**
     * 隐藏软键盘
     */
    private void searchStudent() {
        if (getCurrentFocus().getWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            dialog.show();
            isName = false;
            String str = editText.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                LogUtil.e("校外定位，学生姓名查询", editText.getText().toString());
                boolean isNum = str.matches("[0-9]+");
                if (isNum) {
                    StudentNameRequest studentNameRequest = new StudentNameRequest("", str);
                    NetworkRequest.request(studentNameRequest, CommonUrl.SCHOOLSTUDENTNAME, Config.SCHOOLSTUDENTNAME);
                } else {
                    StudentNameRequest studentNameRequest = new StudentNameRequest(str, "");
                    NetworkRequest.request(studentNameRequest, CommonUrl.SCHOOLSTUDENTNAME, Config.SCHOOLSTUDENTNAME);
                }
            }
        }
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
           /* case "gradeClsList":  // 年级班级
                dialog.dismiss();
                JSONObject json = (JSONObject) event.getData();
                if (json != null) {
                    JSONArray data = json.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                if (o != null) {
                                    StudentsCurrentPositionClassGrade mStudentsCurrentPositionClassGrade = new StudentsCurrentPositionClassGrade();
                                    mStudentsCurrentPositionClassGrade.gradeId = o.optString("gradeId");
                                    mStudentsCurrentPositionClassGrade.gradeName = o.optString("name");
                                    grades.add(o.optString("name"));
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
                                    } else {
                                        strings.add(" ");
                                    }
                                    clazzs.add(strings);
                                    clazzsGrades.add(mStudentsCurrentPositionClassGrade);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    LogUtil.e("班级年级", "班级 = " + clazzs.size() + "年级 = " + grades.size());
                }
                break;*/
            case Config.RfidList:  // 根据年级班级获取学生列表
                dialog.dismiss();
                if (refresh) {
                    stopRefresh();
                }
                JSONObject student = (JSONObject) event.getData();
                if (student != null) {
                    JSONArray data = student.optJSONArray("data");
                    LogUtil.e("定位学生", "student = " + student);
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                StudentsCurrentPosition currentPositionOne = new StudentsCurrentPosition();
                                currentPositionOne.name = o.optString("name");
                                currentPositionOne.card = o.optString("rfidCard");
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
            case Config.SCHOOLSTUDENTNAME:  // 根据学生姓名和学籍号码查询学生信息
                dialog.dismiss();
                lists.clear();
                JSONObject studentOne = (JSONObject) event.getData();
                if (studentOne != null) {
                    JSONArray data = studentOne.optJSONArray("data");
                    LogUtil.e("定位学生", "student = " + studentOne);
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                StudentsCurrentPosition currentPositionOne = new StudentsCurrentPosition();
                                currentPositionOne.name = o.optString("name");
                                currentPositionOne.card = o.optString("rfidCard");
                                lists.add(currentPositionOne);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        ToastUtils.showShort("没有符合的学生");
                    }
                }
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.title_back_img, R.id.activity_students_current_position_text, R.id.activity_students_current_position_image,
            R.id.activity_students_current_position_text_time,R.id.search_image})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.activity_students_current_position_text:
                ViewTools.hideSoftInput(this);
                selectGrade();
                break;
            case R.id.activity_students_current_position_text_time:  // TODO 选择历史时间
                mWheelUtil.selectData(this);
                break;
            case R.id.activity_students_current_position_image:
                if (whether) {
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.mipmap.extramural_up);
                    whether = false;
                } else {
                    imageView.setImageResource(R.mipmap.extramural_down);
                    linearLayout.setVisibility(View.GONE);
                    whether = true;
                }
                break;
            case R.id.search_image:   //搜索
                searchStudent();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String cardNum = lists.get(i).card; // 拿到对应学生卡号
        String studentName = lists.get(i).name;
//        LogUtil.e("定位的学生卡号 = ",cardNum+"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuNameKey, studentName);
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuRicardKey, cardNum);
        if (from == 1) {
            Intent intent = new Intent();
            intent.putExtra("cardNum", cardNum);
            intent.putExtra("studentName", studentName);
            setResult(RESULT_OK, intent);
            finish();
        } else if (from == 2) {
            if (!TextUtils.isEmpty(hestoryTime.getText().toString().trim()) && !"请选择历史时间".equals(hestoryTime.getText().toString().trim())) {
                Intent intent = new Intent();
                intent.putExtra("cardNum", cardNum);
                intent.putExtra("studentName", studentName);
                intent.putExtra("historyDate", mWheelUtil.historyDate);
                intent.putExtra("historyType", mWheelUtil.flag);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtils.toast("请选择历史时间");
            }
        } else if (100 == from) {
            if (!TextUtils.isEmpty(hestoryTime.getText().toString().trim()) && !"请选择历史时间".equals(hestoryTime.getText().toString().trim())) {
                EventBus.getDefault().post(new EventCenter<Object>("STUDENT", new StudentEvent(cardNum, mWheelUtil.historyDate, mWheelUtil.flag)));
                finish();
            } else {
                ToastUtils.toast("历史时间不能为空");
            }
        } else {
            EventBus.getDefault().post(new EventCenter<Object>("STUDENT", new StudentEvent(cardNum, mWheelUtil.historyDate, mWheelUtil.flag)));
            finish();
        }
    }

    /**
     * 历史时间的
     */
    @Override
    public void onClickSubmit() {
        hestoryTime.setText(mWheelUtil.historyDate);
    }

    public void selectGrade() {
        if  (!AppContext.getInstance().gradeBeans.isEmpty() && !AppContext.getInstance().clazzs.isEmpty()) {
            mOptionsGradeView.setPicker((ArrayList) AppContext.getInstance().grades, AppContext.getInstance().clazzs, index_grade, index_clazz, 0);
            mOptionsGradeView.setCyclic(false);
            mOptionsGradeView.setCancelable(true);
            mOptionsGradeView.setOnoptionsSelectListener(new OptionsGradeView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    if (AppContext.getInstance().clazzs.get(options1).size() > 0 && option2 <  AppContext.getInstance().clazzs.get(options1).size()) {
                        textView.setText( AppContext.getInstance().clazzs.get(options1).get(option2));
                        index_grade = options1;
                        index_clazz = option2;
                        LogUtil.e("index_grade = " + index_grade + " index_clazz = " + index_clazz);
                        isName = true;
                        String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
                        String classId =AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
                        LogUtil.e("年级IDindex_grade = " + index_grade + " 班级IDindex_clazz = " + index_clazz);
                        LogUtil.e("年级ID = " + gradeId + " 班级ID = " + classId);
                        lists.clear();
                        dialog.show();
                        listView.requestLayout();
                        adapter.notifyDataSetChanged();
                        GradeClassRequest gradeClassRequest = new GradeClassRequest("1", "10", gradeId, classId);
                        NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList, Config.RfidList);
                    } else {
                        textView.setText("请选择年级/班级");
                        index_grade = 0;
                        index_clazz = 0;
                        ToastUtils.showShort(R.string.please_choose_effcitive_grades_and_classes);
                    }
                }
            });
            mOptionsGradeView.show();
        } else {
            ToastUtils.toast("没有数据");
        }
    }

    @Override
    public void onRefresh() {
        if (isName && AppContext.getInstance().gradeBeans.size()>0) {
            lists.clear();
            listView.requestLayout();
            adapter.notifyDataSetChanged();
            refresh = true;
            currentPage = 1;
            String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
            String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
            GradeClassRequest gradeClassRequest = new GradeClassRequest("1", "10", gradeId, classId);
            NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList, Config.RfidList);
        } else {
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoad() {
        if (isName && AppContext.getInstance().gradeBeans.size()>0 ) {
            ++currentPage;
            refresh = true;
            String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
            String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
            GradeClassRequest gradeClassRequest = new GradeClassRequest(String.valueOf(currentPage), "10", gradeId, classId);
            NetworkRequest.request(gradeClassRequest, CommonUrl.RfidList, Config.RfidList);
        } else {
            stopRefresh();
        }
    }

    private void stopRefresh() {
        if (swipeLayout != null) {
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
