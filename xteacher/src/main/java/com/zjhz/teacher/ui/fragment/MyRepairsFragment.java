package com.zjhz.teacher.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.MyRepairsParams;
import com.zjhz.teacher.NetworkRequests.response.RepairsListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.RepairsProposerStateActivity;
import com.zjhz.teacher.ui.adapter.RepairsProposerAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;

/**
 * 我的报修
 * Created by Administrator on 2016/7/16.
 */
public class MyRepairsFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    RepairsProposerAdapter adapter ;

    private DefaultLoadingLayout loadingLayout;
    private int page = 1, pageSize = 12,total = 0;
    private RepairsBroadCast broadCast;
    private String startTime = "";
    private String endTime = "";
    private int status =0;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_repairs, null);
    }

    @Override
    protected void initViewsAndEvents() {
        loadingLayout = DefaultLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        broadCast = new RepairsBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MyRepairsFragment");
        getActivity().registerReceiver(broadCast,intentFilter);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        initRefreshListener();
        adapter = new RepairsProposerAdapter(getActivity(), 1);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RepairsProposerStateActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("repairId",adapter.getItem(position).getRepairId());
                startActivityForResult(intent, position);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        dialog.setMessage("正在获取报修列表...");
        dialog.show();
        getRepairList();
    }

    //报修人获取报修列表
    private void getRepairList() {
        if(status==0) {
            NetworkRequest.request(new MyRepairsParams(page, pageSize, startTime, endTime), CommonUrl.repairList, Config.MYREPAIRLIST);
        }else{
            String s=Integer.toString(status);

            NetworkRequest.request(new MyRepairsParams(page, pageSize, startTime, endTime,s), CommonUrl.repairList, Config.MYREPAIRLIST);
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()){
            case Config.ERROR:
                stopRefreshListener();
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
            case Config.NOSUCCESS:
                stopRefreshListener();
                dialog.dismiss();
                break;
            case Config.MYREPAIRLIST:
                stopRefreshListener();
                JSONObject jsons = (JSONObject) eventCenter.getData();
                List<RepairsListBean> bean = GsonUtils.toArray(RepairsListBean.class,jsons);
                try {
                    total = jsons.getInt("total");
                    if (bean != null){
                        if (page == 1) {
                            adapter.setBeans(bean);
                        } else {
                            adapter.addBeans(bean);
                        }
                        if(adapter.getCount()>0) {
                            loadingLayout.onDone();
                            adapter.notifyDataSetChanged();
                        }else {
                            loadingLayout.onEmpty();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(broadCast);
    }

    private void stopRefreshListener(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    private void initRefreshListener() {
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onLoad() {
        if (adapter.getBeans() != null && adapter.getBeans().size() < total ){
            page++;
            getRepairList();
        }else {
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getRepairList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;
        boolean isDelete = data.getBooleanExtra("isDelete", false);
        boolean isRevoke = data.getBooleanExtra("revoke", false);
        if(isRevoke) {
            adapter.getBeans().get(requestCode).getOrderFlowState().setNodeName("撤回流程");
            adapter.getBeans().get(requestCode).setStatus(4);
            adapter.notifyDataSetChanged();
        }
        if(isDelete){
            adapter.getBeans().remove(requestCode);//删除请求
            adapter.notifyDataSetChanged();
        }
    }

    class RepairsBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                boolean isupdate = intent.getBooleanExtra("isupdate", false); //更新被退回状态
                if (isupdate) {
                    page = 1;
                    getRepairList();
//                    int index = intent.getIntExtra("update_index", -1);
//                    String describs = intent.getStringExtra("describs");
////                    String bespeakTime = intent.getStringExtra("update_time");
//                    RepairsListBean bean = adapter.getItem(index);
//                    bean.getOrderFlowState().setCurNode(1);
////                    bean.setApplyTime(bespeakTime);
//                    if (!SharePreCache.isEmpty(describs)) {
//                        bean.setSummary(describs);
//
//                    }
//                    bean.setOrginAddress(intent.getStringExtra("OrginAddress"));
//                    bean.setItemName(intent.getStringExtra("ItemName"));
//                    bean.setStatus(intent.getIntExtra("status", -1));
////                    bean.getOrderFlowState().setNodeName("第1级审批");
//                    adapter.notifyDataSetChanged();
                } else {
                    boolean isAdd = intent.getBooleanExtra("isAdd", false);// 表示筛选数据，还是新添加数据
                    if (isAdd) {
                        startTime = "";
                        endTime = "";
                    } else {
                        startTime = intent.getStringExtra("startTime");
                        startTime = startTime + " 00:00:00";
                        endTime = intent.getStringExtra("endTime");
                        endTime = endTime + " 23:59:59";
                        status = intent.getIntExtra("status", 0);
                    }
                    page = 1;
                    getRepairList();
                }
            }
        }
    }
}
