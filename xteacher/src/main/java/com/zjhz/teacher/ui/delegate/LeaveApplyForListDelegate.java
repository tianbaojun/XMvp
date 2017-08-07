package com.zjhz.teacher.ui.delegate;

import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.LeaveApplyForListActivity;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表侧拉框
 */
public class LeaveApplyForListDelegate implements View.OnClickListener {

    LeaveApplyForListActivity activity;

    public LeaveApplyForListDelegate(LeaveApplyForListActivity activity,OnTextViewClick onTextViewClick) {
        this.activity = activity;
        this.mOnTextViewClick = onTextViewClick;
    }

    public void initialize() {
        initView();
        setUpDrawer();
        initData();
    }

    private void initData() {
        states.add("审批中");
        states.add("已完成");

        types.add("婚假");
        types.add("病假");
        types.add("事假");

    }

    private void initView() {
        activity.name.setText("审批状态");
        activity.subject.setText("类型");

        activity.drawer_layout_classes.setVisibility(View.GONE);
        activity.drawer_layout_subject.setVisibility(View.GONE);
        activity.drawer_layout_state.setVisibility(View.GONE);
        activity.navigationHeaderTitle.setText("筛选");
        activity.navigationHeaderLeft.setText("取消");
        activity.navigationHeaderLeft.setOnClickListener(this);
        activity.navigationHeaderRight.setText("确定");
        activity.navigationHeaderRight.setOnClickListener(this);
        activity.drawerLayoutDateStart.setOnClickListener(this);
        activity.drawerLayoutDateEnd.setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
    }

    public void openDrawer() {
        if (activity.drawerLayout == null)
            return;
        activity.drawerLayout.openDrawer(activity.nvDrawerLayout);
    }

    public void closeDrawer() {
        if (activity.drawerLayout == null)
            return;
        activity.drawerLayout.closeDrawer(activity.nvDrawerLayout);
    }

    private void setUpDrawer() {
        if (activity.drawerLayout == null) {
            return;
        }
    }

    List<String> states = new ArrayList<>();
    List<String> types = new ArrayList<>();
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_header_left:
                closeDrawer();
                break;
            case R.id.navigation_header_right:  //  TODO 提交网络
                mOnTextViewClick.onTextViewClick();
                closeDrawer();
                break;
            case R.id.drawer_layout_classes:
                BaseUtil.selectSubject(states,activity,activity.state);
                break;
            case R.id.drawer_layout_subject:
                BaseUtil.selectSubject(types,activity,activity.type);
                break;
            case R.id.drawer_layout_date_start:
                TimeUtil.selectDate(activity,activity.drawerLayoutDateStart);
                break;
            case R.id.drawer_layout_date_end:
                TimeUtil.selectDate(activity,activity.drawerLayoutDateEnd);
                break;
            case R.id.drawer_layout_clear:
                activity.state.setText("");
                activity.type.setText("");
                activity.drawerLayoutDateStart.setText("");
                activity.drawerLayoutDateEnd.setText("");
                break;
        }
    }

    public OnTextViewClick mOnTextViewClick;

    public interface OnTextViewClick{
        void onTextViewClick();
    }
}
