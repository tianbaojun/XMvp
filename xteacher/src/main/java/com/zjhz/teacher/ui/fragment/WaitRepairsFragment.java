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
import com.zjhz.teacher.NetworkRequests.request.WaitRepairsParams;
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
 * 报修待审批
 * Created by Administrator on 2016/7/16.
 */
public class WaitRepairsFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    RepairsProposerAdapter adapters ;

    private DefaultLoadingLayout loadingLayout;
    private int page = 1, pageSize = 12,total = 0;
    private String startTime = "";
    private String endTime = "";
    private int status =0;
    private WaitRepairsBroadCast broadCast;
    private int selectIndex = -1;
//    private  boolean EntryFlag;
    class WaitRepairsBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 1;
            if (intent != null){
                startTime = intent.getStringExtra("startTime");
                startTime = startTime +" 00:00:00";
                endTime = intent.getStringExtra("endTime");
                endTime = endTime +" 23:59:59";
                status = intent.getIntExtra("status",0);
            }
            getapproveListList();
        }
    }
    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_repairs, null);
    }
    @Override
    protected void initViewsAndEvents() {
        loadingLayout = DefaultLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        broadCast = new WaitRepairsBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WaitRepairsFragment");
        getActivity().registerReceiver(broadCast,intentFilter);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        initRefreshListener();
        adapters = new RepairsProposerAdapter(getActivity(), 2);
        refreshListview.setAdapter(adapters);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                Intent intent = new Intent(getActivity(),RepairsProposerStateActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("approveFlag",adapters.getItem(position).getOrderStateMsg().getApproveFlag());
                intent.putExtra("repairId",adapters.getItem(position).getRepairId());
                startActivity(intent);
            }
        });
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        dialog.setMessage("正在获取审批列表...");
        dialog.show();
        getapproveListList();
//        getapproveEntry();
    }

//    private void getapproveEntry() {
//        NetworkRequest.request(null, CommonUrl.approve,Config.approveEntry);
//    }

    //审批人获取报修列表
    private void getapproveListList() {
        if(status==0) {
             NetworkRequest.request(new WaitRepairsParams(startTime,endTime), CommonUrl.approveList,Config.WAITREPAIRLIST);
        }else{
            String s=Integer.toString(status);
            NetworkRequest.request(new WaitRepairsParams(startTime,endTime,s), CommonUrl.approveList,Config.WAITREPAIRLIST);
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
                break;
            case Config.NOSUCCESS:
                stopRefreshListener();
                dialog.dismiss();
                break;
            case Config.WAITREPAIRLIST:
                stopRefreshListener();
                dialog.dismiss();
                JSONObject jsons = (JSONObject) eventCenter.getData();
                List<RepairsListBean> bean = GsonUtils.toArray(RepairsListBean.class,jsons);
                try {
                    total = jsons.getInt("total");
                    if (bean != null){
                        if (page == 1) {
                            adapters.setBeans(bean);
                        } else {
                            adapters.addBeans(bean);
                        }
                        if(adapters.getCount()>0) {
                            loadingLayout.onDone();
                            adapters.notifyDataSetChanged();
                        }else{
                            loadingLayout.onEmpty();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "waitupdate":
                page = 1;
                getapproveListList();
//                adapters.getItem(selectIndex).getOrderStateMsg().setApproveFlag(2);
//                adapters.getItem(selectIndex).getOrderStateMsg().setStatus(2);
//                adapters.notifyDataSetChanged();
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
//        if (EntryFlag){
            if (adapters.getBeans() != null && adapters.getBeans().size() < total ){
                page++;
                getapproveListList();
            }else {
                refreshLayout.setLoading(false);
            }
//        }else {
//            stopRefreshListener();
//        }
    }
    @Override
    public void onRefresh() {
//        if (EntryFlag){
            page = 1;
            getapproveListList();
//        }else {
//            stopRefreshListener();
//        }
    }
}
