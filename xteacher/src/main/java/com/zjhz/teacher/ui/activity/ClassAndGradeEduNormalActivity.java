package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.ClassAndGradeEduNormalAdapter;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClassAndGradeEduNormalBean;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-09-05
 * Time: 14:23
 * Description: 班级德育普通老师查看   列表检查
 */
public class ClassAndGradeEduNormalActivity extends BaseActivity {

    @BindView(R.id.activity_class_and_grade_edu_normal_schem)
    TextView scheme;
    @BindView(R.id.activity_class_and_grade_edu_normal_clazz)
    TextView clazz;
    @BindView(R.id.activity_class_and_grade_edu_normal_time)
    TextView time;
    @BindView(R.id.activity_class_and_grade_edu_normal_list)
    ListView listView;

    private List<ClassAndGradeEduNormalBean> list = new ArrayList<>();
    ClassAndGradeEduNormalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_and_grade_edu_normal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        adapter = new ClassAndGradeEduNormalAdapter(this,list);
        listView.setAdapter(adapter);
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
            case Config.EDUCLASS_SCORE:
                dialog.dismiss();
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("班级德育打分列表数据 = ", data.toString());

                break;
            case Config.EDUCLASS_SCORE_SUB:
                dialog.dismiss();
                JSONObject sub = (JSONObject) event.getData();
                LogUtil.e("提交班级德育打分返回数据 = ", sub.toString());
                break;
        }
    }
}
