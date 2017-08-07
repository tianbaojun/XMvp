package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClassAndGradeEducationListCheckScore;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClassAndGradeEducation;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
import com.zjhz.teacher.ui.adapter.ClassAndGradeEducationDetailAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.kit.ViewTools;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ClassAndGradeEducationDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.project_name)
    TextView projectName;
    @BindView(R.id.grade_name)
    TextView gradeNametv;
    @BindView(R.id.check_time)
    TextView checkTime;
    @BindView(R.id.check_person)
    TextView checkPerson;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.title_ll)
    LinearLayout title_ll;
    private String schemeName, schemeId, checkTimes;
    private String gradeName, gradeId, studentId, studentName,createUser;
    private List<ClassAndGradeEducationListCheckBean> list = new ArrayList<>();
    private ClassAndGradeEducationDetailAdapter adapter;
    private ClassAndGradeEducation classAndGradeEducation;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classandgrade_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        classAndGradeEducation = (ClassAndGradeEducation) bundle.getSerializable("ClassAndGradeEducation");
        schemeName =classAndGradeEducation.schemeName;
        schemeId =classAndGradeEducation.schemeId;
        gradeName =classAndGradeEducation.gradeName;
        gradeId =classAndGradeEducation.gradeId;
        studentId = classAndGradeEducation.studentId;
        checkTimes =classAndGradeEducation.checkTime;
        studentName = classAndGradeEducation.studentName;
        createUser = classAndGradeEducation.createUser;
        initView();
        getData();
    }

    private void initView() {
        titleTv.setText("班级德育");
        rightText.setVisibility(View.VISIBLE);
        title_ll.setVisibility(View.VISIBLE);
        refreshLayout.setColorSchemeColors(Constance.colors);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
        rightText.setText("编辑");
        rightText.setOnClickListener(this);
        titleBackImg.setOnClickListener(this);
        projectName.setText("方案名称：" + schemeName);
        gradeNametv.setText("检查对象：" + gradeName);
        checkTime.setText("检查时间：" + checkTimes.substring(0, 16));
        String name = SharePreCache.isEmpty(studentName) ? "　　　":studentName;
        checkPerson.setText("督导员：" + name);
        adapter = new ClassAndGradeEducationDetailAdapter(this, list);
        refreshListview.setAdapter(adapter);
    }


    private void getData() {
        dialog.setMessage("正在加载...");
        dialog.show();
        ClassAndGradeEducationListCheckScore mClassAndGradeEducationListParams = new ClassAndGradeEducationListCheckScore(gradeId, schemeId, checkTimes,studentId,"1", "1000");
        NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.EDUCLASS_SCORE, Config.EDUCLASS_SCORE_DETAIL);
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
            case Config.EDUCLASS_SCORE_DETAIL:
                dialog.dismiss();
                list.clear();
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("班级德育修改打分列表数据 = ", data.toString());
                List<ClassAndGradeEducationListCheckBean> beanList = GsonUtils.jsonToArray(ClassAndGradeEducationListCheckBean.class, data);
                if (beanList.size() > 0) {
                    list.addAll(beanList);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showShort("没有数据");
                }
                break;
            case Config.EDUCLASS_SCORE_DETAIL_UPDATE:
                getData();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()){
            case R.id.right_text:
                if (createUser.equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))){
                    Intent intent = new Intent(this, ClassAndGradeEducationListCheckModifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("project", schemeName);
                    bundle.putString("projectid", schemeId);
                    bundle.putString("class", gradeName);
                    bundle.putString("classid", gradeId);
                    bundle.putString("bespeakTime", checkTimes);
                    bundle.putString("studentId", studentId);
                    bundle.putString("studentName", studentName);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }else {
                    ToastUtils.showShort("没有编辑权限");
                }
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
