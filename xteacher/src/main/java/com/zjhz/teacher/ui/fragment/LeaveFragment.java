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
import com.zjhz.teacher.NetworkRequests.request.MyRepairsParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.LeaveApplyForListAdapter;
import com.zjhz.teacher.base.Common;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.LeaveApplyForList;
import com.zjhz.teacher.ui.activity.LeaveApplyForStateActivity;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
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
 * Description: 请假申请列表
 */
public class LeaveFragment extends Common implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    public ListView listview;
    public RefreshLayout swipeLayout;
    public List<LeaveApplyForList> lists = new ArrayList<>();
    public LeaveApplyForListAdapter adapter;
//    public WaitDialog dia;
    boolean isCheck = true;
    boolean fly_fly;
    private DefaultLoadingLayout loadingLayout;
    private int pageSize = 50,pageNum = 1,total = 0;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_leave, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        swipeLayout = (RefreshLayout) view.findViewById(R.id.leave_fragment_refresh_layout);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(),swipeLayout);
        listview = (ListView) view.findViewById(R.id.leave_fragment_refresh_listview);
        EventBus.getDefault().register(this);
        initRefreshLayout();
        swipeLayout.setRefreshing(true);
//        dia = new WaitDialog(context);
//        dia.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (lists.size() > 0)
            return;
        if (isVisibleToUser) {
            lists.clear();
//            if (dia != null) {
////                dia.show();
//            }
            pageNum = 1;
            NetworkRequest.request(new MyRepairsParams(pageNum,pageSize,"",""), CommonUrl.LEAVELIST,Config.LEAVELIST); // 申请列表
            LogUtil.e("请假列表  setUserVisibleHint");
            fly_fly = false;
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (lists.size() > 0)
//            return;
//        LogUtil.e("请假列表  onResume");
//        if (fly_fly) {
//            dia.show();
//            lists.clear();
//            listview.requestLayout();
//            adapter.notifyDataSetChanged();
//            NetworkRequest.request(new MyRepairsParams(1,pageSize,"",""), CommonUrl.LEAVELIST,Config.LEAVELIST); // 申请列表
//        }else{
//            fly_fly = true;
//        }
//    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        adapter = new LeaveApplyForListAdapter(context, lists,1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // isChecked = true 进入申请详情
                Intent intent = new Intent(context, LeaveApplyForStateActivity.class);
                intent.putExtra("leave_list_oid", lists.get(position).oid);
                intent.putExtra("leave_list_isCheck", isCheck);
                LogUtil.e("LeaveFragment",isCheck + "");
//                startActivity(intent);
                startActivityForResult(intent, position);
            }
        });
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.toast("请求错误");
//                dia.dismiss();
                break;
            case Config.NOSUCCESS:
//                dia.dismiss();
                break;
            case Config.LEAVELIST:   // 申请列表
//                dia.dismiss();
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("请假列表",data.toString());
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    total = data.optInt("total");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                LeaveApplyForList list = new LeaveApplyForList();
                                list.content = o.optString("summary");
                                list.name = o.optString("createUserVal");    // 名称
                                list.startTime = o.optString("startTime");
                                list.endTime = o.optString("endTime");
                                list.reason = o.optString("summary");
                                list.sma = o.optInt("sma");
                                list.ema = o.optInt("ema");
                                list.type = o.optString("typeVal");
                                list.appType = o.optString("typeVal");
                                list.flowStatus = o.optString("flowStatus");

                                list.time = o.optString("applyerTime").substring(0,16);
                                JSONObject orderFlowState = o.optJSONObject("orderFlowState");
                                if (orderFlowState != null) {
                                    list.state = orderFlowState.optString("nodeName");
                                    list.curNode = orderFlowState.optInt("curNode");
//                                    list.flowStatus = orderFlowState.optString("flowStatus");
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

                if(adapter.getCount()>0) {
                    adapter.notifyDataSetChanged();
                    loadingLayout.onDone();
                }else{
                    loadingLayout.onEmpty();
                }
                break;
        }
    }

    @Override
    public void onLoad() {
//        if (lists.size() < total){
            pageNum++;
            NetworkRequest.request(new MyRepairsParams(pageNum,pageSize,"",""), CommonUrl.LEAVELIST,Config.LEAVELIST); // 申请列表
//            swipeLayout.setLoading(false);
//        }else {
//            swipeLayout.setLoading(false);
//        }
    }

    @Override
    public void onRefresh() {
        lists.clear();
        adapter.notifyDataSetChanged();
        pageNum = 1;
        NetworkRequest.request(new MyRepairsParams(pageNum,pageSize,"",""), CommonUrl.LEAVELIST,Config.LEAVELIST); // 申请列表
//        swipeLayout.setRefreshing(false);
//        dia.show();
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
        if(resultCode == RESULT_OK){
            onRefresh();
        }
//        if(data == null)
//            return;
//        boolean isDelete = data.getBooleanExtra("isDelete", false);
//        boolean isRevoke = data.getBooleanExtra("revoke", false);
//        if(isRevoke) {
//            lists.get(requestCode).flowStatus = "3";
//            lists.get(requestCode).state = "撤回流程";
//            adapter.notifyDataSetChanged();
//        }
//        if(isDelete){
//            lists.remove(requestCode);//删除请求
//            adapter.notifyDataSetChanged();
//        }
    }
}