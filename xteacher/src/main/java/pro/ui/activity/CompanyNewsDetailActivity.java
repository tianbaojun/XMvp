package pro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import com.zjhz.teacher.NetworkRequests.request.PraiseParamFalseNews;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamTrueNews;
import com.zjhz.teacher.NetworkRequests.response.PraiseIdBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.CompanyNewsCommentDetail;
import com.zjhz.teacher.bean.CompanyNewsDetail;
import com.zjhz.teacher.bean.UpdateObject;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.adapter.CompanyNewsCommentReplyAdapter;
import pro.kit.ViewTools;

public class CompanyNewsDetailActivity extends BaseActivity {
    private final static String TAG = CompanyNewsDetailActivity.class.getSimpleName();
    String newsId;
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
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
    private int pageNum = 1, pageSize = 4;
    private CompanyNewsCommentReplyAdapter adapter;
    private boolean isPraised; //初始是否点赞
    private String praisedId;
    private int praisedNum, replayNum;
    private boolean isupdateReplayNum = false, isAdd = false;
    private String type = "";
    private CompanyNewsDetail newsBean;
    private List<CompanyNewsCommentDetail.ReplyListBean> replys = new ArrayList();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            newsId = intent.getStringExtra("newsId");
            LogUtil.e("新闻详情Id", "newsId = " + newsId);
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
        titleTv.setText("资讯详情");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        adapter = new CompanyNewsCommentReplyAdapter(this, R.layout.item_company_comment_reply, replys);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyNewsDetailActivity.this,CompanyNewsCommentDetailActivity.class);
                intent.putExtra("newsId",newsBean.getNewsId());
                intent.putExtra("replyId",newsBean.getList().get(position).getReplyId());
                startActivity(intent);
            }
        });
        refreshListview.setDivider(new ColorDrawable(getResources().getColor(R.color.gray_d)));
        refreshListview.setDividerHeight(1);
        dianzanIv.setVisibility(View.GONE);
        findViewById(R.id.bainji_iv).setVisibility(View.GONE);
    }

    @OnClick({R.id.et, R.id.cancel_tv, R.id.blank, R.id.dianzan_iv, R.id.sure_tv, R.id.dianzan_pinglun_layout, R.id.look_more_ll})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
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
                if (!isPraised) {
                    NetworkRequest.request(new PraiseParamTrueNews(true, newsId), CommonUrl.newsPraise, Config.NEWSPRAISE);
                } else {
                    NetworkRequest.request(new PraiseParamFalseNews(false, newsId, praisedId), CommonUrl.newsPraise, Config.NEWSPRAISE);
                }
                break;
            //点击确定
            case R.id.sure_tv:
                String replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("newsId", newsId);
                    map.put("replyContent", replyMsg);
                    NetworkRequest.request(map, CommonUrl.COMPANYNEWSADDREPLY, Config.NEWSREPLAY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
            case R.id.look_more_ll:
            case R.id.dianzan_pinglun_layout:
                if (replayNum != 0) {
                    Intent intent = new Intent(CompanyNewsDetailActivity.this, CompanyNewsCommentActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("newsId", newsId);
                    intent.putExtra("replyNum", newsBean.getReplyNum());
                    startActivity(intent);
                } else {
                    ToastUtils.showShort("目前没有评论");
                }
                break;
        }
    }

    private void showInput() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(et1, 0);
    }

    public void hintEditRl() {
        hintRl.setVisibility(View.VISIBLE);
        editRl.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editRl.getWindowToken(), 0);
        et1.setText("");
        et.setText("");
    }
    //查询新闻详情
    private void getNewsDetail() {
        NetworkRequest.request(new CommentParams(newsId, pageNum, pageSize), CommonUrl.COMPANYNEWSDTL, Config.NEWSDETAIL);
    }

    //分页查询评论列表
    private void getNewsCommentList() {
        NetworkRequest.request(new CommentParams(newsId, pageNum, pageSize), CommonUrl.COMPANYNEWSREPLYLIST, Config.NEWSREPLAYLIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNum=1;
        getNewsCommentList();
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
                break;
            //获取新闻详情
            case Config.NEWSDETAIL:
                dialog.dismiss();
                JSONObject ob = (JSONObject) event.getData();
                try {
                    newsBean = GsonUtils.toObject(ob.getJSONObject("data").toString(), CompanyNewsDetail.class);
                    if (newsBean != null) {
                        replayNum = newsBean.getReplyNum();
                        newsTitle.setText(newsBean.getTitle());
                        newsTime.setText(DateUtil.getStandardDate(SharePreCache.isEmpty(newsBean.getCreateTime()) ? "" : newsBean.getCreateTime()));
                        newsBrow.setText(newsBean.getLookNum() + "人浏览");
                        praisedNum = newsBean.getPraiseNum();

                        if (newsBean.getReplyNum() == 0) {
                            hint_nocomment.setVisibility(View.VISIBLE);
                            pinglunCountTv.setVisibility(View.GONE);
                        } else {
                            hint_nocomment.setVisibility(View.GONE);
                            pinglunCountTv.setVisibility(View.VISIBLE);
                            pinglunCountTv.setText(newsBean.getReplyNum() + "");
                        }
                        commentCount.setText("评论" + newsBean.getReplyNum() + "条");
                        if (newsBean.getList() != null) {
                            replys.clear();
                            replys.addAll(newsBean.getList());
                            adapter.notifyDataSetChanged();
                            if (newsBean.getReplyNum() > pageSize) {
                                look_more_ll.setVisibility(View.VISIBLE);
                            } else {
                                look_more_ll.setVisibility(View.GONE);
                            }
                        }
                        web.loadDataWithBaseURL(null, StringUtils.getNewContent(newsBean.getContent()), "text/html", "UTF-8", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //获取新闻回复列表
            case Config.NEWSREPLAYLIST:
                dialog.dismiss();
                JSONObject obs = (JSONObject) event.getData();
                LogUtil.e("新闻列表数据 = ", obs.toString());
                try {
                    replayNum = obs.getInt("total");
                    if (replayNum == 0) {
                        hint_nocomment.setVisibility(View.VISIBLE);
                        pinglunCountTv.setVisibility(View.GONE);
                    } else {
                        hint_nocomment.setVisibility(View.GONE);
                        pinglunCountTv.setVisibility(View.VISIBLE);
                        pinglunCountTv.setText(replayNum + "");
                    }

                    commentCount.setText("评论" + replayNum + "条");
                    List<CompanyNewsCommentDetail.ReplyListBean> replayList = GsonUtils.toArray(CompanyNewsCommentDetail.ReplyListBean.class, obs);
                    if (replayList != null) {
                        replys.clear();
                        replys.addAll(replayList);
                        newsBean.setList(replys);
                        adapter.notifyDataSetChanged();
                        if (replayList.size() == pageSize) {
                            look_more_ll.setVisibility(View.VISIBLE);
                        } else {
                            look_more_ll.setVisibility(View.GONE);
                        }
                    }
                    if (isupdateReplayNum) {
                        isupdateReplayNum = false;
                        if (isAdd) {
                            ToastUtils.showShort("评论成功");
                        } else {
                            ToastUtils.showShort("删除评论成功");
                        }
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
        }
    }
}
