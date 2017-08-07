package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ExecuteTaskRepairParams;
import com.zjhz.teacher.NetworkRequests.request.RepairsDetailParams;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.NetworkRequests.response.RepairsDetailBean;
import com.zjhz.teacher.NetworkRequests.response.RepairsTaskListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.DetaiImagelGridAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.RepairsListAdapter;
import com.zjhz.teacher.ui.dialog.DialogLeaveState;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-30
 * Time: 8:57
 * Description: 申请人报修申请状态
 */
public class RepairsProposerStateActivity extends BaseActivity {
    private final static String TAG = RepairsProposerStateActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_repairs_proposer_state_image)
    ImageView circleImageView;
    @BindView(R.id.activity_repairs_proposer_state_time)
    TextView time;
    @BindView(R.id.activity_repairs_proposer_state_item_name)
    TextView name;
    @BindView(R.id.activity_repairs_proposer_state_site)
    TextView site;
    @BindView(R.id.activity_repairs_proposer_state_branch)
    TextView branch;
    @BindView(R.id.activity_repairs_proposer_state_description)
    TextView description;
    @BindView(R.id.activity_repairs_proposer_state_name)
    TextView userName;
    @BindView(R.id.bottom_ll)
    LinearLayout bottom_ll;
    @BindView(R.id.activity_repairs_proposer_state_grid)
    ScrollViewWithGridView gridView;
    DetaiImagelGridAdapter gridAdapter ;
    @BindView(R.id.activity_repairs_proposer_state_list)
    ScrollViewWithListView listView;
    @BindView(R.id.more_ll)
    LinearLayout more_ll;
    @BindView(R.id.revoke_delete_tv)
    TextView rdTv;

    private int status = -1;//保修申请单状态
    private String repairId = "" ;
    private int approveFlag;
    private int type = 0;
    //    private RepairsPopwindow repairsPopwindow;
    private int from = 0;
    private int pageNum = 1,pageSize = 9,taskTotal = 0;
    private List<RepairsTaskListBean> listBeen = new ArrayList<>();
    private  RepairsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_proposer_state);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        Intent intent = getIntent();
        repairId = intent.getStringExtra("repairId");
        approveFlag = intent.getIntExtra("approveFlag",0);
        type = intent.getIntExtra("type",0);
        if (SharePreCache.isEmpty(repairId)){
            ToastUtils.showShort("报修单Id丢失");
            return;
        }
        initView();
        dialog.setMessage("正在获取报修详情...");
        dialog.show();
        getDetail();
    }

    private void getDetail() {
        if (type == 1){
            NetworkRequest.request(new RepairsDetailParams(repairId,true,pageNum,pageSize), CommonUrl.repairDtl,Config.REPAIRDETAIL);
        }else {
            NetworkRequest.request(new RepairsDetailParams(repairId,true,pageNum,pageSize), CommonUrl.approveDtlFlowDtl,Config.REPAIRDETAIL);
        }
    }

    private void initView() {
        titleTv.setText(R.string.repairs_title);
//        repairsPopwindow = new RepairsPopwindow(this);
        adapter = new RepairsListAdapter(this, listBeen);
        listView.setAdapter(adapter);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    @OnClick({R.id.title_back_img,R.id.repairs_btn,R.id.return_btn,R.id.more_ll,R.id.revoke_delete_tv})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.repairs_btn:
                DialogLeaveState dialogLeaveState = new DialogLeaveState(this);
                dialogLeaveState.setSureListener(new DialogLeaveState.SureListener() {
                    @Override
                    public void sureClick(String sure) {
                        if (!TextUtils.isEmpty(sure)) {
                            dialog.setMessage("正在提交...");
                            dialog.show();
                            NetworkRequest.request(new ExecuteTaskRepairParams(repairId, sure), CommonUrl.executeTask_repair, Config.REPAIRapproval);
                        } else {
                            ToastUtils.toast("审批意见不能为空");
                        }
                    }
                });
//                from = 1;
//                if (popupWindow == null){
//                    popupWindow = RepairsPopwindow.makePopwindow(this);
//                }
//                popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
                break;
            case R.id.return_btn:
                DialogLeaveState dialogLeaveState1 = new DialogLeaveState(this);
                dialogLeaveState1.setSureListener(new DialogLeaveState.SureListener() {
                    @Override
                    public void sureClick(String sure) {
                        if (!TextUtils.isEmpty(sure)) {
                            dialog.setMessage("正在提交...");
                            dialog.show();
                            NetworkRequest.request(new ExecuteTaskRepairParams(repairId, sure), CommonUrl.return_repair, Config.REPAIRapproval);
                        } else {
                            ToastUtils.toast("审批意见不能为空");
                        }
                    }
                });
//                from = 2;
//                if (popupWindow == null){
//                    popupWindow = RepairsPopwindow.makePopwindow(this);
//                }
//                popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
//                break;
            case R.id.more_ll:
                pageNum++;
                getDetail();
                break;
            case R.id.revoke_delete_tv:
                HashMap<String, String> map = new HashMap<>();
                map.put("repairId",repairId);
//                map.put("desc")
                if(status == 0)
                    NetworkRequest.request(map, CommonUrl.REPAIR_REVOKE, "repair_revoke");
                if(status == 4)
                    NetworkRequest.request(map, CommonUrl.REPAIR_DELETE, "repair_delete");
                break;
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1){
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey,"");
//                    toLoginActivity();
//                }else {
//                    try {
//                        ToastUtils.showShort(o.getString("msg"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
                break;
            case Config.REPAIRDETAIL:
                dialog.dismiss();
                JSONObject js = (JSONObject) event.getData();
                try {
                    RepairsDetailBean bean = GsonUtils.toObject(js.getJSONObject("data").toString(), RepairsDetailBean.class);
                    initData(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                break;
            case Config.REPAIRapproval:
                pageNum = 1;
                approveFlag = 2;
                EventBus.getDefault().post(new EventCenter<>("waitupdate",null));
                getDetail();
                break;
            case "repair_revoke":
                status = 4;
                ToastUtils.showShort("撤回成功");
                rdTv.setText("删除申请");
                Intent intent1 = new Intent();
                intent1.putExtra("revoke", true);
                setResult(RESULT_OK,intent1);
                break;
            case "repair_delete":
                ToastUtils.showShort("删除成功");
                rdTv.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.putExtra("isDelete", true);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
    private void initData( RepairsDetailBean bean) {
        if (bean != null){
            taskTotal = bean.getDtlTotal();
            if (!SharePreCache.isEmpty(bean.getPhotoUrl())){
                GlideUtil.loadImageHead(bean.getPhotoUrl(),circleImageView);
            }
            userName.setText(bean.getNickName());
            time.setText(SharePreCache.isEmpty(bean.getApplyTime())?"报修时间:":"报修时间: "+bean.getApplyTime().split(" ")[0]);
            name.setText(SharePreCache.isEmpty(bean.getItemName())?"报修物品:":"报修物品: "+bean.getItemName());
            site.setText(SharePreCache.isEmpty(bean.getOrginAddress())?"报修地点:":"报修地点: "+bean.getOrginAddress());
            branch.setText(SharePreCache.isEmpty(bean.getDutyDept())?"报修部门: ":"报修部门: "+bean.getDutyDeptVal());
            description.setText(SharePreCache.isEmpty(bean.getSummary()) ? "情况描述: ":"情况描述: " +bean.getSummary());
//            List<ImageBean> imgs = bean.getImgs();
//            if (imgs != null && imgs.size() > 0){
//                imageAdapter = new DetaiImagelGridAdapter(this,imgs);
//                gridView.setAdapter(imageAdapter);
//            }
            //浏览图片
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
                    ImageGalleryActivity.show(RepairsProposerStateActivity.this, Util.toArray(mSelectedImage), position, false);

                }
            });
            if (type == 1){
                bottom_ll.setVisibility(View.GONE);
                List<RepairsTaskListBean> taskList = bean.getTaskList();

                boolean isRevoke = false;
                for(RepairsTaskListBean bean1 : taskList){
                    if(bean1.getStatus() == 1){
                        isRevoke = true;
                        break;
                    }
                }
                if(!isRevoke) {
                    rdTv.setVisibility(View.VISIBLE);
                    rdTv.setText("撤回申请");
                    status = 0;
                }
                if(bean.getStatus() == 4) {
                    rdTv.setVisibility(View.VISIBLE);
                    rdTv.setText("删除申请");
                    status = 4;
                }
            }else if (type == 2){
                if (approveFlag == 1){
                    bottom_ll.setVisibility(View.VISIBLE);
                }else {
                    bottom_ll.setVisibility(View.GONE);
                }
            }
            List<RepairsTaskListBean> tasklist = bean.getTaskList();
            if ( tasklist != null && tasklist.size() > 0){
                if (pageNum == 1){
                    listBeen.clear();
                }
                for (int i = 0 ; i < tasklist.size() ; i ++){
                    if (tasklist.get(i).getStatus() != 0){
                        listBeen.add(tasklist.get(i));
                    }
                }
                if (listBeen.size() == 0 ){
                    more_ll.setVisibility(View.GONE);
                }else {
                    if (pageNum * pageSize < taskTotal){
                        more_ll.setVisibility(View.VISIBLE);
                    }else {
                        more_ll.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }else {
                more_ll.setVisibility(View.GONE);
            }
        }
        dialog.dismiss();
    }
//    @Override
//    public void sureClick(String reasonStr) {
//        dialog.setMessage("正在提交...");
//        dialog.show();
//        if (from == 1){
//            NetworkRequest.request(new ExecuteTaskRepairParams(repairId,reasonStr),CommonUrl.executeTask_repair,Config.REPAIRapproval);
//        }else if (from == 2){
//            NetworkRequest.request(new ExecuteTaskRepairParams(repairId,reasonStr),CommonUrl.return_repair,Config.REPAIRapproval);
//        }
//    }
}
