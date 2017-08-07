package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ListForScoreParams;
import com.zjhz.teacher.NetworkRequests.request.SendScoreToParentParams;
import com.zjhz.teacher.NetworkRequests.response.GradeScoreBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.GradeScoreAdapter;
import com.zjhz.teacher.ui.adapter.SpinerAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Administrator on 2016/8/29.
 */
public class GradeScoreActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.hint_tv)
    TextView hint_tv;
    @BindView(R.id.right_icon0)
    ImageView rightIcon0;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.ll)
    LinearLayout ll;
    private GradeScoreAdapter adapter;
    private PopupWindow  popupWindow;
    private String stuscoreClassId,year,semester,stuscoreType,subjectName,className,createUser,scoreMethod;
    private int page =1,pageSize = 12,total = 0;
    private ArrayList<GradeScoreBean> scoreBean = new ArrayList<>();
    private TextView max_tv,min_tv,avg_tv,type_tv;
    private ImageView max_iv,min_iv,avg_iv;
//    private RelativeLayout score_type_rl;
    private ListView type_lv;
    private String maxValue = "",minValue = "",avgValue = "";
    private boolean isMax = false,isMin = false,isAvg = false;
//    private SpinerPopWindow spinerPopWindow;
    private SpinerAdapter spinerAdapter ;
    private List<String> strings = new ArrayList<>();
    private int typeOption = 0;
    private boolean isShow;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_score);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        stuscoreClassId = intent.getStringExtra("stuscoreClassId");
        year = intent.getStringExtra("year");
        semester = intent.getStringExtra("semester");
        stuscoreType = intent.getStringExtra("stuscoreType");
        subjectName = intent.getStringExtra("subjectName");
        className = intent.getStringExtra("className");
        createUser = intent.getStringExtra("createUser");
        scoreMethod = intent.getStringExtra("scoreMethod");
        if (SharePreCache.isEmpty(stuscoreClassId)){
            ToastUtils.showShort("班级为空");
            return;
        }
        initView();
        dialog.setMessage("正在获取列表...");
        dialog.show();
        getList();
    }

    private void getList() {
        NetworkRequest.request(new ListForScoreParams(stuscoreClassId,page,pageSize), CommonUrl.listForScore,"listForScore");
    }

    private void initView() {
        titleTv.setText("班级成绩查询");
        // 年-期类型-科目-班级
        hint_tv.setText(year+"-"+semester+stuscoreType+"-"+subjectName+"-"+className);
        if (createUser.equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))){
            rightIcon0.setVisibility(View.VISIBLE);
        }
        rightIcon0.setImageResource(R.mipmap.send_score_icon);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new GradeScoreAdapter(scoreBean,this);
        refreshListview.setAdapter(adapter);
