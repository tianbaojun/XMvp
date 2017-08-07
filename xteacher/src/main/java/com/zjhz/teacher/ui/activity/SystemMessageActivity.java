package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.SystemMessageAndAttenceAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**系统消息
 * Created by Administrator on 2016/7/12.
 */
public class SystemMessageActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    SystemMessageAndAttenceAdapter adapter;
    private final static String TAG = SystemMessageActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        initRefreshListener();
        getData();
    }
    private void getData() {

    }
    private void initRefreshListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }
    private void initView() {
        titleTv.setText("系统消息");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        refreshLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(false);
//            }
//        }));
//        refreshLayout.setColorSchemeResources(Constance.colors);
        adapter = new SystemMessageAndAttenceAdapter(this,null);
        refreshListview.setAdapter(adapter);
    }
    @Subscribe
    public void onEventMainThread(EventCenter event){

    }
    @Override
    public void onLoad() {
        refreshLayout.setLoading(false);
    }
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }
}
