/*
 * 源文件名：HomeWorkPreviewActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：作业（详情）预览
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.zjhz.teacher.NetworkRequests.request.WorkDetailParams;
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewDeleteRequest;
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewLikeRequest;
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewRequest;
import com.zjhz.teacher.NetworkRequests.request.WorkPreviewUnLikeRequest;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.DetaiImagelGridAdapter;
import com.zjhz.teacher.adapter.WorkPreviewGridAdapter;
import com.zjhz.teacher.adapter.WorkPreviewOneAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.WorkPreviewBean;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.ui.view.images.bean.Image;
import com.zjhz.teacher.ui.view.images.utils.Util;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;import butterknife.BindView;

public class HomeWorkPreviewActivity extends BaseActivity implements WorkPreviewOneAdapter.OnDeleteClickListener {

    private final static String TAG = HomeWorkPreviewActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_work_preview_image)
    CircleImageView headImage;
    @BindView(R.id.activity_work_preview_user_name)
    TextView userName;
    @BindView(R.id.activity_work_preview_name)
    TextView name;
    @BindView(R.id.activity_work_preview_subject)
    TextView subject;
    @BindView(R.id.activity_work_preview_issue_time)
    TextView issueTime;
    @BindView(R.id.activity_work_preview_work_time)
    TextView workTime;
    @BindView(R.id.activity_work_preview_hand_in_time)
    TextView handInTime;
    @BindView(R.id.activity_work_preview_grid)
    ScrollViewWithGridView gridGrade;
    @BindView(R.id.activity_work_preview_content)
    TextView contentWork;
//    @BindView(R.id.activity_work_preview_voice)
//    TextView teacherVoice;
    @BindView(R.id.comment_count)
    TextView comment_count;  // 评论数
    @BindView(R.id.activity_work_preview_grid_image)
    ScrollViewWithGridView gridView;
    @BindView(R.id.activity_work_preview_list_image_discuss)
    ListView listView;
    @BindView(R.id.hint_rl_preview)
    RelativeLayout relativeLayout;
    @BindView(R.id.et_preview)
    TextView textDiscuss;
    @BindView(R.id.dianzan_iv_preview)
    ImageView like;
    @BindView(R.id.edit_rl_preview)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.cancel_tv_preview)
    TextView cancel_tv_preview;
    @BindView(R.id.sure_tv_preview)
    TextView sure_tv_preview;
    @BindView(R.id.et1_preview)
    EditText editText;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.activity_work_preview_list_image_discuss_more)
    TextView more;
    @BindView(R.id.hint_nocomment)
    RelativeLayout hint_nocomment;//没有评论图片
    WorkPreviewGridAdapter textGridAdapter;
    DetaiImagelGridAdapter gridAdapter;
    WorkPreviewOneAdapter listViewAdapter;
    List<WorkPreviewBean> lists = new ArrayList<>();
    List<WorkPreviewBean> nums = new ArrayList<>();  // 计数
    boolean isLike;
    int fly;
    private String homeworkId, praiseNum;
    private String praiseId;
    private int replayNum=0;
    private String imgListStr="";//图片逗号拼接
    private HintPopwindow hintPopwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_preview);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        homeworkId = getIntent().getStringExtra("homeworkId");
        praiseNum = getIntent().getStringExtra("praisenu");

        if (SharePreCache.isEmpty(homeworkId)) {
             ToastUtils.showShort("homeworkId丢失");
            return;
        }
        dialog.setMessage("获取详情...");
        dialog.show();
        titleTv.setText(getResources().getText(R.string.work_manager));
        right_text.setVisibility(View.VISIBLE);
        right_text.setBackgroundResource(R.mipmap.pinglunkuang);
        right_text.setPadding(5, 5, 5, 5);
        right_text.setTextSize(10);
//        right_text.setText("0条评论");
        comment_count.setText("全部评论");
        Drawable imgoff = this.getResources().getDrawable(R.mipmap.pinglun2);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        imgoff.setBounds(0, 0, imgoff.getMinimumWidth(), imgoff.getMinimumHeight());
        comment_count.setCompoundDrawablePadding(30);//设置图片和text之间的间距
        comment_count.setCompoundDrawables(imgoff,null,null,null);//设置TextView的drawableleft

        listViewAdapter = new WorkPreviewOneAdapter(lists, this, this);
        listView.setAdapter(listViewAdapter);
       /* gridImage.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击图片，进行预览
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    intent.putExtra("ID", id);
                    intent.putExtra("imgListStr", imgListStr);
                    intent.setClass(HomeWorkPreviewActivity.this, ImageViewActivity.class);
                    startActivityForResult(intent,1);

            }
        });*/
        getData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        lists.clear();
