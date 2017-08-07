package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.StuScoreSchemaClassParams;
import com.zjhz.teacher.NetworkRequests.request.TermAndTypeParams;
import com.zjhz.teacher.NetworkRequests.response.ScoreYearBean;
import com.zjhz.teacher.NetworkRequests.response.StuScoreSchemaClassBean;
import com.zjhz.teacher.NetworkRequests.response.SubjectBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.ScoreAdapter;
import com.zjhz.teacher.ui.delegate.ScoreDelegate;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**
 * 成绩
 * Created by Administrator on 2016/8/29.
 */
public class ScoreActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon0)
    ImageView rightIcon0;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    public int page = 1;
    private int pageSize = 12,total = 0;
    private ScoreDelegate scoreDelegate;
    private ScoreAdapter scoreAdapter;
    private List<StuScoreSchemaClassBean> stuBean = new ArrayList<>();/*
    public List<GradeClsBean> beans;
    public ArrayList<String> grades = new ArrayList<>();
    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();*/
    public List<SubjectBeans> subObj = new ArrayList<>();//科目集合
    public ArrayList<String> subject = new ArrayList<>();
    public ArrayList<ScoreYearBean> yearBean = new ArrayList<>();
    public ArrayList<String> yearStr = new ArrayList<>();
    public ArrayList<ScoreYearBean> tremBean = new ArrayList<>();
    public ArrayList<String> tremStr = new ArrayList<>();
    public ArrayList<ScoreYearBean> scoreBean = new ArrayList<>();
    public ArrayList<String> scoreStr = new ArrayList<>();
    public String yearId = "";
    public String termId = "";
    public String soceId = "";
    public String subId = "";
    public String classId = "";
    public String startTime = "";
    public String endTime = "";
    public boolean isFilter = false;

    private DefaultLoadingLayout loadingLayout;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        initView();
        scoreDelegate = new ScoreDelegate(this);
        scoreDelegate.initDelegate();
        getList();
        MainActivity.getClassz();
