package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClassAndGradeEducationListCheckScore;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.ClassAndGradeEducationListCheckModifyAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.AddBean;
import com.zjhz.teacher.bean.AddListBean;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
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
 * Date: 2016-09-05
 * Time: 14:23
 * Description: 班级德育查看修改   列表检查
 */
public class ClassAndGradeEducationListCheckModifyActivity extends BaseActivity {

    public List<ClassAndGradeEducationListCheckBean> lists = new ArrayList<>();
    public List<String> classs = new ArrayList<>();
    public String subjectId;
    public String checkTime;
    public String studentId;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_class_and_grade_education_list_check_mod_project)
    TextView project1;
    @BindView(R.id.activity_class_and_grade_education_list_check_mod_time)
    TextView time1;
    @BindView(R.id.activity_class_and_grade_education_list_check_mod_clazz)
    TextView clazz1;
    @BindView(R.id.activity_class_and_grade_education_list_check_mod_list)
    ListView listView;
    @BindView(R.id.title_ll)
    LinearLayout title_ll;
    ClassAndGradeEducationListCheckModifyAdapter adapter;
    private String studentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_class_and_grade_education_list_check_mod);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        right_text.setVisibility(View.VISIBLE);
        titleTv.setText("班级德育");
        adapter = new ClassAndGradeEducationListCheckModifyAdapter(this,lists);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String project = bundle.getString("project");
        subjectId = bundle.getString("projectid");
        String clazz = bundle.getString("class");
        String gradeId = bundle.getString("classid");
        studentId = bundle.getString("studentId");
        checkTime = bundle.getString("bespeakTime");
        studentName = bundle.getString("studentName");
        right_text.setText(studentName);
        project1.setText(project);
        clazz1.setText(clazz);
        time1.setText(checkTime.substring(0,16));
        dialog.show();
        ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, subjectId,checkTime,studentId,"1","1000");
        NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCORE,Config.EDUCLASS_SCORE_MODIFY);
    }

    @Override
    protected boolean isBindEventBusHere() {return true;}

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
            case Config.EDUCLASS_SCORE_MODIFY:
                dialog.dismiss();
                lists.clear();
                AppContext.getInstance().eduCheckMore.clear();
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("班级德育修改打分列表数据 = ",data.toString());
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
                                bean.meterUnit = o.optInt("meterUnit");
                                bean.score = o.optString("score");
                                bean.inspector = o.optString("inspector");
                                if (o.optInt("scoreMode") == 0){//减分
                                    if (o.optInt("meterMode") == 0){ //打钩，固定值
                                        bean.min = "-"+ o.optString("meterUnit");
                                    }else {
                                        bean.min = "-"+ o.optString("meterRange");
                                    }
                                    bean.max = "0";
                                }else if (o.optInt("scoreMode") == 1){//加分
                                    if (o.optInt("meterMode") == 0){//打钩，固定值
                                        bean.max = o.optString("meterUnit");
                                    }else {
                                        bean.max = o.optString("meterRange");
                                    }
                                    bean.min = "0";
                                }
                                AppContext.getInstance().eduCheckMore.add(bean);
                                classs.add(o.optString("classId"));
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
            case Config.EDUCLASS_SCORE_SUB_MOD:
                dialog.dismiss();
                JSONObject sub = (JSONObject) event.getData();
                LogUtil.e("提交班级德育打分返回数据 = ",sub.toString());
                EventBus.getDefault().post(new EventCenter<>("EDUCLASS_SCORE_DETAIL_UPDATE",null));
                finish();
                break;
        }
    }

    @OnClick({R.id.title_back_img,R.id.activity_class_and_grade_education_list_check_mod_submit})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.activity_class_and_grade_education_list_check_mod_submit:
                dialog.show();
                List<AddBean> items = adapter.items;
                AddListBean addListBean = new AddListBean(items);
                LogUtil.e("新增分数的请求参数 = ", GsonUtils.toJson(addListBean));
                NetworkRequest.request(addListBean, CommonUrl.EDUCLASS_SCORE_SUB_ALL, Config.EDUCLASS_SCORE_SUB_MOD);
                break;
        }
    }
}
