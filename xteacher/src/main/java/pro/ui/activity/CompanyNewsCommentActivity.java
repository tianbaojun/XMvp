package pro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.CompanyNewsCommentDetail;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
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

import static com.zjhz.teacher.NetworkRequests.Config.NEWSREPLAY;

/**
 * 评论
 * Created by xiangxue on 2016/6/17.
 */
public class CompanyNewsCommentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
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

    private CompanyNewsCommentReplyAdapter adapter;
    private String newsId;
    private int total = 0, pageNum = 1, pageSize = 12;
    private String replyMsg;
    private List<CompanyNewsCommentDetail.ReplyListBean> comments = new ArrayList<>();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            newsId = intent.getStringExtra("newsId");
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
        adapter = new CompanyNewsCommentReplyAdapter(this, R.layout.item_company_comment_reply, comments);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyNewsCommentActivity.this,CompanyNewsCommentDetailActivity.class);
                intent.putExtra("newsId",comments.get(position).getNewsId());
                intent.putExtra("replyId",comments.get(position).getReplyId());
                startActivity(intent);
            }
        });
        dianzanIv.setVisibility(View.GONE);
        findViewById(R.id.dianzan_pinglun_layout).setVisibility(View.GONE);
        findViewById(R.id.bainji_iv).setVisibility(View.GONE);
    }

    private void getData() {
        NetworkRequest.request(new CommentParams(newsId, pageNum, pageSize), CommonUrl.COMPANYNEWSREPLYLIST, Config.NEWSREPLAYLISTs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNum=1;
        getData();
    }

    @OnClick({R.id.et, R.id.cancel_tv, R.id.blank, R.id.sure_tv})
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
                ViewTools.popSoftInput(this);
                break;
            //点击取消
            case R.id.blank:
                hintEditRl();
                break;
            case R.id.cancel_tv:
                hintEditRl();
                break;
            //点击确定
            case R.id.sure_tv:
                replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("newsId", newsId);
                    map.put("replyContent", replyMsg);
                    NetworkRequest.request(map, CommonUrl.COMPANYNEWSADDREPLY, NEWSREPLAY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
        }
    }

    public void hintEditRl() {
        hintRl.setVisibility(View.VISIBLE);
        editRl.setVisibility(View.GONE);
        ViewTools.hideSoftInput(this);
        et1.setText("");
        et.setText("");
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        if (dialog.isShowing())
            dialog.dismiss();
        switch (event.getEventCode()) {
            case Config.ERROR:
                stopRefresh();
                ToastUtils.showShort("请求错误");
                break;
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求错误");
                stopRefresh();
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
                    } else {
                        rightText.setText(total + "条评论");
                    }
                    pinglunCountTv.setText(String.valueOf(total));
                    List<CompanyNewsCommentDetail.ReplyListBean> replayList = GsonUtils.toArray(CompanyNewsCommentDetail.ReplyListBean.class, obs);
                    if (replayList != null) {
                        if (pageNum == 1) {
                            comments.clear();
                            comments.addAll(replayList);
                        } else {
                            if (replayList.size() > 0) {
                                comments.addAll(replayList);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case NEWSREPLAY:
                ToastUtils.showShort("评论成功");
                pageNum = 1;
                getData();
                break;
        }
    }

    private void stopRefresh() {
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
        if (adapter.getCount() < total) {
            pageNum++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }
}