//        getGradeAndClass();
        getSubjectData();
        getTermAndType();
    }

    private void initView() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        titleTv.setText("成绩列表");
        rightIcon0.setVisibility(View.VISIBLE);
        rightIcon0.setImageResource(R.mipmap.select_icon);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        scoreAdapter = new ScoreAdapter(stuBean,this);
        refreshListview.setAdapter(scoreAdapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScoreActivity.this,GradeScoreActivity.class);
                intent.putExtra("stuscoreClassId",stuBean.get(position).getStuscoreClassId());
                intent.putExtra("year",stuBean.get(position).getYearVal());
                intent.putExtra("semester",stuBean.get(position).getSemesterVal());
                intent.putExtra("stuscoreType",stuBean.get(position).getStuscoreTypeVal());
                intent.putExtra("subjectName",stuBean.get(position).getSubjectName());
                intent.putExtra("className",stuBean.get(position).getClassName());
                intent.putExtra("createUser",stuBean.get(position).getCreateUser());
                intent.putExtra("scoreMethod",stuBean.get(position).getScoreMethod());
                startActivity(intent);
            }
        });
    }
    @OnClick({R.id.title_back_img,R.id.right_icon0})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_icon0:
                scoreDelegate.openDrawLayout();
                break;
        }
    }
    @Override
    public void onRefresh() {
        page = 1;
        getList();
    }
    @Override
    public void onLoad() {
        if (stuBean.size() < total){
            page ++;
            getList();
        }else {
            refreshLayout.setLoading(false);
        }
    }
    private void stopRefresh(){
        if (refreshLayout != null){
            refreshLayout.setLoading(false);
            refreshLayout.setRefreshing(false);
        }
    }

   /* private void getGradeAndClass() {
        NetworkRequest.request(null, CommonUrl.gradeClsList,"ScogradeClsList");
    }*/
    private void getSubjectData() {
        NetworkRequest.request(null,CommonUrl.homeworkSubjectList,"ScosubList");
    }
    private void getTermAndType() {
        NetworkRequest.request(new TermAndTypeParams("SCHOOL_CALENDAR_YEAR"),CommonUrl.parentKeyTochilds,"SCHOOL_CALENDAR_YEAR");
        NetworkRequest.request(new TermAndTypeParams("SCHOOL_CALENDAR_SEMESTER"),CommonUrl.parentKeyTochilds,"SCHOOL_CALENDAR_SEMESTER");
        NetworkRequest.request(new TermAndTypeParams("SCHOOL_SCORE_TYPE"),CommonUrl.getStuScoreAllType,"SCHOOL_SCORE_TYPE");
    }

    /**筛选请求*/
    public void getList(){
        if (isFilter){
            StuScoreSchemaClassParams stuScoreSchemaClassParams = new StuScoreSchemaClassParams(page,pageSize,yearId,termId,soceId,classId,subId,startTime,endTime,"2");
            LogUtil.e("成绩筛选参数 = ",GsonUtils.toJson(stuScoreSchemaClassParams));
            NetworkRequest.request(stuScoreSchemaClassParams,CommonUrl.listForClass,"listForClass");
        }else {
            NetworkRequest.request(new StuScoreSchemaClassParams(page,pageSize,"2"),CommonUrl.listForClass,"listForClass");
        }
    }
    @Subscribe
    public void onEventMainThread(EventCenter e){
        switch (e.getEventCode()){
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                stopRefresh();
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                stopRefresh();
                dialog.dismiss();
                break;
            /*case "ScogradeClsList":
                stopRefresh();
                dialog.dismiss();
                JSONObject json = (JSONObject) e.getData();
                beans = GsonUtils.toArray(GradeClsBean.class,json);
                if (beans != null && beans.size() > 0){
                    for (int i = 0 ; i < beans.size() ; i ++){
                        grades.add(beans.get(i).getName());//年级
                        List<ClassesBeans> classesList = beans.get(i).getDetail();//班级
                        ArrayList<String> cls = new ArrayList<>();
                        if (classesList != null && classesList.size() > 0){
                            for (int j = 0 ; j < classesList.size() ; j++){
                                cls.add(classesList.get(j).getName());
                            }
                        }
                        clazzs.add(cls);
                    }
                }
                break;*/
            case "ScosubList":
                JSONObject obj = (JSONObject) e.getData();
                subObj = GsonUtils.toArray(SubjectBeans.class,obj);
                if (subObj.size() > 0){
                    for (int i = 0 ;i < subObj.size() ; i ++){
                        subject.add(subObj.get(i).getName());
                    }
                }
                break;
            case "SCHOOL_CALENDAR_YEAR":
                JSONObject obj1 = (JSONObject) e.getData();
                yearBean = (ArrayList<ScoreYearBean>) GsonUtils.jsonToArray(ScoreYearBean.class,obj1);
                if (yearBean.size() > 0){
                    for (int i = 0 ; i < yearBean.size() ; i ++){
                        yearStr.add(yearBean.get(i).getParamValue());
                    }
                }
                break;
            case "SCHOOL_CALENDAR_SEMESTER":
                JSONObject obj2 = (JSONObject) e.getData();
                tremBean = (ArrayList<ScoreYearBean>) GsonUtils.jsonToArray(ScoreYearBean.class,obj2);
                if (tremBean.size() > 0){
                    for (int i = 0 ; i < tremBean.size() ; i ++){
                        tremStr.add(tremBean.get(i).getParamValue());
                    }
                }
                break;
            case "SCHOOL_SCORE_TYPE":
                JSONObject obj3 = (JSONObject) e.getData();
                scoreBean = (ArrayList<ScoreYearBean>) GsonUtils.jsonToArray(ScoreYearBean.class,obj3);
                if (scoreBean.size() > 0){
                    for (int i = 0 ; i < scoreBean.size() ; i ++){
                        scoreStr.add(scoreBean.get(i).getParamValue());
                    }
                }
                break;
            case "listForClass":
                stopRefresh();
                dialog.dismiss();
                JSONObject obj4 = (JSONObject) e.getData();
                try {
                    total = obj4.getInt("total");
                   List<StuScoreSchemaClassBean> stuBeans = GsonUtils.jsonToArray(StuScoreSchemaClassBean.class,obj4);
                    if (stuBeans != null && stuBeans.size() > 0 ){
                        if (page == 1){
                            stuBean.clear();
                            stuBean.addAll(stuBeans);
                        }else {
                            stuBean.addAll(stuBeans);
                        }
                        loadingLayout.onDone();
                        scoreAdapter.notifyDataSetChanged();
                    }else {

                        loadingLayout.onEmpty();
                        if (page == 1){
                            stuBean.clear();
                            scoreAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.showShort("没有数据");
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }
}
