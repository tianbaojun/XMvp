package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CommentParams;
import com.zjhz.teacher.NetworkRequests.request.DeleteNewsCommentParams;
import com.zjhz.teacher.NetworkRequests.request.NewsDetailParams;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamFalseNews;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamTrueNews;
import com.zjhz.teacher.NetworkRequests.request.ReplayNewsBean;
import com.zjhz.teacher.NetworkRequests.response.NewsBean;
import com.zjhz.teacher.NetworkRequests.response.NewsReplayBean;
import com.zjhz.teacher.NetworkRequests.response.PraiseIdBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateObject;
import com.zjhz.teacher.ui.adapter.CommentAdapter;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.StringUtils;
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


public class NewsDetailActivity extends BaseActivity{
    String newsId;
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
//    @BindView(R.id.right_text)
//    TextView right_text;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
//    @BindView(R.id.refresh_layout)
//    RefreshLayout refreshLayout;
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
    @BindView(R.id.ll)
    FrameLayout ll;
    @BindView(R.id.dian_zhan_qp)
    ImageView bubbleImage;
    @BindView(R.id.dianzan_pinglun_count)
    TextView pinglunCountTv;
    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_time)
    TextView newsTime;
    @BindView(R.id.news_creat_user)
    TextView newsCreatUser;
    @BindView(R.id.news_brow)
    TextView newsBrow;
    @BindView(R.id.hint_nocomment)
    RelativeLayout hint_nocomment;//没有评论图片
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.look_more_ll)
    LinearLayout look_more_ll;

    @BindView(R.id.comment_count)
    TextView commentCount;


    private int pageNum = 1,pageSize = 4;
    private CommentAdapter adapter;
    private boolean isPraised; //初始是否点赞
    private String praisedId;
    private final static String TAG = NewsDetailActivity.class.getSimpleName();
    private int praisedNum,replayNum;
    private boolean isupdateReplayNum = false,isAdd = false;
    private String type = "";
    private NewsBean newsBean;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        Intent intent = getIntent();
        if (intent != null){
            newsId =intent.getStringExtra("newsId");
            LogUtil.e("新闻详情Id","newsId = " + newsId);
            type = intent.getStringExtra("type");
        }
        if (SharePreCache.isEmpty(newsId)) {
            ToastUtils.showShort("新闻ID丢失");
            return;
        }
        dialog.setMessage("正在获取详情...");
        dialog.show();
        initTitle();
        initView();
        getNewsDetail();
    }
    private void initTitle() {
        titleTv.setText(R.string.news_details);
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        right_text.setText("评论0条");
//        right_text.setVisibility(View.VISIBLE);
//        right_text.setPadding(5, 5, 5, 5);
//        right_text.setTextSize(10);
//        right_text.setBackgroundResource(R.mipmap.pinglunkuang);

    }

    private void initView() {
//        refreshLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(false);
//            }
//        }));
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshLayout.setRefreshing(false);
//            }
//        });
//        refreshLayout.setColorSchemeResources(Constance.colors);
//        View view = LayoutInflater.from(this).inflate(R.layout.header_news, null);
//        newsTitle = (TextView) view.findViewById(R.id.news_title);
//        newsTime= (TextView) view.findViewById(R.id.news_time);
//        newsCreatUser = (TextView) view.findViewById(R.id.news_creat_user);
//        newsBrow = (TextView) view.findViewById(R.id.news_brow);
//        hint_nocomment= (RelativeLayout) view.findViewById(R.id.hint_nocomment);

//        commentCount = (TextView) view.findViewById(R.id.comment_count);
//        Drawable imgoff = this.getResources().getDrawable(R.mipmap.pinglun3);
//        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
//        imgoff.setBounds(0, 0, imgoff.getMinimumWidth(), imgoff.getMinimumHeight());
//        commentCount.setCompoundDrawablePadding(30);//设置图片和text之间的间距
//        commentCount.setCompoundDrawables(imgoff,null,null,null);//设置TextView的drawableleft



//        web = (WebView) view.findViewById(R.id.web);
//        refreshListview.addHeaderView(view);
//        View foodview = LayoutInflater.from(this).inflate(R.layout.bottom_more, null);
//        look_more_ll = (LinearLayout) foodview.findViewById(R.id.look_more_ll);
//        look_more_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NewsDetailActivity.this, NewsCommentActivity.class);
//                intent.putExtra("type", 1);
//                intent.putExtra("newsId", newsId);
//                intent.putExtra("praisedNum", praisedNum);
//                startActivity(intent);
//            }
//        });
//        refreshListview.addFooterView(foodview);
        adapter = new CommentAdapter(this);
        adapter.setItemCallBack(new CommentAdapter.ItemCallBack() {
            @Override
            public void delete(String id) {
                deleteComment(id, CommonUrl.removeNewReply);
            }
        });
        refreshListview.setAdapter(adapter);
    }

    @OnClick({R.id.et,R.id.cancel_tv, R.id.blank, R.id.dianzan_iv,R.id.sure_tv, R.id.dianzan_pinglun_layout, R.id.look_more_ll})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
