/*
 * 源文件名：FoodDetailActivity
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：食谱详情
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteFoodCommentParams;
import com.zjhz.teacher.NetworkRequests.request.FoodCommentParams;
import com.zjhz.teacher.NetworkRequests.request.FoodDetailParams;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamFlaseFood;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamTrueFood;
import com.zjhz.teacher.NetworkRequests.request.ReplayFoodBean;
import com.zjhz.teacher.NetworkRequests.response.FoodBean;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.NetworkRequests.response.NewsReplayBean;
import com.zjhz.teacher.NetworkRequests.response.PraiseIdBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.DetaiImagelGridAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateObject;
import com.zjhz.teacher.ui.adapter.CommentAdapter;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.PullToRefreshView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.ui.view.images.bean.Image;
import com.zjhz.teacher.ui.view.images.utils.Util;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

@TargetApi(Build.VERSION_CODES.M)
public class FoodDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {

    private final static String TAG = FoodDetailActivity.class.getSimpleName();

    @BindView(R.id.title_back_img)
    TextView  titleLeft;
    @BindView(R.id.title_tv)
    TextView  titleTv;
    @BindView(R.id.right_text)
    TextView  titleRight;
    @BindView(R.id.food_type_tv)
    ImageView food_type_tv;
    @BindView(R.id.activity_food_detail_name)
    TextView name;
    @BindView(R.id.activity_food_detail_name_number)
    TextView nameNumber;
    @BindView(R.id.activity_food_detail_describe)
    TextView describe;
    @BindView(R.id.activity_food_detail_time)
    TextView time;
    @BindView(R.id.activity_food_detail_grid)
    GridView gridView;
    @BindView(R.id.activity_food_detail_list)
    ListView listView;
    PullToRefreshView mPullToRefreshView;
    @BindView(R.id.hint_rl)
    RelativeLayout hintRl;
    @BindView(R.id.dianzan_iv)
    ImageView dianzanIv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.edit_rl)
    RelativeLayout editRl;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et)
    TextView et;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.activity_food_detail_image)
    CircleImageView headImage;
    @BindView(R.id.look_more_ll)
    LinearLayout look_more_ll;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.hint_nocomment)
    RelativeLayout hint_nocomment;//没有评论图片
    private List<Image> mSelectedImage;
    private String cookbookId = "";
    private FoodBean bean;
    private int pageNum = 1;
    private int PageSize = 4;
    private boolean isPraised;
    private String praisedId;
    DetaiImagelGridAdapter gridAdapter;
    CommentAdapter listAdapter;
    private int praisedNum = 0 ,replayNum = 0;
    private boolean isupdateReplayNum = false,isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        cookbookId = getIntent().getStringExtra("cookbookId");
        initView();
        titleTv.setText(R.string.module_cookbook);//食谱详情
        dialog.show();
        NetworkRequest.request(new FoodDetailParams(cookbookId), CommonUrl.MENUDETAIL, Config.FOODDETAIL);
        getCommentList();
    }


    private void initView() {

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        titleRight.setText("0条评论");
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setPadding(5, 5, 5, 5);
        titleRight.setTextSize(10);
        titleRight.setBackgroundResource(R.mipmap.pinglunkuang);

        mPullToRefreshView = genericFindViewById(R.id.activity_food_detail_pull_refresh_view);

        listAdapter = new CommentAdapter(this);
        listAdapter.setItemCallBack(new CommentAdapter.ItemCallBack() {
            @Override
            public void delete(String id) {
                deleteComment(id, CommonUrl.removeCookBookReply);
            }
        });
        listView.setAdapter(listAdapter);

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());

    }


    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


    @OnClick({R.id.title_back_img,R.id.right_text,R.id.hint_rl, R.id.cancel_tv,R.id.dianzan_iv,R.id.sure_tv,R.id.look_more_ll})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                if(replayNum!=0) {
                    Intent intent = new Intent(FoodDetailActivity.this, CommentActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("newsId", cookbookId);
                    intent.putExtra("praisedNum", praisedNum);
                    startActivity(intent);
                }else{
                    ToastUtils.showShort("目前没有评论");
                }
                break;
            case R.id.look_more_ll:
                Intent intent1 = new Intent(FoodDetailActivity.this, CommentActivity.class);
                intent1.putExtra("type",2);
                intent1.putExtra("newsId", cookbookId);
                intent1.putExtra("praisedNum",praisedNum);
                startActivity(intent1);
                break;

            //点击评论
            case R.id.hint_rl:
                hintRl.setVisibility(View.GONE);
                dianzanIv.setVisibility(View.GONE);
                editRl.setVisibility(View.VISIBLE);
                et1.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(et1, 0);
                break;
            //点击取消
            case R.id.cancel_tv:
                hintEditRl();
                break;
            //点击点赞
            case R.id.dianzan_iv:
                if (!isPraised) {
                    NetworkRequest.request(new PraiseParamTrueFood(true, cookbookId), CommonUrl.foodPraise, Config.FOODPRAISE);
                } else {
                    NetworkRequest.request(new PraiseParamFlaseFood(false, cookbookId, praisedId), CommonUrl.foodPraise,  Config.FOODPRAISE);
                }
                break;
            //点击确定
            case R.id.sure_tv:
                String replyMsg = et1.getText().toString();
                if (!SharePreCache.isEmpty(replyMsg)) {
                    dialog.setMessage("正在添加评论...");
                    dialog.show();
                    NetworkRequest.request(new ReplayFoodBean(cookbookId, replyMsg), CommonUrl.foodReplay, Config.FOODREPLAY);
                    hintEditRl();
                } else {
                    ToastUtils.showShort("内容不能为空");
                }
                break;
        }
    }


    public void hintEditRl() {
        hintRl.setVisibility(View.VISIBLE);
        dianzanIv.setVisibility(View.VISIBLE);
        editRl.setVisibility(View.GONE);
        SharePreCache.hintInput(this);
        et1.setText("");
        et.setText("");
    }


    private void initData() {
        GlideUtil.loadImage(bean.getPhotoUrl(), headImage);
        if (bean.getPattern() == 1) {
            food_type_tv.setImageResource(R.mipmap.food_icon1);
        } else if (bean.getPattern() == 2) {
            food_type_tv.setImageResource(R.mipmap.food_icon2);
        } else if (bean.getPattern() == 3) {
            food_type_tv.setImageResource(R.mipmap.food_icon3);
        }

        name.setText(bean.getName());
        describe.setText(bean.getContent());
        nameNumber.setText(bean.getLookNum() + "人浏览");
        time.setText(bean.getPublishTime().split(" ")[0]);

        final List<ImageBean> images = bean.getImageUrls();
        if (images != null && images.size() > 0) {
            gridAdapter = new DetaiImagelGridAdapter(this, images);
            gridView.setAdapter(gridAdapter);
        }
        //图片浏览
        final List<Image> mSelectedImage = new ArrayList<>();
        for(int i =0 ;i<images.size();i++){
                Image image = new Image();
                image.setSelect(true);
                image.setPath(images.get(i).getDocPath());
                mSelectedImage.add(image);
        }

         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageGalleryActivity.show(FoodDetailActivity.this, Util.toArray(mSelectedImage), position, false);

            }
        });


        isPraised = bean.isPraise();
        praisedId = bean.getPraiseId();
//        replayNum = bean.getReplyNum();
        praisedNum = bean.getPraiseNum();
        if (isPraised){
            dianzanIv.setImageResource(R.mipmap.dianzan2_pre);
        }else {
            dianzanIv.setImageResource(R.mipmap.dianzan2_nor);
        }
    }


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum = 1;
                mPullToRefreshView.onHeaderRefreshComplete("更新于:"
                        + Calendar.getInstance().getTime().toLocaleString());
                getCommentList();
            }

        }, 2000);
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.onHeaderRefreshComplete();
            }
        }, 2000);
    }


    private void getCommentList() {
        NetworkRequest.request(new FoodCommentParams(cookbookId, pageNum, PageSize),CommonUrl.foodReplyList, Config.FOODREPLAYLIST);
    }


    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        dialog.dismiss();
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("操作失败");
                break;
            case Config.NOSUCCESS:
                ToastUtils.showShort("操作失败");
//                JSONObject o = (JSONObject) eventCenter.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            case Config.FOODDETAIL:
                JSONObject jsonObject = (JSONObject) eventCenter.getData();
                try {
                    bean = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(), FoodBean.class);
                    initData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  Config.FOODREPLAYLIST:
                JSONObject comments = (JSONObject) eventCenter.getData();
                try {
                    replayNum = comments.getInt("total");
                    comment_count.setText( replayNum + "条评论");
                    if (replayNum == 0){
                        titleRight.setText( "0条评论");
                        hint_nocomment.setVisibility(View.VISIBLE);
                    }else {
                        hint_nocomment.setVisibility(View.GONE);
                        titleRight.setText( replayNum + "条评论");
                    }
                    List<NewsReplayBean> replayBeens = GsonUtils.toArray(NewsReplayBean.class, comments);
                    if (replayBeens != null) {

                        listAdapter.setDatas(replayBeens);
                        listAdapter.notifyDataSetChanged();
                        if (replayBeens.size() == PageSize){
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
                        EventBus.getDefault().post(new EventCenter<>(Config.FOODUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case  Config.FOODPRAISE:
                JSONObject pri = (JSONObject) eventCenter.getData();
                try {
                    if (isPraised){ // 如果为true则表示取消点赞成功，如果为false则表示点赞成功
                        if (praisedNum >= 1){
                            praisedNum = praisedNum -1;
                        }
                        isPraised = false;
                        dianzanIv.setImageResource(R.mipmap.dianzan2_nor);
                        EventBus.getDefault().post(new EventCenter<>(Config.FOODUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }else {
                        praisedNum = praisedNum + 1;
                        isPraised = true;
                        PraiseIdBean b = GsonUtils.toObject(pri.getJSONObject("data").toString(), PraiseIdBean.class);
                        praisedId = b.getPraiseId();
                        dianzanIv.setImageResource(R.mipmap.dianzan2_pre);
                        EventBus.getDefault().post(new EventCenter<>(Config.FOODUPDATE,new UpdateObject(praisedNum,replayNum)));
                    }
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.FOODREPLAY:
                pageNum = 1;
                isupdateReplayNum = true;
                isAdd = true;
                getCommentList();
                break;
            case Config.FOODDELETECOMMENT:
                pageNum = 1;
                isupdateReplayNum = true;
                isAdd = false;
                getCommentList();
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
                NetworkRequest.request(new DeleteFoodCommentParams(id,cookbookId),url, Config.FOODDELETECOMMENT);
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
