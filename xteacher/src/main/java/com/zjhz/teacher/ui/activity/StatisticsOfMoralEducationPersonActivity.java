package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.adapter.StatisticsOfMoralEducationClassOrPersonAdapter;
import com.zjhz.teacher.ui.view.OptionsGradeView;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by order on 2016/9/23.
 */
public class StatisticsOfMoralEducationPersonActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private final static String TAG = StatisticsOfMoralEducationPersonActivity.class.getSimpleName();
    private TextView titleBackImg;
    private TextView titleTv;
    private TextView right_tv;
    private ListView refreshListview;
    private RefreshLayout refreshLayout;
    StatisticsOfMoralEducationClassOrPersonAdapter adapter;
    public int index_grade;
    public int index_clazz;
//    public ArrayList<String> grades = AppContext.getInstance().grades;
//    public ArrayList<ArrayList<String>> clazzs = AppContext.getInstance().clazzs;
//    public List<Grade> clazzsGrades = AppContext.getInstance().gradeBeans;
    OptionsGradeView mOptionsGradeView;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_of_moraleducation_class_person);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initTitle();
        initView();
        initListener();
        getData();
    }
    private void initTitle() {
        titleBackImg = (TextView) findViewById(R.id.title_back_img);
        titleTv = (TextView) findViewById(R.id.title_tv);
        right_tv = (TextView) findViewById(R.id.right_text);
        refreshListview = (ListView) findViewById(R.id.refresh_listview);
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        titleTv.setText("德育统计");
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText("二年级四班");
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        getData();
    }

    private void getData() {
        MainActivity.getClassz();
    }

    private void initView() {
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshListview.setSelector(R.color.transparent);
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGrade();
            }
        });
        mOptionsGradeView = new OptionsGradeView(this);
//        adapter = new StatisticsOfMoralEducationClassOrPersonAdapter(this, this);
//        refreshListview.setAdapter(adapter);

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
                    if ( AppContext.getInstance().clazzs.get(options1).size() > 0 && option2 <  AppContext.getInstance().clazzs.get(options1).size()) {
                        right_tv.setText( AppContext.getInstance().clazzs.get(options1).get(option2));
                        index_grade = options1;
                        index_clazz = option2;
                        LogUtil.e("index_grade = " + index_grade + " index_clazz = " + index_clazz);
                        String gradeId = AppContext.getInstance().gradeBeans.get(index_grade).getGradeId();
                        String classId = AppContext.getInstance().gradeBeans.get(index_grade).getDetail().get(index_clazz).getClassId();
                        LogUtil.e("年级IDindex_grade = " + index_grade + " 班级IDindex_clazz = " + index_clazz);
                        LogUtil.e("年级ID = " + gradeId + " 班级ID = " + classId);
                    } else {
                        right_tv.setText("请选择班级");
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
}
