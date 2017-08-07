package pro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.CompanyNewsCommentDetail;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

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
import pro.widget.Pullable.PullToRefreshLayout;
import pro.widget.Pullable.PullableListView;

public class CompanyNewsCommentDetailActivity extends BaseActivity {

    private ImageView dianzan_iv;
    private TextView comment_praise_num;
    private TextView first_reply_tv;
    private TextView all_reply_tv;
    private TextView comment_username_tv;
    private TextView comment_time_tv;
    private TextView comment_content_tv;
    private CircleImageView comment_header_iv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.activity_company_news_comment_detail)
    View root;
    @BindView(R.id.content_view)
    PullableListView reply_listView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refresh_layout;
    @BindView(R.id.et)
    TextView et;
    @BindView(R.id.hint_rl)
    LinearLayout hintRl;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.edit_rl)
    RelativeLayout editRl;

    //新闻id
    private String newsId = "";
    //对评论的回复进行回复是需要传    代表评论的id
    private String rootId = "";
    //评论的回复的用户id
    private String replyUser = "";
    //回复的那条消息的id
    private String refReplyId = "";
    private int page = 1;
    private int pageSize = 10;
    //评论回复列表请求参数
    private Map<String, String> map = new HashMap<>();
    private CompanyNewsCommentReplyAdapter adapter;
    private List<CompanyNewsCommentDetail.ReplyListBean> replys = new ArrayList();
    private CompanyNewsCommentDetail detail;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_news_comment_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title_tv.setText("评论详情");
        Intent intent = getIntent();
        newsId = intent.getStringExtra("newsId");
        rootId = intent.getStringExtra("replyId");
        refReplyId = rootId;
        map.put("newsId", newsId);
        map.put("rootId", rootId);
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        NetworkRequest.request(map, CommonUrl.COMPANYNEWSREPLYDTL, Config.COMPANYNEWSREPLYDTL);
        dialog.show();
        View view = View.inflate(this, R.layout.company_news_comment_detail_list_header, null);
        //初始化头布局
        initHeader(view);
        //发送按钮初始不能点击
