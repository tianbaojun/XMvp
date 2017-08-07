package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CommentParams;
import com.zjhz.teacher.NetworkRequests.request.DeleteFoodCommentParams;
import com.zjhz.teacher.NetworkRequests.request.DeleteNewsCommentParams;
import com.zjhz.teacher.NetworkRequests.request.FoodCommentParams;
import com.zjhz.teacher.NetworkRequests.response.NewsReplayBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateObject;
import com.zjhz.teacher.ui.adapter.CommentAdapter;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 评论
 * Created by xiangxue on 2016/6/17.
 */
public class CommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.comment_count)
    TextView commentCount;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.ll)
    LinearLayout ll;
    private CommentAdapter adapter;
    private String newsId;
    private int total = 0 ,pageNum = 1, pageSize = 12,praisedNum = 0;
    private int type;
    private boolean isupdateReplayNum = false;
    private final static String TAG = CommentActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null){
            newsId = intent.getStringExtra("newsId");
            praisedNum =intent.getIntExtra("praisedNum",0);
            type = intent.getIntExtra("type",0);
        }
        if (SharePreCache.isEmpty(newsId)) {
            ToastUtils.showShort("新闻ID丢失");
            return;
        }
        initTile();
        initView();
        getData();
    }
    private void initTile() {
        titleTv.setText("评论页面");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rightText.setText("全部评论");
        rightText.setVisibility(View.VISIBLE);
        rightText.setPadding(5, 5, 5, 5);
        rightText.setTextSize(10);
        rightText.setBackgroundResource(R.mipmap.pinglunkuang);
    }
    private void initView() {
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new CommentAdapter(this);
        adapter.setItemCallBack(new CommentAdapter.ItemCallBack() {
            @Override
            public void delete(String id) {
                if(type == 1)
                    deleteComment(id, CommonUrl.removeNewReply);
                else if(type == 2)
                    deleteComment(id, CommonUrl.removeCookBookReply);
            }
        });
        refreshListview.setAdapter(adapter);
    }
    private void getData() {
        if (type == 1){
            NetworkRequest.request(new CommentParams(newsId,pageNum,pageSize), CommonUrl.newsReplyList,Config.NEWSREPLAYLISTs);
        }else if (type == 2){
            NetworkRequest.request(new FoodCommentParams(newsId, pageNum, pageSize), CommonUrl.foodReplyList,Config.NEWSREPLAYLISTs);
        }
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                stopRefresh();
                ToastUtils.showShort("请求错误");
                break;
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求错误");
                stopRefresh();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            //获取新闻回复列表
            case Config.NEWSREPLAYLISTs:
                stopRefresh();
                JSONObject obs = (JSONObject) event.getData();
                try {
                    total = obs.getInt("total");
                    commentCount.setText("最新评论");
                    if (total == 0) {
                        rightText.setText("全部评论");
//                        commentCount.setText("全部评论");
                    }else{
                        rightText.setText(total + "条评论");
//                        commentCount.setText(total + "条评论");
                    }

                    List<NewsReplayBean> replayList = GsonUtils.toArray(NewsReplayBean.class, obs);
                    if (replayList != null) {
                        if (pageNum == 1) {
                            adapter.setDatas(replayList);
                        } else {
                            if (replayList.size() > 0 ){
                                adapter.addDatas(replayList);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if (isupdateReplayNum){
                        isupdateReplayNum = false;
                        ToastUtils.showShort("删除评论成功");
                        if (type == 1){
                            EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE,new UpdateObject(praisedNum,total)));
                        }else if (type == 2){
                            EventBus.getDefault().post(new EventCenter<>(Config.FOODUPDATE,new UpdateObject(praisedNum,total)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.NEWSDELETECOMMENT:
                pageNum = 1;
                isupdateReplayNum = true;
                getData();
                break;
            case Config.FOODDELETECOMMENT:
                pageNum = 1;
                isupdateReplayNum = true;
                getData();
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
        getData();
    }

    @Override
    public void onLoad() {
        if (adapter.getCount() < total){
            pageNum++;
            getData();
        }else {
            refreshLayout.setLoading(false);
        }
    }

    private HintPopwindow hintPopwindow;
    public void deleteComment(final String id, final String url){
        if (hintPopwindow == null){
            hintPopwindow = new HintPopwindow(this);
            hintPopwindow.setTitleMessage("确认删除此评论吗?");
        }
        hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
            @Override
            public void sureClick() {
                hintPopwindow.dismiss();
                if (type == 1){
                    NetworkRequest.request(new DeleteNewsCommentParams(id,newsId),url, Config.NEWSDELETECOMMENT);
                }else if (type == 2){
                    NetworkRequest.request(new DeleteFoodCommentParams(id,newsId),url, Config.FOODDELETECOMMENT);
                }
            }
            @Override
            public void cancelClick() {
                hintPopwindow.dismiss();
            }
        });
        if (!hintPopwindow.isShowing()){
            hintPopwindow.showAtLocation(ll, Gravity.CENTER,0,0);
        }
    }
}
