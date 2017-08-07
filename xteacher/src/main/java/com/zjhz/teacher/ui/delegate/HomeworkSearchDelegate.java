package com.zjhz.teacher.ui.delegate;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.HomeworkSearchReqBean;
import com.zjhz.teacher.NetworkRequests.response.SubjectBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Dict;
import com.zjhz.teacher.ui.activity.homework.HomeworkManagerNewActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pro.kit.ViewTools;

/**
 * Created by zzd on 2017/7/15.
 */

public class HomeworkSearchDelegate extends XDelegate<HomeworkManagerNewActivity, HomeworkSearchReqBean> implements View.OnClickListener{

    private static String[] param = {"",""};

    private TextView statusTv, subjectTv, startTv, endTv;

    private static String status, subject, startTime, endTime;

    private ArrayList<Dict> statusList = new ArrayList<>();
    private ArrayList<Dict> subjectList = new ArrayList<>();

    public HomeworkSearchDelegate(Context context,IDrawerLayout drawerLayout) {
        super(context, drawerLayout);
    }

    @Override
    public void initialize() {
        statusTv = (TextView) delegateLayout.findViewById(R.id.homework_state_tv);
        subjectTv = (TextView) delegateLayout.findViewById(R.id.homework_subject_tv);
        startTv = (TextView) delegateLayout.findViewById(R.id.homework_start_time);
        endTv = (TextView) delegateLayout.findViewById(R.id.homework_end_time);
        statusTv.setOnClickListener(this);
        subjectTv.setOnClickListener(this);
        startTv.setOnClickListener(this);
        endTv.setOnClickListener(this);

        //1 待发布 2 已发布 3 已完结  教师端
        statusList.add(new Dict("1", "待发布"));
        statusList.add(new Dict("2", "已发布"));
        statusList.add(new Dict("3", "已完结"));

        EventBus.getDefault().register(this);
        NetworkRequest.request(null, CommonUrl.homeworkSubjectList, "subject_list");
    }

    @Override
    public void readStateOnOpen() {
        startTv.setText(status);
        subjectTv.setText(subject);
        startTv.setText(startTime);
        endTv.setText(endTime);
    }

    @Override
    public void saveStateOnClose() {
        status = statusTv.getText().toString();
        subject = subjectTv.getText().toString();
        startTime = startTv.getText().toString();
        endTime = endTv.getText().toString();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.navigation_header_right:
                ViewTools.hideSoftInput((Activity) context);
                //TODO 通过接口刷选
                filter();
                break;
            case R.id.navigation_header_left:
                ViewTools.hideSoftInput((Activity) context);
                break;
            case R.id.drawer_layout_clear:
                ViewTools.hideSoftInput((Activity) context);
                break;
            case R.id.homework_state_tv:
                chooseStatus(statusTv, statusList, 0);
                break;
            case R.id.homework_subject_tv:
                chooseStatus(subjectTv,subjectList, 1);
                break;
            case R.id.homework_start_time:
                TimeUtil.selectDate(context, startTv);
                break;
            case R.id.homework_end_time:
                TimeUtil.selectDate(context, endTv);
                break;
        }
    }

    @Override
    public void resetData() {
        statusTv.setText("");
        subjectTv.setText("");
        startTv.setText("");
        endTv.setText("");

        status = ""; subject = ""; startTime = ""; endTime = "";
        param[0] = "";
        param[1] = "";
        filter();
        closeDrawer();
    }

    private void chooseStatus(final TextView tv, final ArrayList<Dict> dicts, final int i){
        OptionsPickerView<Dict> optionsPickerView = new OptionsPickerView<Dict>(context);
        int statusInt = 0;
        if(!TextUtils.isEmpty(param[i])){
            for (Dict dict : dicts) {
                if (param[i].equals(dict.id)) {
                    break;
                }
                statusInt++;
            }
        }
        optionsPickerView.setPicker(dicts, statusInt, 0, 0);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                tv.setText(dicts.get(options1).value);
                param[i] = dicts.get(options1).id;
            }
        });
        optionsPickerView.show();
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case "subject_list":
                List<SubjectBeans> list = GsonUtils.toArray(SubjectBeans.class, (JSONObject)event.getData());
                if(list != null){
                    for(SubjectBeans beans: list){
                        subjectList.add(new Dict(beans.getSubjectId(), beans.getName()));
                    }
                }
                break;
        }
    }

    private void filter(){
        dataBean.page = "1";
        dataBean.pageSize = "10";
        dataBean.status = param[0];
        dataBean.subjectId = param[1];
        dataBean.startTime = startTime;
        dataBean.endTime = endTime;
        NetworkRequest.request(dataBean, CommonUrl.HOMEWORK_LIST, "homework_list");
    }
}
