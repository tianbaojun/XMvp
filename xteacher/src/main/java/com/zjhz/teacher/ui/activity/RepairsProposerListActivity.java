package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.FragAdapter;
import com.zjhz.teacher.ui.delegate.RepairsDelegate;
import com.zjhz.teacher.ui.fragment.MyRepairsFragment;
import com.zjhz.teacher.ui.fragment.WaitRepairsFragment;

import org.greenrobot.eventbus.Subscribe;
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
 * Date: 2016-06-30
 * Time: 8:57
 * Description: 申请人报修申请列表
 */
public class RepairsProposerListActivity extends BaseActivity {
    @BindView(R.id.leave_title_apply)
    TextView apply;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.leave_title_approve)
    TextView approve;
    @BindView(R.id.layout_title)
    LinearLayout layout_title;
    @BindView(R.id.leave_list_view_pager)
    ViewPager view_pager;
    @BindView(R.id.activity_leave_apply_drawer_layout)
    public DrawerLayout drawer_layout;
    @BindView(R.id.drawer_layout_classes_name)
    public TextView drawer_layout_classes_name;
    @BindView(R.id.nv_drawer_layout)
    public LinearLayout nv_drawer_layout;
    List<Fragment> fragments = new ArrayList<>();
    private final static String TAG = RepairsProposerListActivity.class.getSimpleName();
//    private boolean isEntryFlag;
    public int type = 0;
    private RepairsDelegate delegate;
    private boolean isShow = true ;
    private  boolean EntryFlag;//true为有审批权限
    //添加按钮
    @BindView(R.id.right_addicon)
    ImageView right_addicon;
    //筛选按钮
    @BindView(R.id.right_selecticon)
    ImageView right_selecticon;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply_for_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        delegate = new RepairsDelegate(this);
        delegate.initDelegat();
        //先判断下是否有审批权限
        NetworkRequest.request(null, CommonUrl.approve,Config.approveEntry);

    }
    private void init() {
        /*leave_add_tv.setText("申请");
        leave_side_tv.setText("筛选");
        Drawable drawable = getResources().getDrawable(R.mipmap.add_icon);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        leave_add_tv.setCompoundDrawables(drawable,null,null,null);
        Drawable drawableLeave = getResources().getDrawable(R.mipmap.work_side);
        drawableLeave.setBounds(0, 0, drawableLeave.getMinimumWidth(), drawableLeave.getMinimumHeight());
        leave_side_tv.setCompoundDrawables(drawableLeave,null,null,null);*/
        fragments.add(new MyRepairsFragment());
        if (EntryFlag){
            layout_title.setVisibility(View.VISIBLE);
            apply.setText(R.string.my_apply);
            approve.setText(R.string.pending_endorsement);
            fragments.add(new WaitRepairsFragment());

        }else {//无审批权限
            title_tv.setText(R.string.repairs_title);
            layout_title.setVisibility(View.GONE);
            approve.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
        }

        FragAdapter fragadapter = new FragAdapter(getSupportFragmentManager(),fragments);
        view_pager.setAdapter(fragadapter);
        view_pager.setCurrentItem(0);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    type = 0;
                    apply.setBackgroundResource(R.drawable.yuanjiao_message_white);
                    approve.setBackgroundResource(R.drawable.yuanjiao_contacts);
                    apply.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
                    approve.setTextColor(getResources().getColor(R.color.white));
                    view_pager.setCurrentItem(0);
                }else {
                    type = 1;
                    apply.setBackgroundResource(R.drawable.yuanjiao_message);
                    approve.setBackgroundResource(R.drawable.yuanjiao_contacts_wright);
                    apply.setTextColor(getResources().getColor(R.color.white));
                    approve.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
                    view_pager.setCurrentItem(1);
                }
            }    @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    @OnClick({R.id.leave_title_left,R.id.right_selecticon,R.id.right_addicon,R.id.leave_title_apply,R.id.leave_title_approve})
    public void clickEvent(View v){
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()){
            case R.id.leave_title_left:
                finish();
                break;
           /* case R.id.right_selecticon:
                if (isShow){
                    isShow = false;
                    leave_more_btn.setVisibility(View.VISIBLE);
                }else {
                    isShow = true;
                    leave_more_btn.setVisibility(View.GONE);
                }
                break;*/
            case R.id.right_addicon:
                isShow = true;
//                leave_more_btn.setVisibility(View.GONE);
                startActivityForResult(new Intent(this, RepairsProposerActivity.class), 1);
                break;
            case R.id.right_selecticon:
                isShow = true;
//                leave_more_btn.setVisibility(View.GONE);
                drawer_layout.openDrawer(nv_drawer_layout);
                break;
            case R.id.leave_title_apply:
                view_pager.setCurrentItem(0);
                break;
            case R.id.leave_title_approve:
                view_pager.setCurrentItem(1);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 1){
                view_pager.setCurrentItem(0);
                Intent intent = new Intent();
                intent.setAction("MyRepairsFragment");
                intent.putExtra("isAdd",true);
                sendBroadcast(intent);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.approveEntry:
                JSONObject o = (JSONObject) event.getData();
                try {
                    EntryFlag = o.getJSONObject("data").getBoolean("EntryFlag");
                    init();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                break;
//            case "approve":
//                JSONObject json = (JSONObject) event.getData();
//                try {
//                    ApproveBean bean = GsonUtils.toObject(json.getJSONObject("data").toString(),ApproveBean.class);
//                    isEntryFlag = bean.isEntryFlag();
//                    init();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                dialog.dismiss();
//                break;
        }
    }
}
