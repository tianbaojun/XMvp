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
import com.zjhz.teacher.NetworkRequests.request.NewsAndAnnouncementListParams;
import com.zjhz.teacher.NetworkRequests.response.NewsBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.AnnouncementAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * 通知公告
 * Created by xiangxue on 2016/6/16.
 */
public class AnnouncementActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    private AnnouncementAdapter adapter;
    private DefaultLoadingLayout loadingLayout;
    private final static String TAG = AnnouncementActivity.class.getSimpleName();
    private int pageNum = 1;
    private int pageSize = 12;
    private int total;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        initView();
        initListener();
        getAnnounceData();
    }

    private void initView() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        Intent mIntent = getIntent();
        String title = mIntent.getStringExtra("title");
        titleTv.setText(title);
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        adapter = new AnnouncementAdapter(this);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AnnouncementActivity.this,AnnounceDetailActivity.class);
                intent.putExtra("announceId",adapter.getItem(position).getNewsId());
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        switch (event.getEventCode()){
            case  Config.ERROR:
                stopRefresh();
                break;
            case  Config.NOSUCCESS:
                stopRefresh();
                break;
            case  Config.announcementlist:
                stopRefresh();
                JSONObject object = (JSONObject) event.getData();
                try {
                    total = object.getInt("total");
                    List<NewsBean> list =  GsonUtils.toArray(NewsBean.class,object);
                    if (list != null){
                        if (pageNum == 1){
                            adapter.setDatas(list);
                        }else {
                            adapter.addDatas(list);
                        }
                        if(adapter.getCount()>0) {
                            loadingLayout.onDone();
                            adapter.notifyDataSetChanged();
                        }else{
                            loadingLayout.onEmpty();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void stopRefresh(){
        refreshLayout.setRefreshing(false);
        refreshLayout.setLoading(false);
    }
    @Override
    public void onRefresh() {
        pageNum = 1;
        getAnnounceData();
    }
    @Override
    public void onLoad() {
        if (adapter.getDatas().size() < total){
            pageNum++;
            getAnnounceData();
        }else {
            refreshLayout.setLoading(false);
        }
    }
    private void getAnnounceData() {
        NetworkRequest.request(new NewsAndAnnouncementListParams("OA_CATEGORY_NAME_2",3,pageNum,pageSize), CommonUrl.announcementList, Config.announcementlist);
    }
}