//        send_tv.setClickable(false);
        //列表不能下拉
        reply_listView.setCanPullDown(false);
        reply_listView.addHeaderView(view);
        reply_listView.setDivider(null);
        adapter = new CompanyNewsCommentReplyAdapter(this, R.layout.item_company_comment_reply, replys);
        reply_listView.setAdapter(adapter);
        //对评论的回复进行回复   自己的不能回复
        reply_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && !replys.get(position - 1).getCreateUser().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))) {
                    hintRl.setVisibility(View.GONE);
                    editRl.setVisibility(View.VISIBLE);
                    et1.requestFocus();
                    ViewTools.popSoftInput(CompanyNewsCommentDetailActivity.this);
                    et1.setText("");
                    et1.setHint("回复 " + replys.get(position - 1).getUserName());
                    refReplyId = replys.get(position - 1).getReplyId();
                    replyUser = replys.get(position - 1).getCreateUser();
//                    rootId = replys.get(position - 1).getRootId();
                }else{
                    ToastUtils.showShort("不能回复自己");
                }
            }
        });
        //滑动就变成回复评论
       /* reply_listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    hintEditRl();
                    et1.setText("");
                    et1.setHint("回复 ");
                    refReplyId = detail.getReplyId();
                    replyUser = detail.getCreateUser();
                    rootId = "";
                    ViewTools.hideSoftInput(CompanyNewsCommentDetailActivity.this);
                }
                return false;
            }
        });*/
        refresh_layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                return;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (detail!=null&&detail.getReplyNum() > replys.size()) {
                    page++;
                    getDate();
                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }
            }
        });
    }

    /**
     * 列表头布局初始化
     *
     * @param view
     */
    private void initHeader(View view) {
        dianzan_iv = (ImageView) view.findViewById(R.id.dianzan_iv);
        comment_praise_num = (TextView) view.findViewById(R.id.comment_praise_num);
        first_reply_tv = (TextView) view.findViewById(R.id.first_reply_tv);
        all_reply_tv = (TextView) view.findViewById(R.id.all_reply_tv);
        comment_username_tv = (TextView) view.findViewById(R.id.comment_username_tv);
        comment_time_tv = (TextView) view.findViewById(R.id.comment_time_tv);
        comment_content_tv = (TextView) view.findViewById(R.id.comment_content_tv);
        comment_header_iv = (CircleImageView) view.findViewById(R.id.comment_header_iv);
        //点赞操作
        dianzan_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("newsId", detail.getNewsId());
                map.put("replyId", detail.getReplyId());
                if (detail.getPraiseStatus() == 1) {
                    map.put("praiseStatus", String.valueOf(0));
                } else {
                    map.put("praiseStatus", String.valueOf(1));
                }
                NetworkRequest.request(map, CommonUrl.COMPANYNEWSPRAISEOCANCEL, Config.COMPANYNEWSPRAISEOCANCEL);
                dialog.show();
            }
        });
        first_reply_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintRl.setVisibility(View.GONE);
                editRl.setVisibility(View.VISIBLE);
                et1.requestFocus();
                ViewTools.popSoftInput(CompanyNewsCommentDetailActivity.this);
            }
        });
    }

    /**
     * 回复列表
     */
    private void getDate() {
        Map<String ,String> map = new HashMap<>();
        map.put("newsId", newsId);
        map.put("rootId", rootId);
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        NetworkRequest.request(map, CommonUrl.COMPANYNEWSREPLYSERVICELIST, Config.COMPANYNEWSREPLYSERVICELIST);
    }

    @OnClick({R.id.title_back_img, R.id.et,R.id.blank, R.id.cancel_tv, R.id.sure_tv})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            //点击评论
            case R.id.et:
                hintRl.setVisibility(View.GONE);
                editRl.setVisibility(View.VISIBLE);
                et1.requestFocus();
                et1.setHint("回复");
                ViewTools.popSoftInput(this);
                break;
            //点击取消
            case R.id.blank:
            case R.id.cancel_tv:
                hintEditRl();
                break;
            //点击确定
            case R.id.sure_tv:
                String replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();
                    map.put("newsId", newsId);
                    map.put("rootId", rootId.equals(refReplyId)?"":rootId);
                    map.put("refReplyId", refReplyId);
                    map.put("replyContent", replyMsg);
                    map.put("replyUser", replyUser);
                    NetworkRequest.request(map, CommonUrl.COMPANYNEWSADDNEWMSGREPLY, Config.COMPANYNEWSADDNEWMSGREPLY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("无网络");
                break;
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                if (page > 1) {
                    refresh_layout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                break;
            case Config.COMPANYNEWSREPLYDTL:
                JSONObject obs = (JSONObject) event.getData();
                CompanyNewsCommentDetail detail = GsonUtils.toObjetJson(obs, CompanyNewsCommentDetail.class);
                if (detail != null) {
                    this.detail = detail;
                    comment_username_tv.setText(detail.getUserName());
                    comment_time_tv.setText(detail.getCreateTime());
                    comment_content_tv.setText(detail.getReplyContent());
                    comment_praise_num.setText(String.valueOf(detail.getPraiseNum()));
                    replyUser = detail.getCreateUser();
//                    title_tv.setText(detail.getReplyNum() + "条回复");
//                    是否可以点赞 0 是 1否
                    if (detail.getPraiseStatus() == 1) {
                        dianzan_iv.setImageResource(R.mipmap.dianzan2_pre_1);
                    }
                    GlideUtil.loadImageHead(detail.getPhotoUrl(), comment_header_iv);
                    //全部评论，抢先评论
                    if (detail.getReplyNum() > 0) {
                        all_reply_tv.setVisibility(View.VISIBLE);
                        reply_listView.setVisibility(View.VISIBLE);
                        replys.clear();
                        replys.addAll(detail.getReplyList());
                        adapter.notifyDataSetChanged();
                        first_reply_tv.setVisibility(View.GONE);
                    } else {
                        first_reply_tv.setVisibility(View.VISIBLE);
                        all_reply_tv.setVisibility(View.GONE);
                    }
                }
                break;
            case Config.COMPANYNEWSADDNEWMSGREPLY:   //发送评论回复
                dialog.dismiss();
                page = 1;
                getDate();
                ViewTools.hideSoftInput(this);
                et1.setText("");
                et1.setHint("回复");
                break;
            case Config.COMPANYNEWSREPLYSERVICELIST:   //获取评论回复列表
                List<CompanyNewsCommentDetail.ReplyListBean> list = GsonUtils.toArray(CompanyNewsCommentDetail.ReplyListBean.class, (JSONObject) event.getData());
                if (list != null) {
                    if (page == 1) {
                        replys.clear();
                    }
                    replys.addAll(list);
                }
                adapter.notifyDataSetChanged();
                if (replys.size() == 0) {
                    first_reply_tv.setVisibility(View.VISIBLE);
                    all_reply_tv.setVisibility(View.GONE);
                    reply_listView.setVisibility(View.GONE);
                } else {
                    first_reply_tv.setVisibility(View.GONE);
                    all_reply_tv.setVisibility(View.VISIBLE);
                    reply_listView.setVisibility(View.VISIBLE);
                }
                if (page > 1) {
                    refresh_layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                break;
            case Config.COMPANYNEWSPRAISEOCANCEL:
                dialog.dismiss();
                if (this.detail != null) {
                    JSONObject js = (JSONObject) event.getData();
                    if (this.detail.getPraiseStatus() == 1) {
                        dianzan_iv.setImageResource(R.mipmap.dianzan2_nor_1);
                        this.detail.setPraiseStatus(0);
                    } else {
                        dianzan_iv.setImageResource(R.mipmap.dianzan2_pre_1);
                        this.detail.setPraiseStatus(1);
                    }
                    try {
                        comment_praise_num.setText(js.getJSONObject("data").optString("praiseNum"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    public void hintEditRl() {
        hintRl.setVisibility(View.VISIBLE);
        editRl.setVisibility(View.GONE);
        ViewTools.hideSoftInput(this);
        refReplyId = rootId;
        replyUser = detail.getCreateUser();
        et1.setText("");
        et.setText("");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
}
