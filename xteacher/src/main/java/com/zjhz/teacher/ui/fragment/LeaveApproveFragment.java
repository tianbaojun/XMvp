package com.zjhz.teacher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.LeaveApplyCheckResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.LeaveApplyForListAdapter;
import com.zjhz.teacher.base.Common;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.LeaveApplyForList;
import com.zjhz.teacher.ui.activity.LeaveApplyForListActivity;
import com.zjhz.teacher.ui.activity.LeaveApplyForStateActivity;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-15
 * Time: 15:57
 * Description: 请假审批列表
 */
public class LeaveApproveFragment extends Common implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    public ListView listview;
    public RefreshLayout swipeLayout;
    public List<LeaveApplyForList> lists = new ArrayList<>();
    public LeaveApplyForListAdapter adapter;
//    public WaitDialog dia;
    boolean checkFlag;
    boolean isCheck = false;
    private DefaultLoadingLayout loadingLayout;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_leave, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), swipeLayout);
        listview = (ListView) view.findViewById(R.id.leave_fragment_refresh_listview);
        swipeLayout = (RefreshLayout) view.findViewById(R.id.leave_fragment_refresh_layout);
        EventBus.getDefault().register(this);
        initRefreshLayout();
//        dia = new WaitDialog(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (lists.size() > 0)
            return;
        if (isVisibleToUser) {
            lists.clear();
            NetworkRequest.request(null, CommonUrl.LEAVECHECK,Config.LEAVECHECK);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (lists.size() > 0)
//            return;
//        LogUtil.e("请假审批列表  onResume  True");
////        dia.show();
//        lists.clear();
//        listview.requestLayout();
//        adapter.notifyDataSetChanged();
//        NetworkRequest.request(null, CommonUrl.LEAVECHECK, Config.LEAVECHECK);
//    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        adapter = new LeaveApplyForListAdapter(context, lists,-1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, LeaveApplyForStateActivity.class);
                intent.putExtra("leave_list_oid", lists.get(position).oid);
                intent.putExtra("leave_list_isCheck", isCheck);
                intent.putExtra("leave_list_approveFlag", lists.get(position).approveFlag);
                LogUtil.e("传递到审批详情的position = ",position + "   传递到审批详情的approveFlag = " + lists.get(position).approveFlag);
                LogUtil.e("LeaveFragment",isCheck + "");
                startActivityForResult(intent, position);
//                startActivityForResult(intent, LeaveApplyForListActivity.REFRESH_TWO);
            }
        });
    }

    @Override @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
//                dia.dismiss();
                ToastUtils.toast("请求错误");
                break;
            case Config.NOSUCCESS:
//                dia.dismiss();
                break;
            case Config.LEAVECHECK:
                JSONObject check = (JSONObject) event.getData();
                LeaveApplyCheckResponse leaveApplyCheckResponse = GsonUtils.toObject(check.toString(), LeaveApplyCheckResponse.class);
                checkFlag = leaveApplyCheckResponse.data.EntryFlag;
                if (checkFlag) {
                    lists.clear();
                    NetworkRequest.request(null, CommonUrl.LEAVECHECKLIST, Config.LEAVECHECKLIST);   // 得到待审批请假列表
                    swipeLayout.setRefreshing(true);
                } else {
//                    dia.dismiss();
//                    ToastUtils.toast("没有审批权限");
                }
                break;
            case Config.LEAVECHECKLIST:
                LogUtil.e("请假审批列表数据---------------------------");
//                dia.dismiss();
                JSONObject checkData = (JSONObject) event.getData();
                int total = checkData.optInt("total");
                if (checkData != null) {
                    JSONArray data1 = checkData.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                LeaveApplyForList list = new LeaveApplyForList();
                                list.content = o.optString("summary");
                                list.name = o.optString("nickName");    // 名称
                                list.startTime = o.optString("startTime");
                                list.endTime = o.optString("endTime");
                                list.reason = o.optString("summary");
                                list.type = o.optString("typeVal");
                                list.appType = o.optString("typeVal");
                                list.photoUrl = o.optString("photoUrl");

                                list.time = o.optString("applyerTime").substring(0,16);
                                JSONObject orderFlowState = o.optJSONObject("orderStateMsg");
                                if (orderFlowState != null) {
                                    list.state = orderFlowState.optString("nodeName");
                                    list.curNode = orderFlowState.optInt("curNode");
                                    list.approveFlag = orderFlowState.optString("approveFlag");
                                    list.flowStatus = orderFlowState.optString("flowStatus");
                                }
                                list.oid = o.optString("oid");
                                lists.add(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);
                if(adapter.getCount() >= total)
                    swipeLayout.setLoading(false);

                adapter.notifyDataSetChanged();
                if(adapter.getCount()>0) {
                    loadingLayout.onDone();
                }else{
                    loadingLayout.onEmpty();
                }
                break;
        }
    }

    @Override
    public void onLoad() {
        //不分页
        swipeLayout.setLoading(false);
    }

    @Override
    public void onRefresh() {
        if (checkFlag){
//            dia.show();
            lists.clear();
//        adapter.notifyDataSetChanged();
            NetworkRequest.request(null, CommonUrl.LEAVECHECKLIST, Config.LEAVECHECKLIST);   // 审批列表
//            swipeLayout.setRefreshing(false);
        }
    }

    private void initRefreshLayout() {
//        swipeLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(true);
//            }
//        }));
        swipeLayout.setColorSchemeResources(Constance.colors);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            lists.clear();
            NetworkRequest.request(null, CommonUrl.LEAVECHECKLIST, Config.LEAVECHECKLIST);   // 审批列表
        }

//        if(data == null)
//            return;
//        boolean isDelete = data.getBooleanExtra("isDelete", false);
//        boolean isRevoke = data.getBooleanExtra("revoke", false);
//        if(isRevoke) {
//            lists.get(requestCode).flowStatus = "3";
//            adapter.notifyDataSetChanged();
//        }
//        if(isDelete){
//            lists.remove(requestCode);//删除请求
//            adapter.notifyDataSetChanged();
//        }
//        switch (requestCode){
//            case LeaveApplyForListActivity.REFRESH_TWO:
//                if(resultCode == RESULT_OK){
//                    onRefresh();
//                }
//        }
    }
}