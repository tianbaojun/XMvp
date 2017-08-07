package com.zjhz.teacher.ui.activity;

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
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewDeleteRequest;
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.WorkPreviewAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.WorkPreviewBean;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业（详情）预览 评论
 */
public class HomeWorkCommentActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener,WorkPreviewAdapter.OnDeleteClickListener {

    private final static String TAG = HomeWorkCommentActivity.class.getSimpleName();
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
    WorkPreviewAdapter listViewAdapter;
    List<WorkPreviewBean> lists = new ArrayList<>();
    int fly;
    private String homeworkId;
    private int total;
    private int pageNum = 1;
    private int pageSize = 12;
    private int type;
    private HintPopwindow hintPopwindow;

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
        homeworkId = getIntent().getStringExtra("homeworkId");
        if (SharePreCache.isEmpty(homeworkId)) {
             ToastUtils.showShort("ID丢失");
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
//        refreshLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(true);
//            }
//        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        listViewAdapter = new WorkPreviewAdapter(lists,this,this);
        refreshListview.setAdapter(listViewAdapter);
    }

    private void getData() {
        dialog.show();
        NetworkRequest.request(new WorkPreviewParams("1","15",homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLISTS);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case "error":
                dialog.dismiss();
                stopRefresh();
                 ToastUtils.showShort("请求错误");
                break;
            case "noSuccess":
                dialog.dismiss();
                stopRefresh();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1){
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey,"");
//                    toLoginActivity();
//                }
                break;
            case Config.HOMEWORKDISCUSSREMOVEDIS:
                ToastUtils.toast("删除评论成功");
                EventBus.getDefault().post(new EventCenter<>("HOMEWORKDISCUSSREMOVE",null));
                dialog.dismiss();
                lists.remove(fly);
                refreshListview.requestLayout();
                listViewAdapter.notifyDataSetChanged();
                if (lists.size() == 0) {
                    rightText.setText("全部评论");
                }else{
                    commentCount.setText("最新评论");
                    rightText.setText(lists.size() + "条评论");
                }
                break;
            case Config.HOMEWORKDISCUSSLISTS:
                dialog.dismiss();
                stopRefresh();
                JSONObject discuss = (JSONObject) event.getData();
                if (discuss != null) {
                    JSONArray data = discuss.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                WorkPreviewBean mWorkPreviewBean = new WorkPreviewBean();
                                mWorkPreviewBean.headImage = o.optString("photoUrl");
                                mWorkPreviewBean.name = o.optString("name");
                                mWorkPreviewBean.content = o.optString("msgContect");
                                mWorkPreviewBean.time = o.optString("replyTime");
                                mWorkPreviewBean.replyId = o.optString("replyId");
                                lists.add(mWorkPreviewBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (lists.size() == 0) {
                    rightText.setText("全部评论");
                }else{
                    commentCount.setText( "最新评论");
                    rightText.setText(lists.size() + "条评论");
                }
                refreshListview.requestLayout();
                listViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.title_back_img})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    private void stopRefresh(){
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        lists.clear();
        getData();
    }

    @Override
    public void onLoad() {
//        if (listViewAdapter.getCount() < total){
            pageNum++;
            NetworkRequest.request(new WorkPreviewParams(pageNum+"","15",homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLISTS);
//        }else {
//            refreshLayout.setLoading(false);
//        }
    }

    @Override
    public void onDeleteClickListener(int position, final String replyId) {
        fly = position;
//        dialog.show();
        LogUtil.e("评论列表","homeworkId = " + homeworkId + "     replyId = " + replyId);
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
                NetworkRequest.request(new WorkPreviewDeleteRequest(homeworkId,replyId), CommonUrl.HOMEWORKDISCUSSREMOVE, Config.HOMEWORKDISCUSSREMOVEDIS);
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
