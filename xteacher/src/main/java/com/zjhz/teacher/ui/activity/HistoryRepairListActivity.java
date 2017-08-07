package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PageNumSize;
import com.zjhz.teacher.NetworkRequests.response.RepairsListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
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

/**
 * 历史修复
 * Created by Administrator on 2016/7/15.
 */
public class HistoryRepairListActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    private RepairsProposerAdapter adapter;
    private int page = 1,pageSize = 12;
    private int total;
    boolean isEntryFlag;
    private final static String TAG = HistoryRepairListActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        initRefreshListener();
        dialog.setMessage("正在获取列表...");
        dialog.show();
        //获历史审批
        getHistoryListList();
    }
    private void initView() {
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTv.setText("审批完结");
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        adapter = new RepairsProposerAdapter(this, 3);
        refreshListview.setAdapter(adapter);
        isEntryFlag = getIntent().getBooleanExtra("isEntryFlag",false);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryRepairListActivity.this,RepairsProposerStateActivity.class);
                //历史数据本能审批
                intent.putExtra("type",3);
                intent.putExtra("repairId",adapter.getItem(position).getRepairId());
                startActivity(intent);
            }
        });
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                stopRefreshListener();
                ToastUtils.showShort("请求错误");
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefreshListener();
                break;
            case "hisrepairList":
                dialog.dismiss();
                stopRefreshListener();
                JSONObject jsons = (JSONObject) event.getData();
                List<RepairsListBean> bean = GsonUtils.toArray(RepairsListBean.class,jsons);
                try {
                    total = jsons.getInt("total");
                    if (bean != null){
                        if (page == 1) {
                            adapter.setBeans(bean);
                        } else {
                            adapter.addBeans(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        //获历史审批
        getHistoryListList();
    }
    @Override
    public void onLoad() {
        if (adapter.getBeans() != null && adapter.getBeans().size() < total ){
            page++;
            //获历史审批
            getHistoryListList();
        }else {
            refreshLayout.setLoading(false);
        }
    }
    //审批人获取历史报修列表
    private void getHistoryListList() {
        NetworkRequest.request(new PageNumSize(page,pageSize), CommonUrl.HistoryList,"hisrepairList");
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
}
