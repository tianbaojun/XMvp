package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteHomeworkParams;
import com.zjhz.teacher.NetworkRequests.request.PageNumSizeStatus;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.WorkManagerAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 草稿箱
 * Created by Administrator on 2016/6/25.
 */
public class DraftActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    private int pageNum = 1;
    private int pageSize = 12;
    private int total = 0;
    private List<HomeWorkBean> workManagers = new ArrayList<>();
    private WorkManagerAdapter adapter;
    private boolean isopenLeft = true;
    private int status = 1;
    private String homeworkIds = "";
    private final static String TAG = DraftActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        initData();
        initListener();
        getData();
    }

    private String homeworkId;
    private void initData() {
        titleTv.setText("草稿箱");
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(R.mipmap.work_delete);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        adapter = new WorkManagerAdapter(this, workManagers,false);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
                homeworkId = workManagers.get(position).getHomeworkId();
                Intent intent = new Intent(DraftActivity.this, HomeWorkEditFisrtActivity.class);
                intent.putExtra("homeworId", workManagers.get(position).getHomeworkId());
                intent.putExtra("draft", "1");
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                Intent intent = new Intent("HomeWorkManagerActivity");
                sendBroadcast(intent);
                finish();
            }
        }else if (resultCode == 2){
            pageNum = 1;
            getData();
        }
    }


    @OnClick({R.id.title_back_img,R.id.right_icon,R.id.right_text})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.right_icon:
                if (workManagers.size() > 0 ){
                    if (isopenLeft){
                        right_text.setVisibility(View.VISIBLE);
                        rightIcon.setVisibility(View.GONE);
                        right_text.setText("完成");
                        adapter.setAnimation(true);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.right_text:
                isopenLeft = true;
                right_text.setVisibility(View.GONE);
                rightIcon.setVisibility(View.VISIBLE);
                int size = adapter.getLists().size();
                if (size > 0){
                    for (int  i = 0 ; i < size ; i++){
                        String id = adapter.getLists().get(i);
                        homeworkIds += (id +",");
                    }
                }
                if (!SharePreCache.isEmpty(homeworkIds)){
                    homeworkIds.substring(0,homeworkIds.length() - 1);
                    deleteData();
                }
                adapter.setAnimation(false);
                adapter.notifyDataSetChanged();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }
    private void getData() {
        NetworkRequest.request(new PageNumSizeStatus(pageNum, pageSize,status, SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey)), CommonUrl.homeworkList, "draftworkList");
    }

    /**删除对应数据*/
    private void deleteData() {
        NetworkRequest.request(new DeleteHomeworkParams(homeworkIds), CommonUrl.homeworkRemove, "deletework");
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case "noSuccess":
                hintRefresh();
                break;
            case "fly":  // TODO
                NetworkRequest.request(new DeleteHomeworkParams(homeworkId), CommonUrl.homeworkRemove, "deletework");
                break;
            case "deletework":
                homeworkIds= "";
                ToastUtils.showShort("删除成功");
                pageNum = 1;
                getData();
                break;
            case "draftworkList":
                hintRefresh();
                JSONObject object = (JSONObject) event.getData();
                total = object.optInt("total");
                List<HomeWorkBean> homeWorkBeen = GsonUtils.toArray(HomeWorkBean.class, object);
                if (homeWorkBeen != null && homeWorkBeen.size() > 0) {
                    if (pageNum == 1) {
                        workManagers.clear();
                        workManagers.addAll(homeWorkBeen);
                    } else {
                        workManagers.addAll(homeWorkBeen);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (pageNum == 1){
                    if (homeWorkBeen == null || homeWorkBeen.size() == 0){
                        workManagers.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
    private void hintRefresh() {
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }
    @Override
    public void onLoad() {
        if (workManagers.size() < total) {
            pageNum++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }
    @Override
    public void onRefresh() {
        pageNum = 1;
        getData();
    }

}
