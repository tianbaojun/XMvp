package com.zjhz.teacher.ui.delegate;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.VisitorListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.VisitorManagerActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.TimeUtil;

import java.util.ArrayList;

import pro.kit.ViewTools;

/**
 * Created by zzd on 2017/4/18.
 */

public class VisitDelegate extends XDelegate<VisitorManagerActivity, VisitorListBean> implements View.OnClickListener {

    private static String name = "", state = "", startTime = "", endTime = "";
    private static int stateInt = 0;
    private ArrayList<String> stateList = new ArrayList<String>();
    private TextView visitNameTv,visitStateTv, visitStartTimeTv, visitEndTimeTv;

    private LinearLayout visitStateLL;

    private int useRole;

    public VisitDelegate(Context context,IDrawerLayout drawerLayout) {
        super(context, drawerLayout);
    }

    public void setUseRole(int useRole) {
        this.useRole = useRole;
    }

    @Override
    public void initialize() {


        visitStateLL = (LinearLayout) delegateLayout.findViewById(R.id.visit_state_delegate_ll);
        visitStartTimeTv = (TextView) delegateLayout.findViewById(R.id.visit_start_time);
        visitEndTimeTv = (TextView) delegateLayout.findViewById(R.id.visit_end_time);


        visitNameTv= (TextView)delegateLayout.findViewById(R.id.visit_name_delegate);
        visitStateTv = (TextView) delegateLayout.findViewById(R.id.visit_state_delegate_tv);


        visitStateLL.setOnClickListener(this);
        visitStartTimeTv.setOnClickListener(this);
        visitEndTimeTv.setOnClickListener(this);

//        activity.navigationHeaderLeft.setOnClickListener(this);
//        activity.navigationHeaderRight.setOnClickListener(this);
//        activity.visitStateLL.setOnClickListener(this);
//        activity.visitStartTimeTv.setOnClickListener(this);
//        activity.visitEndTimeTv.setOnClickListener(this);
//        activity.drawerClearTv.setOnClickListener(this);

        stateList.add("全部");
        stateList.add("草稿");
        stateList.add("待拜访");
        stateList.add("拜访中");
        stateList.add("拜访完成");
        stateList.add("系统关闭");
    }

    @Override
    public void readStateOnOpen() {

        visitNameTv.setText(name);
        visitStateTv.setText(state);
        visitStartTimeTv.setText(startTime);
        visitEndTimeTv.setText(endTime);
//        activity.visitNameTv.setText(state);
//        activity.visitStateTv.setText(state);
//        activity.visitStartTimeTv.setText(startTime);
//        activity.visitEndTimeTv.setText(endTime);
    }

    @Override
    public void saveStateOnClose() {
        name = visitNameTv.getText().toString();
        state = visitStateTv.getText().toString();
        startTime = visitStartTimeTv.getText().toString();
        endTime = visitEndTimeTv.getText().toString();

//        name = activity.visitNameTv.getText().toString();
//        state = activity.visitStateTv.getText().toString();
//        startTime = activity.visitStartTimeTv.getText().toString();
//        endTime = activity.visitEndTimeTv.getText().toString();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.navigation_header_right:
                ViewTools.hideSoftInput((Activity) context);
                //TODO 通过接口刷选
                filter(visitNameTv.getText().toString(), stateInt,
                        visitStartTimeTv.getText().toString(), visitEndTimeTv.getText().toString());
                break;
            case R.id.navigation_header_left:
                ViewTools.hideSoftInput((Activity) context);
                break;

            case R.id.visit_state_delegate_ll:
                ViewTools.hideSoftInput((Activity) context);
                setOnStateClick(stateInt);
                break;
            case R.id.visit_start_time:
                ViewTools.hideSoftInput((Activity) context);
                TimeUtil.selectDate(context, visitStartTimeTv);
                break;
            case R.id.visit_end_time:
                ViewTools.hideSoftInput((Activity) context);
                TimeUtil.selectDate(context, visitEndTimeTv);
                break;
            case R.id.drawer_layout_clear:
                ViewTools.hideSoftInput((Activity) context);
                break;
        }
    }

    private void setOnStateClick(int state) {
        OptionsPickerView<String> optionsPickerView = new OptionsPickerView<String>(context);

        optionsPickerView.setPicker(stateList, state, 0, 0);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                visitStateTv.setText(stateList.get(options1));
                stateInt = options1;
            }
        });
        optionsPickerView.show();
    }

    //筛选
    private void filter(String name, int status, String startDate, String endDate) {
        dataBean.setPage(1);
//        dataBean.setPageSize(CommonUrl.PAGE_SIZE);
//        dataBean.setTeacherName(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserNameKey));
        dataBean.setVisitorName(name);
        dataBean.setAppointmentTimeStart(startDate);
        dataBean.setAppointmentTimeEnd(endDate);
        if (status != 0) {
            if (useRole != 2)
                dataBean.setVisitStatus(status - 1 + "");
            else
                dataBean.setVisitStatus(status + "");
        }
        else {
            dataBean.setVisitStatus("");
        }
        NetworkRequest.request(dataBean, CommonUrl.VISITOR_LIST, "visitor_list");
    }

    @Override
    public void resetData() {
        ((TextView)delegateLayout.findViewById(R.id.visit_name_delegate)).setText("");
        ((TextView)delegateLayout.findViewById(R.id.visit_state_delegate_tv)).setText("");
        ((TextView)delegateLayout.findViewById(R.id.visit_start_time)).setText("");
        ((TextView)delegateLayout.findViewById(R.id.visit_end_time)).setText("");

        name = ""; state = ""; startTime = ""; endTime = "";
//        activity.visitNameTv.setText("");
//        activity.visitStateTv.setText("");
//        activity.visitStartTimeTv.setText("");
//        activity.visitEndTimeTv.setText("");
        stateInt = 0;
        saveStateOnClose();
        filter(((TextView)delegateLayout.findViewById(R.id.visit_name_delegate)).getText().toString(), stateInt,
                ((TextView)delegateLayout.findViewById(R.id.visit_start_time)).getText().toString(),
                ((TextView)delegateLayout.findViewById(R.id.visit_end_time)).getText().toString());
    }

    public void deleteDraft() {
        stateList.remove("草稿");
    }

}
