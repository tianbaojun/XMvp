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
import com.zjhz.teacher.bean.UpdateObject;
import com.zjhz.teacher.ui.adapter.NewsAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;import butterknife.BindView;

/**
 * 新闻页
 * Created by xiangxue on 2016/6/8.
 */
public class NewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    DefaultLoadingLayout loadingLayout;
    private NewsAdapter adapter;
    private int pageNum = 1;
    private int pageSize = 12;
    private int total = 0;
    private final static String TAG = NewsActivity.class.getSimpleName();
    private int index;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
//        setSystemBarTintDrawable("#24b7a4");
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        setListener();
        getNewsData();
    }
    private void initView() {
        loadingLayout = DefaultLoadingLayout.createDefaultLayout(this, refreshLayout);
        titleTv.setText("本校新闻");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        adapter = new NewsAdapter(this);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(this);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum = 1;
                getNewsData();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.getCount() < total){
                    pageNum++;
                    getNewsData();
                }else {
                    refreshLayout.setLoading(false);
                }
            }
        }, 2000);
    }

    private void getNewsData() {
        NetworkRequest.request(new NewsAndAnnouncementListParams("OA_CATEGORY_NAME_1",3,pageNum,pageSize), CommonUrl.newsUrl, Config.NEWSLIST);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case Config.ERROR:
                stopRefresh();
                ToastUtils.showShort("请求错误");
                break;
            case  Config.NOSUCCESS:
                stopRefresh();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            case Config.NEWSLIST: // 新闻列表
                stopRefresh();
                JSONObject object = (JSONObject) event.getData();
                total = object.optInt("total");
                try {
                    if (!SharePreCache.isEmpty(object.getString("data"))) {
                        List<NewsBean> newslists = GsonUtils.toArray(NewsBean.class, object);
                        if (newslists != null) {
                            if (pageNum == 1) {
                                adapter.setDatas(newslists);
                            } else {
                                adapter.addDatas(newslists);
                            }
                            if(adapter.getDatas().size()>0) {
                                loadingLayout.onDone();
                                adapter.notifyDataSetChanged();
                            }else{
                                loadingLayout.onEmpty();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.NEWSUPDATE:// 新闻列表点赞回复变化
                UpdateObject be = (UpdateObject) event.getData();
                NewsBean bean =  adapter.getItem(index);
                int PraiseNum = be.PraiseNum;
                int ReplyNum =be.ReplyNum;
                bean.setPraiseNum(PraiseNum);
                bean.setReplyNum(ReplyNum);
                adapter.notifyDataSetChanged();
                break;
        }
    }
    private void stopRefresh(){
        refreshLayout.setRefreshing(false);
        refreshLayout.setLoading(false);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        Intent intent = new Intent(this,NewsDetailActivity.class);
        intent.putExtra("newsId",adapter.getItem(position).getNewsId());
//        intent.putExtra("type", "list");
        startActivity(intent);
    }
}
