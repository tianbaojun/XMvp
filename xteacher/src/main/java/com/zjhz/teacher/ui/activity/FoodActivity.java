/*
 * 源文件名：FoodActivity
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：今日食谱
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PageNumSize;
import com.zjhz.teacher.NetworkRequests.request.ReplayFoodBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.FoodProBean;
import com.zjhz.teacher.bean.UpdateObject;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import pro.adapter.FoodAdapter;
import pro.kit.ViewTools;

public class FoodActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon)
    ImageView right_icon;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    //评论的布局
    @BindView(R.id.comment_rl)
    RelativeLayout comment_rl;
    //评论编辑框
    @BindView(R.id.comment_et)
    EditText comment_et;
    //评论发送按钮
    @BindView(R.id.send_tv)
    TextView send_tv;

    private String foodId;

    private DefaultLoadingLayout loadingLayout;
    private int total = 0;
    private FoodAdapter adapter;
    private int pageNum = 1;
    private int pageSize = 10;
    private final static String TAG = FoodActivity.class.getSimpleName();
    private int index;

    private List<FoodProBean> list = new ArrayList<>();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        initView();
        initListener();
        getData();
    }

    private void getData() {
        NetworkRequest.request(new PageNumSize(pageNum,pageSize), CommonUrl.NEWLIST, Config.FOODLIST);
    }

    private void initView() {
        loadingLayout = DefaultLoadingLayout.createDefaultLayout(this, refreshLayout);
        titleTv.setText(R.string.module_cookbook);
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right_icon.setVisibility(View.VISIBLE);
        right_icon.setImageResource(R.mipmap.add_icon);

        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        adapter = new FoodAdapter(this,R.layout.item_classz_moments_hounor_roll,list);
        refreshListview.setAdapter(adapter);
//        refreshListview.setOnItemClickListener(this);
        refreshListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==(MotionEvent.ACTION_DOWN)){
                    comment_rl.setVisibility(View.GONE);
                    comment_et.setText("");
                    ViewTools.hideSoftInput(FoodActivity.this);
                }
                return false;
            }
        });
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum= 1 ;
                getData();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum++ ;
                if (adapter.getCount() < total){
                    getData();
                }else {
                    refreshLayout.setLoading(false);
                }
            }
        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        Intent intent = new Intent(FoodActivity.this,FoodDetailActivity.class);
        intent.putExtra("cookbookId",adapter.getItem(position).getCookbookId());
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                hintRefresh();
                ToastUtils.showShort("请求出错");
                break;
            case Config.NOSUCCESS:
                hintRefresh();
                break;
            case Config.FOODLIST:
                hintRefresh();
                JSONObject object = (JSONObject) event.getData();
                total = object.optInt("total");
                List<FoodProBean> foodBean = GsonUtils.toArray(FoodProBean.class, object);
                if (foodBean != null && foodBean.size() > 0) {
                    if (pageNum == 1){
                        list.clear();
//                        adapter.setDatasFood(foodBean);
                    }/*else {
                        adapter.addAll(foodBean);
                    }*/
                    list.addAll(foodBean);
                    if(list.size()>0) {
                        loadingLayout.onDone();
                        adapter.notifyDataSetChanged();
                    }else{
                        loadingLayout.onEmpty();
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case Config.FOODUPDATE:
                UpdateObject be = (UpdateObject) event.getData();
                FoodProBean bean =  adapter.getItem(index);
                int PraiseNum = be.PraiseNum;
                int ReplyNum = be.ReplyNum;
                bean.setPraiseNum(PraiseNum);
                bean.setReplyNum(ReplyNum);
                adapter.notifyDataSetChanged();
                break;
            case Config.SHOWCOMMENTDIALOG:   //显示评论弹窗，在点击弹窗中的评论
//                inputMenu.setVisibility(View.VISIBLE);
                comment_rl.setVisibility(View.VISIBLE);
                comment_et.requestFocus();
                ViewTools.popSoftInput(this);
                foodId = event.getData().toString();
                break;
        }
    }

    private void hintRefresh(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @OnClick({R.id.right_icon,R.id.send_tv})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()){
            case R.id.right_icon:
                startActivityForResult(new Intent(this,pro.ui.activity.EditFoodActivity.class),1);
                break;
            case R.id.send_tv:  //发送评论
                String content = ((TextView) comment_et).getText().toString();
                comment_et.setText("");
                if (!SharePreCache.isEmpty(content)) {
                    ViewTools.hideSoftInput(this);
                    comment_rl.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(content)) {
                        NetworkRequest.request(new ReplayFoodBean(foodId, content), CommonUrl.foodReplay, Config.COMMENT);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                pageNum= 1;
                getData();
            }
        }
    }
}
