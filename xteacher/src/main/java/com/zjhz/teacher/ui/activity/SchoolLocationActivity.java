package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolCurrentLocationParams;
import com.zjhz.teacher.NetworkRequests.request.SchoolSideRouteParams;
import com.zjhz.teacher.NetworkRequests.response.SchoolLocationBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.SchoolLocationAdapter;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-06
 * Time: 16:57
 * Description: 校内定位
 * Modify: fei.wang  2016.7.19
 */
public class SchoolLocationActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.location_lv)
    ScrollViewWithListView locationLv;
    @BindView(R.id.location_ll)
    LinearLayout location_ll;
    @BindView(R.id.location_name)
    TextView locationName;
    @BindView(R.id.location_address)
    TextView locationAddress;
    @BindView(R.id.location_time)
    TextView locationTime;

    private SchoolLocationAdapter adapter;
    private String studentName = "";
    private String cardNum = "";//6E7BEF89
    private String flag;
    private String date = TimeUtil.getNowYMD();
    private String historyDate = "";
    private ArrayList<String> typeString = new ArrayList<>();
    private final static String TAG = SchoolLocationActivity.class.getSimpleName();
    private boolean isHistory = false;
    private boolean isShow = true;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_location);
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuNameKey,"请选择学生");
        SharedPreferencesUtils.removeKey(ConstantKey.lastStuRicardKey);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        initDatas();
        adapter = new SchoolLocationAdapter(this);
        locationLv.setAdapter(adapter);
        locationLv.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initView() {
        titleTv.setText("校内定位");
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("请选择学生");
    }
    private void initDatas() {
        typeString.add("上午");
        typeString.add("下午");
    }
    @OnClick({R.id.title_back_img, R.id.right_text,R.id.current_location, R.id.current_morning,R.id.current_afternoon,R.id.current_histore})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                isHistory = false;
                Intent intent = new Intent(this, StudentsCurrentPositionActivity.class);
                intent.putExtra("from",1);
                startActivityForResult(intent,1);
                break;
            case R.id.current_location:
                isHistory = false;
                if (isRequest()){
                    getCurrentLoactionData();
                }
                break;
            case R.id.current_morning:
                isHistory = false;
                if (isRequest()){
                    flag = 0+"";
                    dialog.setMessage("正在获取上午...");
                    dialog.show();
                    getSideRouteLoactionData(date);
                }
                break;
            case R.id.current_afternoon:
                isHistory = false;
                if (isRequest()){
                    flag = 1+"";
                    dialog.setMessage("正在获取下午...");
                    dialog.show();
                    getSideRouteLoactionData(date);
                }
                break;
            case R.id.current_histore:
                isHistory = true;
                if (isRequest()){
                    selectData();
                }
                break;
        }
    }
    private boolean isRequest(){
        if (SharePreCache.isEmpty(cardNum) && SharePreCache.isEmpty(studentName)){
            Intent intent = new Intent(this, StudentsCurrentPositionActivity.class);
            ToastUtils.showShort("请先选择学生");
            if (isHistory){
                intent.putExtra("from",2);
            }else {
                intent.putExtra("from",1);
            }
            startActivityForResult(intent,1);
            return false;
        }
        if (SharePreCache.isEmpty(cardNum) && !SharePreCache.isEmpty(studentName)){
            ToastUtils.showShort("无法获取该学生位置信息");
            return false;
        }
        return true;
    }
