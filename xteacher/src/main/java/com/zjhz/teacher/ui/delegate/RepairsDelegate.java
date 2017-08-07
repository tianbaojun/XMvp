package com.zjhz.teacher.ui.delegate;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.RepairsProposerListActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/15.
 */
public class RepairsDelegate implements View.OnClickListener{
    private RepairsProposerListActivity activity;
    private String startTime;
    private String endTime;
    private int status = 0;
    private TextView startTime_tv,endTime_tv ,re_status;
    private ArrayList<String> repairStateArr = new ArrayList<>();
    public RepairsDelegate(RepairsProposerListActivity activity) {
        this.activity = activity;
    }
    public void initDelegat(){
        initView();
    }
    private void initView() {
        activity.drawer_layout_classes_name.setText("审批状态");
        activity.findViewById(R.id.drawer_layout_classes).setVisibility(View.GONE);
       activity.findViewById(R.id.drawer_layout_subject).setVisibility(View.GONE);
       activity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
       activity.findViewById(R.id.navigation_header_right).setOnClickListener(this);
       activity.findViewById(R.id.navigation_header_left).setOnClickListener(this);
       activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
       startTime_tv = (TextView) activity.findViewById(R.id.drawer_layout_date_start);
       startTime_tv.setOnClickListener(this);
       endTime_tv = (TextView) activity.findViewById(R.id.drawer_layout_date_end);
       endTime_tv.setOnClickListener(this);
        re_status = (TextView) activity.findViewById(R.id.repair_state);
        activity.findViewById(R.id.drawer_layout_state).setVisibility(View.GONE);
        re_status.setOnClickListener(this);

        //初始化维修状态
        String[] types =activity.getResources().getStringArray(R.array.repair_state);
        for (int i = 0; i < types.length; i++) {
            repairStateArr.add(types[i]);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_header_right:
                if (isRequest()){
                    if (activity.type == 0 ){
                        Intent intent = new Intent();
                        intent.setAction("MyRepairsFragment");
                        intent.putExtra("startTime",startTime);
                        intent.putExtra("endTime",endTime);
                        intent.putExtra("status",status);
                        intent.putExtra("isAdd",false);
                        activity.sendBroadcast(intent);
                    }else {
                        Intent intent = new Intent();
                        intent.setAction("WaitRepairsFragment");
                        intent.putExtra("startTime",startTime);
                        intent.putExtra("endTime",endTime);
                        intent.putExtra("status",status);
                        activity.sendBroadcast(intent);
                    }
                    clearText();
                    activity.drawer_layout.closeDrawer(activity.nv_drawer_layout);
                }
                break;
            case R.id.navigation_header_left:
                clearText();
                activity.drawer_layout.closeDrawer(activity.nv_drawer_layout);
                break;
            case R.id.drawer_layout_clear:
                clearText();
                break;
            case R.id.drawer_layout_date_start:
                selectDate(startTime_tv);
                break;
            case R.id.drawer_layout_date_end:
                selectDate(endTime_tv);
                break;
            case R.id.repair_state:
                selectStatus();
                break;
        }
    }
    private int index;
    private OptionsPickerView optionsPickerView;
    private void selectStatus() {
        if (optionsPickerView == null){
            optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker((ArrayList) repairStateArr, index, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    re_status.setText(repairStateArr.get(options1));
                    status = options1+1;
                    index = options1;
                }
            });
        }
        optionsPickerView.show();
    }
    private void clearText(){
        startTime = "";
        endTime = "";
        startTime_tv.setText("");
        endTime_tv.setText("");
        re_status.setText("");
    }

    private boolean isRequest(){
        startTime = startTime_tv.getText().toString().trim();
        if (SharePreCache.isEmpty(startTime)){
            ToastUtils.showShort("请选择起始时间");
            return false;
        }
        endTime = endTime_tv.getText().toString().trim();
        if (SharePreCache.isEmpty(endTime)){
            ToastUtils.showShort("请选择结束时间");
            return false;
        }
        return true;
    }

    /**
     * 选择日期
     */
    private void selectDate(final TextView view) {
        TimePickerView pvTime = new TimePickerView(activity, TimePickerView.Type.YEAR_MONTH_DAY,null,0);
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
                view.setText(TimeUtil.getYMD(date));
            }
        });
    }
}