//        spinerAdapter.refreshData(strings, 0);
//        spinerPopWindow = new SpinerPopWindow(this);
//        spinerPopWindow.setAdatper(spinerAdapter);
//        spinerPopWindow.setItemListener(new SpinerPopWindow.IOnItemSelectListener() {
//            @Override
//            public void onItemClick(int pos) {
//                if (pos >= 0 && pos < strings.size()) {
//                    type_tv.setText(strings.get(pos));
//                    typeOption = pos + 1;
//                }
//            }
//        });
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
                if (TextUtils.isEmpty(maxValue)&&TextUtils.isEmpty(minValue)&&TextUtils.isEmpty(avgValue)){
                     ToastUtils.showShort("没有数据");
                }else {
                    if (popupWindow == null){
                        View views = LayoutInflater.from(this).inflate(R.layout.score_popwindow,null);
                        max_tv = (TextView) views.findViewById(R.id.max_tv);
                        min_tv = (TextView) views.findViewById(R.id.min_tv);
                        avg_tv = (TextView) views.findViewById(R.id.avg_tv);
                        max_iv = (ImageView) views.findViewById(R.id.max_iv);
                        min_iv = (ImageView) views.findViewById(R.id.min_iv);
                        avg_iv = (ImageView) views.findViewById(R.id.avg_iv);
                        type_lv = (ListView) views.findViewById(R.id.type_lv);
                        type_tv = (TextView) views.findViewById(R.id.type);
//                    score_type_rl = (RelativeLayout) views.findViewById(R.id.score_type_rl);
                        strings.add("分数");
                        strings.add("优、良、及格");
                        strings.add("A、B、C");
                        max_tv.setText("最高分："+maxValue);
                        min_tv.setText("最低分："+minValue);
                        avg_tv.setText("平均分："+avgValue);
                        spinerAdapter = new SpinerAdapter(this,strings);
                        type_lv.setAdapter(spinerAdapter);
                        popupWindow = new PopupWindow(views, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setFocusable(true);
                    }
                    type_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            type_tv.setText(strings.get(position));
                            typeOption = position + 1;
                            isShow = false;
                            type_lv.setVisibility(View.INVISIBLE);
                        }
                    });
                    if (!popupWindow.isShowing()){
                        popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
                    }
                }
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
        if (scoreBean.size() < total){
            page++;
            getList();
        }else {
            stopRefresh();
        }
    }
    private void stopRefresh(){
        if (refreshLayout != null){
            refreshLayout.setLoading(false);
            refreshLayout.setRefreshing(false);
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
            case "listForScore":
                stopRefresh();
                dialog.dismiss();
                JSONObject j = (JSONObject) e.getData();
                List<GradeScoreBean> beanList = GsonUtils.toArray(GradeScoreBean.class,j);
                total = j.optInt("total");
                if (beanList != null && beanList.size() > 0){
                    if (page == 1){
                        scoreBean.clear();
                        setSendData(beanList.get(beanList.size() - 1));
                    }
                    beanList.remove(beanList.size() - 1);
                    scoreBean.addAll(beanList);
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showShort("没有数据");
                }
                break;
            case "sendScoreToParent":
                clear();
                ToastUtils.showShort("发送成功");
                break;
        }
    }
    private void setSendData(GradeScoreBean bean) {
        maxValue = bean.getMax();
        minValue = bean.getMin();
        avgValue = bean.getAvg();
    }
    //分制选择
    public void onSelectType(View view){
        if (isShow){
            isShow = false;
            type_lv.setVisibility(View.INVISIBLE);
        }else {
            isShow = true;
            type_lv.setVisibility(View.VISIBLE);
        }
//        if (spinerPopWindow != null){
//            spinerPopWindow.setWidth(type_tv.getWidth());
//            spinerPopWindow.showAsDropDown(type_tv);
//        }
    }
    //确定
    public void onSure(View view){
        if (typeOption == 0){
             ToastUtils.showShort("选择分制");
            return;
        }
        if (!isMax && !isMin && !isAvg){
             ToastUtils.showShort("选择发送内容");
            return;
        }
        String msg = subjectName+stuscoreType+":";
        if (isMax){
            msg+="最高分："+maxValue+",";
        }
        if (isMin){
            msg+="最低分："+minValue+",";
        }
        if (isAvg){
            msg+="平均分："+avgValue;
        }
        NetworkRequest.request(new SendScoreToParentParams(stuscoreClassId,msg,typeOption+"",scoreMethod),"StuScoreDetailService.sendScoreToParent","sendScoreToParent");
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
    public void onMax(View view){
        if (isMax){
            isMax = false;
            max_iv.setImageResource(R.mipmap.score_select);
        }else {
            isMax = true;
            max_iv.setImageResource(R.mipmap.score_selected);
        }
    }
    public void onMin(View view){
        if (isMin){
            isMin = false;
            min_iv.setImageResource(R.mipmap.score_select);
        }else {
            isMin = true;
            min_iv.setImageResource(R.mipmap.score_selected);
        }
    }
    public void onAvg(View view){
        if (isAvg){
            isAvg = false;
            avg_iv.setImageResource(R.mipmap.score_select);
        }else {
            isAvg = true;
            avg_iv.setImageResource(R.mipmap.score_selected);
        }
    }
    public void onCancle(View view){
        clear();
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
    private void clear(){
        type_tv.setText("");
        typeOption = 0;
        isMax = false;
        max_iv.setImageResource(R.mipmap.score_select);
        isMin = false;
        min_iv.setImageResource(R.mipmap.score_select);
        isAvg = false;
        avg_iv.setImageResource(R.mipmap.score_select);
    }
}