//    private int index1,index2,index3,index4;
    private void selectData() {
        TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY_TYPE,typeString,1);
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
            public void onTimeSelect(Date date,String type) {
                dialog.setMessage("正在获取历史数据...");
                dialog.show();
                flag = type;
                historyDate = TimeUtil.getYMD(date);
                getSideRouteLoactionData(historyDate);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) {
                cardNum = data.getStringExtra("cardNum");
                studentName = data.getStringExtra("studentName");
                historyDate = data.getStringExtra("historyDate");
                flag = data.getStringExtra("historyType");
                rightText.setText(studentName);
                if (SharePreCache.isEmpty(cardNum)){
                    ToastUtils.showShort("该学生RfidCard为空,不能获取该学生当前位置");
                    if (isHistory){
                        initrouteData(null);
                    }else {
                        initCurrentTitleData(null);
                    }
                }else {
                    if (isHistory){
                        getSideRouteLoactionData(historyDate);
                    }else {
                        getCurrentLoactionData();
                    }
                }
            }
        }else {
            if (requestCode == 2){
                finish();
            }
        }
    }
    private void getCurrentLoactionData() {
        dialog.setMessage("正在获取当前位置...");
        dialog.show();
        NetworkRequest.request(new SchoolCurrentLocationParams(cardNum), CommonUrl.schoolcurrentlocation, Config.currentlocation);
    }
    private void getSideRouteLoactionData(String date) {
        String time = date;
        NetworkRequest.request(new SchoolSideRouteParams(cardNum,time,flag), CommonUrl.inSideRouteList, Config.SideRoutelocation);
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("获取数据失败");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                ToastUtils.showShort("获取数据失败");
                break;
            case  Config.currentlocation://当前
                dialog.dismiss();
                JSONObject jsonObject = (JSONObject) event.getData();
                List<SchoolLocationBean> bean = GsonUtils.toArray(SchoolLocationBean.class, jsonObject);
                if (bean != null && bean.size() > 0 ){
                    initCurrentTitleData(bean);
                    adapter.setShowCurrent(true);
                    notifydata(bean);
                }else {
//                    date = TimeUtil.getNowYMD();
                    initCurrentTitleData(null);
                    if (isShow){
                        isShow = false;
                        ToastUtils.showShort("没有数据");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    isShow = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
                break;
            case  Config.SideRoutelocation://上下午
                dialog.dismiss();
                JSONObject json2 = (JSONObject) event.getData();
                List<SchoolLocationBean> sideRoutelocation_beans2 = GsonUtils.toArray(SchoolLocationBean.class, json2);
                if (sideRoutelocation_beans2 != null && sideRoutelocation_beans2.size() > 0) {
                    initrouteData(sideRoutelocation_beans2);
                    adapter.setShowCurrent(false);
                    notifydata(sideRoutelocation_beans2);
                }else {
                    initrouteData(null);
                    if (isShow){
                        isShow = false;
                        ToastUtils.showShort("没有数据");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    isShow = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
                break;
        }
    }
    //区间显示title
    private void initrouteData(List<SchoolLocationBean> beans2) {
        location_ll.setVisibility(View.VISIBLE);
        rightText.setText(studentName);
        locationName.setText("定位人："+studentName);
        if (beans2 != null && beans2.size() > 0){
            if (beans2.size() > 1){
                locationAddress.setText("定位区间："+beans2.get(0).getStationAddress() +"——"+beans2.get(beans2.size() - 1).getStationAddress());
                locationTime.setText("定位时间："+beans2.get(0).getRecordTime() +"——"+beans2.get(beans2.size() - 1).getRecordTime());
            }else if (beans2.size() == 1){
                locationAddress.setText("定位区间："+beans2.get(0).getStationAddress());
                locationTime.setText("定位时间："+beans2.get(0).getRecordTime());
            }
        } else {
            locationAddress.setText("定位区间：");
            locationTime.setText("定位时间：");
            adapter.setBeans(new ArrayList<SchoolLocationBean>());
            adapter.notifyDataSetChanged();
        }
    }
    //当前定位显示title
    private void initCurrentTitleData(List<SchoolLocationBean> bean ) {
        location_ll.setVisibility(View.VISIBLE);
        rightText.setText(studentName);
        locationName.setText("定位人："+studentName);
        if (bean != null && bean.size() > 0){
            locationAddress.setText("当前位置："+bean.get(0).getStationAddress());
            locationTime.setText("当前时间："+bean.get(0).getRecordTime());
            date = bean.get(0).getRecordTime().split(" ")[0];
        }else {
            locationAddress.setText("当前位置： ");
            locationTime.setText("当前时间：");
            adapter.setBeans(new ArrayList<SchoolLocationBean>());
            adapter.notifyDataSetChanged();
        }
    }
    private void notifydata(List<SchoolLocationBean> data){
        adapter.setBeans(data);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        rightText.setText("");
    }
}
