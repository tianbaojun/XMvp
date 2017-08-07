package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.BaseUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

public abstract class ListActivity<E> extends BaseActivity implements
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    protected List<E> dataList = new ArrayList<>();

    protected ListViewDBAdapter<E> adapter;
    protected int listItemLayoutId;
    protected int BRId;


    protected LinearLayout top, bottom;
    protected RefreshLayout refreshLayout;
    protected ListView listView;

    protected int page = 1;

    protected SmartLoadingLayout loadingLayout;

    @BindView(R.id.title_back_img)
    public TextView backTv;
    @BindView(R.id.right_icon0)
    public ImageView addTv;
    @BindView(R.id.right_icon1)
    public ImageView filterImg;
    @BindView(R.id.list_manager_drawer)
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_layout);
        ButterKnife.bind(this);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listItemLayoutId = getListItemLayoutId();
        BRId = getBRId();
        adapter = new ListViewDBAdapter<E>(this, dataList, listItemLayoutId, BRId);

        top = (LinearLayout)findViewById(R.id.list_layout_top);
        bottom = (LinearLayout)findViewById(R.id.list_layout_bottom);
        refreshLayout = (RefreshLayout)findViewById(R.id.refresh_layout);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setRefreshing(true);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);

        initView();
        getSearcherList();
    }
    protected abstract int getListItemLayoutId();
    protected abstract int getBRId();
    protected abstract void initView();
    protected abstract void getSearcherList();

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        refreshLayout.setRefreshing(false);
        refreshLayout.setLoading(false);
        int total = 0;
        if(event.getData() instanceof JSONObject) {
            total = ((JSONObject) event.getData()).optInt("total");
        }
        if(total == dataList.size()){
            refreshLayout.setCanLoad(false);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(dataList!=null&&dataList.size()>0){
            loadingLayout.onDone();
        }else{
            loadingLayout.onEmpty();
        }
    }

    protected void notifyDataSetChanged(){
        if(BaseUtil.isEmpty(dataList)){
            loadingLayout.onEmpty();
        }else {
            loadingLayout.onDone();
            adapter.notifyDataSetChanged();
        }
    }
}