//            case R.id.right_text:
//                if(replayNum!=0) {
//                    Intent intent = new Intent(NewsDetailActivity.this,NewsCommentActivity.class);
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
                String replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();
                    NetworkRequest.request(new ReplayNewsBean(newsId, replyMsg), CommonUrl.newsReply, Config.NEWSREPLAY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
            case R.id.look_more_ll:
            case R.id.dianzan_pinglun_layout:
                if(replayNum!=0) {
                    Intent intent = new Intent(NewsDetailActivity.this,NewsCommentActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("newsId",newsId);
                    intent.putExtra("praisedNum",praisedNum);
                    intent.putExtra("isPraised", isPraised);
                    startActivity(intent);
                }else{
                    ToastUtils.showShort("目前没有评论");
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

    private void getNewsDetail() {
//        if (type.equals("img")){
//            NetworkRequest.request(new NewsImageDetailParams(newsId), CommonUrl.newsImageDetail,Config.NEWSDETAIL);
//        }else {
            NetworkRequest.request(new NewsDetailParams(newsId), CommonUrl.newsDetail,Config.NEWSDETAIL);
//        }
        getNewsCommentList();
    }

    private void getNewsCommentList() {
        NetworkRequest.request(new CommentParams(newsId,pageNum,pageSize), CommonUrl.newsReplyList,Config.NEWSREPLAYLIST);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
                break;
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            //获取新闻详情
            case  Config.NEWSDETAIL:
                dialog.dismiss();
                JSONObject ob = (JSONObject) event.getData();
                try {
                    newsBean = GsonUtils.toObject(ob.getJSONObject("data").toString(), NewsBean.class);
                    if (newsBean != null) {
                        newsTitle.setText(newsBean.getTitle());
//                        newsTime.setText(b.getPublishTime().split(" ")[0]);
                        newsTime.setText( DateUtil.getStandardDate(SharePreCache.isEmpty(newsBean.getPublishTime())?"":newsBean.getPublishTime()));
                        newsCreatUser.setText(newsBean.getPublishUserVal());
                        newsBrow.setText(newsBean.getLookNum()+"人浏览");
                        isPraised = newsBean.isPraise();
                        praisedId = newsBean.getPraiseId();
                        praisedNum = newsBean.getPraiseNum();
//                        replayNum = b.getReplyNum();
                        if (isPraised){
                            dianzanIv.setImageResource(R.mipmap.dianzan2_pre_1);
                        }else {
                            dianzanIv.setImageResource(R.mipmap.dianzan2_nor_1);
                        }
                        web.loadDataWithBaseURL(null, StringUtils.getNewContent(newsBean.getContent()) ,"text/html","UTF-8",null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //获取新闻回复列表
            case Config.NEWSREPLAYLIST:
                dialog.dismiss();
                JSONObject obs = (JSONObject) event.getData();
                LogUtil.e("新闻列表数据 = ",obs.toString());
                try {
                    replayNum = obs.getInt("total");
                    if (replayNum == 0){
//                        right_text.setText( "0条评论");
                        hint_nocomment.setVisibility(View.VISIBLE);
                        pinglunCountTv.setVisibility(View.GONE);
                    }else {
//                        right_text.setText( replayNum + "条评论");
                        hint_nocomment.setVisibility(View.GONE);
                        pinglunCountTv.setVisibility(View.VISIBLE);
                        pinglunCountTv.setText(replayNum+"");
                    }

                    commentCount.setText("评论"+replayNum +"条" );
                    List<NewsReplayBean> replayList = GsonUtils.toArray(NewsReplayBean.class, obs);
                    if (replayList != null) {
//                        adapter.datas.clear();
                        adapter.setDatas(replayList);
                        adapter.notifyDataSetChanged();
                        if ( replayList.size() == pageSize){
                            look_more_ll.setVisibility(View.VISIBLE);
                        }else {
                            look_more_ll.setVisibility(View.GONE);
                        }
                    }
                    if (isupdateReplayNum){
                        isupdateReplayNum = false;
                        if (isAdd){
                            ToastUtils.showShort("评论成功");
                        }else {
                            ToastUtils.showShort("删除评论成功");
                        }
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //获取添加文字回复
            case Config.NEWSREPLAY:
                isupdateReplayNum = true;
                isAdd = true;
                getNewsCommentList();
                break;
            //删除文字回复
            case Config.NEWSDELETECOMMENT:
                isupdateReplayNum = true;
                isAdd = false;
                getNewsCommentList();
                break;
            case Config.NEWSPRAISE:
                dialog.dismiss();
                JSONObject pri = (JSONObject) event.getData();
                try {
                    if (isPraised){ // 如果为true则表示取消点赞成功，如果为false则表示点赞成功
                        if (praisedNum > 0){
                            praisedNum = praisedNum -1;
                        }
                        isPraised = false;
                        dianzanIv.setImageResource(R.mipmap.dianzan2_nor_1);
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }else {
                        praisedNum = praisedNum +1;
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
                        EventBus.getDefault().post(new EventCenter<>(Config.NEWSUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
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
                dialog.setMessage("正在删除评论...");
                dialog.show();
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