//        dialog.show();
//        NetworkRequest.request(new WorkDetailParams(homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLIST);
//    }

    private void getData() {
        NetworkRequest.request(new WorkDetailParams(homeworkId), "TeachHomeworkService.dtl", "detail");
        NetworkRequest.request(new WorkDetailParams(homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLIST);
    }

    private void initData(HomeWorkBean bean) {
        if (bean != null) {
            String imgUrl = bean.getPhotoUrl();
            if (!SharePreCache.isEmpty(imgUrl)) {
                GlideUtil.loadImageHead(imgUrl, headImage);
            }
            userName.setText("教师:" + bean.getCreateUserName());
            name.setText(bean.getHomeworkName());
            subject.setText("科　　目：" + bean.getSubjectName());
            issueTime.setText("发布时间：" + bean.getPublishTime().substring(0,16));
            workTime.setText("作业时长：" + bean.getCostTime() + "分钟");
            String deliverTime = bean.getDeliverTime();
            if (!SharePreCache.isEmpty(deliverTime)) {
                if (deliverTime.split(" ").length == 2) {
                    handInTime.setText("上交时间：" + bean.getDeliverTime().split(" ")[0]);
                } else {
                    handInTime.setText("上交时间：" + deliverTime);
                }
            }
            contentWork.setText("作业内容：" + bean.getContent());

            if (bean.getLink() != null) {
                textGridAdapter = new WorkPreviewGridAdapter(bean.getLink(), this);
                gridGrade.setAdapter(textGridAdapter);
            }
           /* if (bean.getImgs() != null) {
                imageGridAdapter = new DetaiImagelGridAdapter(this, bean.getImgs());
                gridImage.setAdapter(imageGridAdapter);
                for(int i=0;i<bean.getImgs().size();i++){
                    imgListStr+=bean.getImgs().get(i).getDocPath()+",";
                }
            } else {
                gridImage.setVisibility(View.GONE);
            }*/

            final List<ImageBean> images = bean.getImgs();
            if (images != null && images.size() > 0) {
                gridAdapter = new DetaiImagelGridAdapter(this, images);
                gridView.setAdapter(gridAdapter);
            }

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
                    ImageGalleryActivity.show(HomeWorkPreviewActivity.this, Util.toArray(mSelectedImage), position, false);

                }
            });
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case "detail":
                dialog.dismiss();
                JSONObject jsonBean = (JSONObject) event.getData();
                try {
                    HomeWorkBean bean = GsonUtils.toObject(jsonBean.getJSONObject("data").toString(), HomeWorkBean.class);
                    initData(bean);
                    boolean isPraise = jsonBean.optJSONObject("data").optBoolean("isPraise");
                    if (isPraise) {
                        like.setImageResource(R.mipmap.dianzan2_pre);
                        isLike = false;
                    } else {
                        like.setImageResource(R.mipmap.dianzan2_nor);
                        isLike = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                JSONObject noLike = (JSONObject) event.getData();
                int code = noLike.optInt("code");
                if (code == 5045) {
                    isLike = true;
                    ToastUtils.toast("此作业已经点赞");
                }
                break;
            case Config.HOMEWORKDISCUSSREMOVE:
                dialog.dismiss();
//                JSONObject remove = (JSONObject) event.getData();
                dialog.show();
                ToastUtils.toast("删除评论成功");
                NetworkRequest.request(new WorkDetailParams(homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLIST);
//                lists.remove(fly);
//                listView.requestLayout();
//                listViewAdapter.notifyDataSetChanged();
//                if (lists.size() == 0) {
//                    right_text.setText("全部评论");
//                    comment_count.setText("全部评论");
//                }else{
//                    right_text.setText(lists.size() + "条评论");
//                    comment_count.setText(lists.size() + "条评论");
//                }
                break;
            case Config.HOMEWORKLIKE: // 赞
                dialog.dismiss();
                isLike = false;
//                ToastUtils.toast2("+1");
//                ToastUtils.toast("点赞成功");
                JSONObject praise = (JSONObject) event.getData();
                if (praise != null) {
                    JSONObject data = praise.optJSONObject("data");
                    if (data != null) {
                        praiseId = data.optString("praiseId");
                        SharedPreferencesUtils.putSharePrefString("fly_praiseId",praiseId);
                    }
                }
                like.setImageResource(R.mipmap.dianzan2_pre);
                break;
            case Config.HOMEWORKUNLIKE: // 取消赞
                dialog.dismiss();
                isLike = true;
//                ToastUtils.toast("取消点赞");
//                ToastUtils.toast2("-1");

                like.setImageResource(R.mipmap.dianzan2_nor);
                break;
            case Config.HOMEWORKDISCUSSADD:
//                dialog.dismiss();
                ToastUtils.toast("评论成功");
                editText.setText("");
                NetworkRequest.request(new WorkDetailParams(homeworkId), CommonUrl.HOMEWORKDISCUSSLIST, Config.HOMEWORKDISCUSSLIST);
                break;
            case Config.HOMEWORKDISCUSSLIST:  // 评论列表
                dialog.dismiss();
                lists.clear();
                JSONObject discuss = (JSONObject) event.getData();
                int total = discuss.optInt("total");
                LogUtil.e("评论数据 = ",discuss.toString());
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
                                mWorkPreviewBean.homeworkId = homeworkId;
//                                nums.add(mWorkPreviewBean);
//                                if (lists.size() > 3) {
//                                } else {
                                    if (lists.size() == 3) {
                                        more.setVisibility(View.VISIBLE);
                                    }
                                    lists.add(mWorkPreviewBean);
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                replayNum=lists.size();
                if (lists.size() == 0) {
                    comment_count.setText("评论0条");
                    right_text.setText("0条评论");
                    hint_nocomment.setVisibility(View.VISIBLE);
                }else{
                    right_text.setText(total + "条评论");
                    comment_count.setText("评论"+total +"条" );
                    hint_nocomment.setVisibility(View.GONE);
                }
                listView.requestLayout();
                listViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.title_back_img, R.id.et_preview, R.id.dianzan_iv_preview, R.id.cancel_tv_preview, R.id.sure_tv_preview, R.id.right_text, R.id.activity_work_preview_list_image_discuss_more})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                if(replayNum!=0) {
                    Intent intent = new Intent(this, HomeWorkCommentActivity.class);
                    intent.putExtra("homeworkId", homeworkId);
                    startActivity(intent);
                }else{
                     ToastUtils.showShort("目前没有评论");
                }
                break;
            case R.id.activity_work_preview_list_image_discuss_more:
                Intent intentF = new Intent(this, HomeWorkCommentActivity.class);
                intentF.putExtra("homeworkId", homeworkId);
                startActivity(intentF);
                break;
            case R.id.et_preview:  // 点击显示评论
                relativeLayout.setVisibility(View.GONE);
                mRelativeLayout.setVisibility(View.VISIBLE);
                BaseUtil.upKeyboard(editText);
                editText.requestFocus();
                break;
            case R.id.dianzan_iv_preview:  // 点赞
                if (isLike) {
                    dialog.setMessage("正在点赞");
                    dialog.show();
                    NetworkRequest.request(new WorkPreviewLikeRequest(homeworkId, true), CommonUrl.HOMEWORKLIKE, Config.HOMEWORKLIKE);
                } else {
                    dialog.setMessage("正在取消点赞");
                    dialog.show();
                    NetworkRequest.request(new WorkPreviewUnLikeRequest(homeworkId, SharedPreferencesUtils.getSharePrefString("fly_praiseId")), CommonUrl.HOMEWORKUNLIKE, Config.HOMEWORKUNLIKE);
                }
                break;
            case R.id.cancel_tv_preview:  // 取消
                relativeLayout.setVisibility(View.VISIBLE);
                mRelativeLayout.setVisibility(View.GONE);
                BaseUtil.downKeyboard(editText);
                break;
            case R.id.sure_tv_preview:  // 确定
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    dialog.show();
                    relativeLayout.setVisibility(View.VISIBLE);
                    mRelativeLayout.setVisibility(View.GONE);
                    BaseUtil.downKeyboard(editText);
                    LogUtil.e("homeId+++++", homeworkId);
                    NetworkRequest.request(new WorkPreviewRequest(homeworkId, editText.getText().toString().trim()), CommonUrl.HOMEWORKDISCUSSADD, Config.HOMEWORKDISCUSSADD);
                } else {
                    ToastUtils.toast("评论内容不能为空");
                }
                break;
        }
    }

    @Override
    public void onDeleteClickListener(int position, final String replyId) {
        fly = position;
//        dialog.show();
        LogUtil.e("作业预览", "homeworkId = " + homeworkId + "   replyId = " + replyId);
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
                NetworkRequest.request(new WorkPreviewDeleteRequest(homeworkId, replyId), CommonUrl.HOMEWORKDISCUSSREMOVE, Config.HOMEWORKDISCUSSREMOVE);
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
