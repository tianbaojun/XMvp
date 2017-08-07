package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CommentParams;
import com.zjhz.teacher.NetworkRequests.request.DeleteNewsCommentParams;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamFalseNews;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamTrueNews;
import com.zjhz.teacher.NetworkRequests.request.ReplayNewsBean;
import com.zjhz.teacher.NetworkRequests.response.NewsReplayBean;
import com.zjhz.teacher.NetworkRequests.response.PraiseIdBean;
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
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 评论
 * Created by xiangxue on 2016/6/17.
 */
public class NewsCommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
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
    FrameLayout ll;
    
    @BindView(R.id.et)
    TextView et;
    @BindView(R.id.hint_rl)
    LinearLayout hintRl;
    @BindView(R.id.dianzan_iv)
    ImageView dianzanIv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.edit_rl)
    RelativeLayout editRl;
    @BindView(R.id.dian_zhan_qp)
    ImageView bubbleImage;
    @BindView(R.id.dianzan_pinglun_count)
    TextView pinglunCountTv;
    
    private CommentAdapter adapter;
    private String newsId, praisedId;
    private int total = 0 ,pageNum = 1, pageSize = 12,praisedNum = 0, replayNum;
    private boolean isupdateReplayNum = false, isAdd = false;
    private String replyMsg;
    private boolean isPraised; //初始是否点赞

    private final static String TAG = CommentActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null){
            newsId = intent.getStringExtra("newsId");
            praisedNum =intent.getIntExtra("praisedNum",0);
            isPraised = intent.getBooleanExtra("isPraised", false);
            if (isPraised){
                dianzanIv.setImageResource(R.mipmap.dianzan2_pre_1);
            }else {
                dianzanIv.setImageResource(R.mipmap.dianzan2_nor_1);
            }
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
                deleteComment(id, CommonUrl.removeNewReply);
            }
        });
        refreshListview.setAdapter(adapter);
    }
    private void getData() {
        NetworkRequest.request(new CommentParams(newsId,pageNum,pageSize), CommonUrl.newsReplyList,Config.NEWSREPLAYLISTs);
    }

    @OnClick({R.id.et,R.id.cancel_tv, R.id.blank, R.id.dianzan_iv,R.id.sure_tv})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
//            case R.id.right_text:
//                if(replayNum!=0) {
//                    Intent intent = new Intent(NewsCommentActivity.this,NewsCommentActivity.class);
//                    intent.putExtra("type",1);
//                    intent.putExtra("newsId",newsId);
//                    intent.putExtra("praisedNum",praisedNum);
//                    startActivity(intent);
//                }else{
//                    ToastUtils.showShort("目前没有评论");
//                }
//                break;
            //点击评论
            case R.id.et:
                hintRl.setVisibility(View.GONE);
                dianzanIv.setVisibility(View.GONE);
                editRl.setVisibility(View.VISIBLE);
                et1.requestFocus();
                showInput();
                break;
            //点击取消
            case R.id.blank:
                hintEditRl();
                break;
            case R.id.cancel_tv:
                hintEditRl();
                break;
            //点击点赞
            case R.id.dianzan_iv:
                if (!isPraised){
                    NetworkRequest.request(new PraiseParamTrueNews(true,newsId), CommonUrl.newsPraise, Config.NEWSPRAISE);
                }else {
                    NetworkRequest.request(new PraiseParamFalseNews(false,newsId,praisedId), CommonUrl.newsPraise, Config.NEWSPRAISE);
                }
                break;
            //点击确定
            case R.id.sure_tv:
                replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();

                    NetworkRequest.request(new ReplayNewsBean(newsId, replyMsg), CommonUrl.newsReply, Config.NEWSREPLAY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
        }
    }

    private void showInput() {
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(et1, 0);
    }

    public void hintEditRl() {
        hintRl.setVisibility(View.VISIBLE);
        dianzanIv.setVisibility(View.VISIBLE);
        editRl.setVisibility(View.GONE);
//        SharePreCache.hintInput(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editRl.getWindowToken(), 0);
        et1.setText("");
        et.setText("");
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        if(dialog.isShowing())
            dialog.dismiss();
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
                    } else {
                        rightText.setText(total + "条评论");
//                        commentCount.setText(total + "条评论");
                    }
                    pinglunCountTv.setText(String.valueOf(total));
                    List<NewsReplayBean> replayList = GsonUtils.toArray(NewsReplayBean.class, obs);
                    if (replayList != null) {
                        if (pageNum == 1) {
                            adapter.setDatas(replayList);
                        } else {
                            if (replayList.size() > 0) {
                                adapter.addDatas(replayList);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if (isupdateReplayNum) {
                        isupdateReplayNum = false;
                        ToastUtils.showShort("删除评论成功");
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE, new UpdateObject(praisedNum, total)));
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
            case Config.NEWSPRAISE:
                dialog.dismiss();
                JSONObject pri = (JSONObject) event.getData();
                try {
                    if (isPraised) { // 如果为true则表示取消点赞成功，如果为false则表示点赞成功
                        if (praisedNum > 0) {
                            praisedNum = praisedNum - 1;
                        }
                        isPraised = false;
                        dianzanIv.setImageResource(R.mipmap.dianzan2_nor_1);
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE, new UpdateObject(praisedNum, replayNum)));
                    } else {
                        praisedNum = praisedNum + 1;
                        isPraised = true;
                        PraiseIdBean b = GsonUtils.toObject(pri.getJSONObject("data").toString(), PraiseIdBean.class);
                        praisedId = b.getPraiseId();
                        bubbleImage.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(this, R.anim.dian_zhan_qp);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                bubbleImage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        bubbleImage.setAnimation(animation);
                        dianzanIv.setImageResource(R.mipmap.dianzan2_pre_1);
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE, new UpdateObject(praisedNum, replayNum)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //获取添加文字回复
            case Config.NEWSREPLAY:
                isupdateReplayNum = true;
                isAdd = true;
                NewsReplayBean replayBean = new NewsReplayBean();
                replayBean.setMsgContect(replyMsg);
                replayBean.setReplyTime(TimeUtil.getNowYMDHMS());
                replayBean.setReplyUser(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserNameKey));
                adapter.addData(0,replayBean);
                int num = Integer.parseInt(pinglunCountTv.getText().toString());
                pinglunCountTv.setText(String.valueOf(num+1));
                adapter.notifyDataSetChanged();
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
                    NetworkRequest.request(new DeleteNewsCommentParams(id,newsId),url, Config.NEWSDELETECOMMENT);
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
