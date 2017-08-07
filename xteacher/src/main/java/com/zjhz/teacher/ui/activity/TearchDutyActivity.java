package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.CurrentDutyBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.FragAdapter;
import com.zjhz.teacher.ui.fragment.TeacherDutyFragment;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 教师值日
 * Created by Administrator on 2016/6/21.
 */
public class TearchDutyActivity extends BaseActivity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    public int dataWeek = 1; // 获取数据的周
    public String dutyId;
    private int totalWeek = 0,currentWeek = 0;//总周次、当前周、
    private final static String TAG = TearchDutyActivity.class.getSimpleName();
    private List<Fragment> fragments = new ArrayList<>();
    private boolean isLeader = false;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_viewpager);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initTitle();
        dialog.setMessage("获取当前周值日信息");
        dialog.show();
        isLeader();
        getCurrentDutyData();
    }
    private void initTitle() {
        titleTv.setText("教师值日");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getCurrentDutyData() {
        NetworkRequest.request(null, CommonUrl.currentDuty,Config.currentDuty);
    }

    private void isLeader(){
        Map<String,String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.ISLEADER,Config.ISLEADER);
    }

    @OnClick({R.id.right_icon0})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.right_icon0:
                startActivity(TeacherDutyAddActivity.class,"dutyId",dutyId);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.currentDuty:
                dialog.dismiss();
                JSONObject obj = (JSONObject) event.getData();
                try {
                    CurrentDutyBean bean = GsonUtils.toObject(obj.getJSONObject("data").toString(),CurrentDutyBean.class);
                    if (bean != null){
                        dutyId = bean.getDutyId();
                        getTotalWeek(bean);
                        getCurrentWeek(bean);
                        dataWeek = currentWeek;
                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.ISLEADER:
                dialog.dismiss();
                JSONObject obj2 = (JSONObject) event.getData();
                isLeader = obj2.optBoolean("data");
                /*if(isLeader){
                    addDuty.setVisibility(View.VISIBLE);
                }else{
                    addDuty.setVisibility(View.GONE);
                }*/
                break;
        }
    }
    //获取总的周数量
    private void getTotalWeek(CurrentDutyBean bean) {
        if (bean == null || SharePreCache.isEmpty(bean.getStartTime())){
            return;
        }
        String startTime = bean.getStartTime();
        String endTime = bean.getEndTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            Date startDate=format.parse(startTime);
            Date endDate = format.parse(endTime);
            start.setTime(startDate);
            end.setTime(endDate);
            int sumSunday = 0;
            while(start.compareTo(end) <= 0) {
                int w = start.get(Calendar.DAY_OF_WEEK);
                if(w == Calendar.SUNDAY)
                    sumSunday ++;
                //循环，每次天数加1
                start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
            }
            int dayofweek = DateUtil.dayForWeek(startTime);
            if (dayofweek == 7){
                totalWeek = sumSunday;
            }else {
                totalWeek = sumSunday + 1;
            }
            System.out.println("dataWeek:"+totalWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //获取当前日前为第几周
    private void getCurrentWeek(CurrentDutyBean bean) {
        if (bean == null || SharePreCache.isEmpty(bean.getStartTime())){
            return;
        }
        Calendar start = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        String startTime = bean.getStartTime();//值日开始日期
        String endTime = bean.getEndTime();//值日结束日期
        String currentTime = TimeUtil.getNowYMD();//当天日期
        //获取当前日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            start.setTime(format.parse(endTime));
            currentDate.setTime(format.parse(currentTime));
            //比较当天日期是否属于结束日期之前的日期
            int result = start.compareTo(currentDate);
            if (result >= 0){
                start.setTime(format.parse(startTime));
                int sumSunday = 0;
                while(start.compareTo(currentDate) <= 0) {
                    int w = start.get(Calendar.DAY_OF_WEEK);
                    if(w == Calendar.SUNDAY)
                        sumSunday ++;
                    //循环，每次天数加1
                    start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
                }
                int dayofweek = DateUtil.dayForWeek(startTime);
                if (dayofweek == 7){
                    currentWeek = sumSunday;
                }else {
                    currentWeek = sumSunday + 1;
                }
            }else {
                currentWeek = 1;
            }
            System.out.println("currentWeek:"+currentWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void initData(){
        for (int i = 0 ; i < totalWeek ; i++){
            TeacherDutyFragment teacherDutyFragment = new TeacherDutyFragment();
            teacherDutyFragment.initFragmentData(this);
            fragments.add(teacherDutyFragment);
        }
        FragAdapter fragadapter = new FragAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragadapter);
        viewPager.setCurrentItem(dataWeek - 1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dataWeek = position + 1;
//                Log.d("dataWeek",dataWeek+"");
//                viewPager.setCurrentItem(position);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public String getDutyId(){
        return dutyId;
    }
    public boolean getLeader(){
        return isLeader;
    }
}
