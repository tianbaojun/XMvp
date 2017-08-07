package com.zjhz.teacher.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.PayOrderResponseBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.ExpandListViewDBAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.listview.PinnedHeaderExpandableListView;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayHistoryActivity extends BaseActivity implements PinnedHeaderExpandableListView.OnHeaderUpdateListener,
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView backImg;
    @BindView(R.id.pay_refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.pinned_list_view)
    PinnedHeaderExpandableListView listView;

    protected ViewDataBinding headViewBinding;
    private ExpandListViewDBAdapter adapter;
    private List<String> groupList = new ArrayList<>();
    private List<List<PayOrderResponseBean>> childList = new ArrayList<>();

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);
        ButterKnife.bind(this);
        titleTv.setText("历史账单");
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ExpandListViewDBAdapter<String, PayOrderResponseBean>(this, groupList, childList,
                R.layout.pay_list_group_item, BR.month, R.layout.pay_history_item, BR.payOrderBean);

        listView.setOnHeaderUpdateListener(this);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        getData();
    }

    private void getData(){
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("pageSize", CommonUrl.PAGE_SIZE+"");
        map.put("status","3");
        NetworkRequest.request(map, CommonUrl.PAY_LIST, "pay_list_all");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter ev) {
        switch (ev.getEventCode()){
            case "pay_list_all":
                List<PayOrderResponseBean> orderBean = GsonUtils.toArray( PayOrderResponseBean.class, (JSONObject) ev.getData());
                if (page == 1) {
                    groupList.clear();
                    childList.clear();
                    refreshLayout.setRefreshing(false);
                }
                String date = null;
                for (int i = 0; i < orderBean.size(); i++) {
                    date = orderBean.get(i).getCreateTime().substring(0, 7);
                    if (date == null)
                        continue;
                    boolean hasItem = false;
                    for (int j = 0; j < groupList.size(); j++) {
                        if (date.equals(groupList.get(j))) {
                            childList.get(j).add(orderBean.get(i));
                            hasItem = true;
                            break;
                        }
                    }
                    if (!hasItem) {
                        groupList.add(date);
                        ArrayList<PayOrderResponseBean> itemList = new ArrayList<>();
                        itemList.add(orderBean.get(i));
                        childList.add(itemList);
                    }
                }
                adapter.notifyDataSetChanged();
                for(int i = 0; i < groupList.size(); i++){
                    listView.expandGroup(i);
                }

                if (groupList.size() > 0) {
                    listView.setOnHeaderUpdateListener(this);
                } else
                    listView.setOnHeaderUpdateListener(null);

                if (orderBean.size() < CommonUrl.PAGE_SIZE) {
                    refreshLayout.setLoading(false);
                } else {
                    refreshLayout.setLoading(true);
                }
                break;
        }
    }

    @Override
    public View getPinnedHeader() {
        headViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pay_list_group_item, refreshLayout, true);
        return headViewBinding.getRoot();
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        if (firstVisibleGroupPos < 0)
            return;
        headViewBinding.setVariable(BR.month, adapter.getGroup(firstVisibleGroupPos));
        listView.expandGroup(firstVisibleGroupPos);
//        if (listView.isGroupExpanded(firstVisibleGroupPos))
//            headViewBinding.setVariable(BR.isExpandGroupOpen, true);
//        else
//            headViewBinding.setVariable(BR.isExpandGroupOpen, false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    public void onLoad() {
        page++;
        getData();
    }
}
