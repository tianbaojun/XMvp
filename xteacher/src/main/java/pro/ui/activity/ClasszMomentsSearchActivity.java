package pro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentSearchListParams;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsComment;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.ClasszMomentCommentBean;
import com.zjhz.teacher.bean.ClasszMomentType;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.adapter.ClasszMomentsAdapter;
import pro.adapter.itemDelagate.ClasszMomentsDocument;
import pro.adapter.itemDelagate.ClasszMomentsHonour;
import pro.adapter.itemDelagate.ClasszMomentsShare;
import pro.kit.ViewTools;

public class ClasszMomentsSearchActivity extends BaseActivity implements RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    //搜索输入框
    @BindView(R.id.search_et)
    EditText search_et;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //班级动态tv
    @BindView(R.id.share_tv)
    TextView share_tv;
    //学习资源tv
    @BindView(R.id.document_tv)
    TextView document_tv;
    //班级红榜tv
    @BindView(R.id.honour_tv)
    TextView honour_tv;
    //搜索结果列表
    @BindView(R.id.refresh_listview)
    ListView refresh_listview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refresh_layout;
    @BindView(R.id.comment_rl)
    RelativeLayout comment_rl;
    @BindView(R.id.comment_et)
    EditText comment_et;
    @BindView(R.id.send_tv)
    TextView send_tv;
   /* @BindView(R.id.listView)
    HorizontalScrollView listView;*/

    //动态集合
    private ClasszMomentSearchListParams params = new ClasszMomentSearchListParams();
    private int page = 1;
    private List<ClasszMoment> moments = new ArrayList<>();
    private DefaultLoadingLayout loadingLayout;
    //ListView的适配器
    private ClasszMomentsAdapter adapter;
    //类型适配器
    private CommonAdapter<ClasszMomentType> typeCommonAdapter;
    //班级动态类型
    private List<ClasszMomentType> classzMomentsTypes = new ArrayList<>();
    //动态总条数
    private int total = 0;

    private String momentDid;
    private int commentType;
    private ClasszMomentCommentBean beRepCommentBean;

    private final String SHOWCOMMENTDIALOG = Config.SHOWCOMMENTDIALOG+"SEARCH";
    private final String SHOWCOMMENTDIALOGINLIST = Config.SHOWCOMMENTDIALOGINLIST+"SEARCH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments_search);
        ButterKnife.bind(this);
        title_tv.setText(R.string.activity_classz_moments_search_title);
        Intent intent = getIntent();
        if (!SharePreCache.isEmpty(intent.getStringExtra("class_id"))) {
            params.setClassId(intent.getStringExtra("class_id"));
        }
        getMomentType();
        params.setPage(String.valueOf(page));
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refresh_layout);
//        loadingLayout.onEmpty();
        adapter = new ClasszMomentsAdapter(this, moments, "SEARCH");
        adapter.regesterEventBus(true);
        adapter.addItemViewDelegate(new ClasszMomentsShare(adapter, moments, "SEARCH"));
        adapter.addItemViewDelegate(new ClasszMomentsHonour());
        adapter.addItemViewDelegate(new ClasszMomentsDocument());
        refresh_layout.setOnLoadListener(this);
        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setColorSchemeResources(Constance.colors);
        refresh_listview.setAdapter(adapter);
        refresh_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==(MotionEvent.ACTION_DOWN)){
                    comment_rl.setVisibility(View.GONE);
                    comment_et.setText("");
                    ViewTools.hideSoftInput(ClasszMomentsSearchActivity.this);
                }
                return false;
            }
        });
    }

    private void getMomentType() {
        //请求动态类型
        NetworkRequest.request(null, CommonUrl.MOMENTTYPELIST, Config.MOMENTTYPELIST);
    }

    /**
     * 请求班级动态
     */
    private void getData() {
        String searchWord = search_et.getText().toString();
        if (SharePreCache.isEmpty(searchWord)) {
            ToastUtils.showShort("请输入搜索内容");
            return;
        } else {
            params.setSearchValue(searchWord);
        }
        NetworkRequest.request(params, CommonUrl.SEARCHLIST, Config.SEARCHLIST);
        loadingLayout.onLoading();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.title_back_img, R.id.share_rl, R.id.document_rl, R.id.honour_rl,R.id.send_tv})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                setResult(1031);
                finish();
                break;
            case R.id.share_rl: //班级动态
                setTvColorDefault(share_tv);
                for (ClasszMomentType type : classzMomentsTypes) {
                    if ("1001".equals(type.getDcCode()))
                        params.setDcId(type.getDcId());
                }
                page =1;
                params.setPage(String.valueOf(1));
                moments.clear();
                adapter.notifyDataSetChanged();
                getData();
                ViewTools.hideSoftInput(this);
                break;
            case R.id.document_rl:  //学习资源
                setTvColorDefault(document_tv);
                for (ClasszMomentType type : classzMomentsTypes) {
                    if ("1002".equals(type.getDcCode()))
                        params.setDcId(type.getDcId());
                }
                page =1;
                params.setPage(String.valueOf(1));
                moments.clear();
                adapter.notifyDataSetChanged();
                getData();
                ViewTools.hideSoftInput(this);
                break;
            case R.id.honour_rl:  //班级红榜
                setTvColorDefault(honour_tv);
                for (ClasszMomentType type : classzMomentsTypes) {
                    if ("1003".equals(type.getDcCode()))
                        params.setDcId(type.getDcId());
                }
                page =1;
                params.setPage(String.valueOf(1));
                moments.clear();
                adapter.notifyDataSetChanged();
                getData();
                ViewTools.hideSoftInput(this);
                break;
            case R.id.send_tv:   //发送评论
                String content = ((TextView) comment_et).getText().toString();
                comment_et.setText("");
                if (!SharePreCache.isEmpty(content)) {
                    ViewTools.hideSoftInput(this);
                    comment_rl.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(content)) {
                        if (commentType == 0) {
                            ClasszMomentsComment commentParams = new ClasszMomentsComment(momentDid, content, "0", SharedPreferencesUtils.getSharePrefString("login_userid"));
                            NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT+"SEARCH");
                        } else if (commentType == 1) {  //回复
                            ClasszMomentsComment commentParams = new ClasszMomentsComment(beRepCommentBean.getdId(), content, "0", SharedPreferencesUtils.getSharePrefString("login_userid"));
                            commentParams.setBrepUserId(beRepCommentBean.getUserId());
                            commentParams.setParentId(beRepCommentBean.getDcId());
                            NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT+"SEARCH");
                        }
                    }
                }
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Config.SEARCHLIST:
                refresh_layout.setRefreshing(false);
                refresh_layout.setLoading(false);
                JSONObject j = (JSONObject) eventCenter.getData();
                try {
                    total = j.getInt("total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<ClasszMoment> list = GsonUtils.toArray(ClasszMoment.class, j);
                if (list != null && list.size() > 0) {
                    if (page == 1) {
                        moments.clear();
                    }
                    moments.addAll(list);
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                } else {
                    loadingLayout.onEmpty();
                }
                break;
            case Config.MOMENTTYPELIST:   //获取动态类型集合
                JSONObject jo = (JSONObject) eventCenter.getData();
                classzMomentsTypes.clear();
                classzMomentsTypes.addAll(GsonUtils.toArray(ClasszMomentType.class, jo));
                break;

            case SHOWCOMMENTDIALOG:   //显示评论弹窗，在点击弹窗中的评论
//                inputMenu.setVisibility(View.VISIBLE);
                comment_rl.setVisibility(View.VISIBLE);
                ViewTools.popSoftInput(this);
                comment_et.requestFocus();
                momentDid = eventCenter.getData().toString();
                commentType = 0;
                break;
            case SHOWCOMMENTDIALOGINLIST:
//                inputMenu.setVisibility(View.VISIBLE);
                comment_rl.setVisibility(View.VISIBLE);
                ViewTools.popSoftInput(this);
                comment_et.requestFocus();
                beRepCommentBean = (ClasszMomentCommentBean) eventCenter.getData();
                commentType = 1;
                break;
        }
    }


    /**
     * 将三个选项的字体颜色设为默认
     */
    private void setTvColorDefault(View v) {
        share_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color65));
        document_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color65));
        honour_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color65));
        ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.title_background_red));
    }

    @Override
    public void onRefresh() {
        page = 1;
        params.setPage(String.valueOf(page));
        getData();
    }

    @Override
    public void onLoad() {
        if (moments != null && moments.size() < total) {
            page++;
            params.setPage(String.valueOf(page));
            getData();
        } else {
            refresh_layout.setLoading(false);
        }
    }
}
