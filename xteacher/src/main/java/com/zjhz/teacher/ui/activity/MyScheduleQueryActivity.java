package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.MyScheduleQueryNamePaging;
import com.zjhz.teacher.NetworkRequests.request.MyScheduleQueryPaging;
import com.zjhz.teacher.NetworkRequests.response.MyScheduleQueryNamePagingResponse;
import com.zjhz.teacher.NetworkRequests.response.MyScheduleQueryPagingResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.MyScheduleQueryAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.PullToRefreshView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师
 */
public class MyScheduleQueryActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener  {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_my_schedule_query_ed)
    EditText editText;
    @BindView(R.id.activity_my_schedule_query_list_view)
    ListView listView;
    @BindView(R.id.activity_my_schedule_query_pull_refresh_view)
    PullToRefreshView mPullToRefreshView;
    private final static String TAG = MyScheduleQueryActivity.class.getSimpleName();
    int currentPage = 1;
    boolean type = true;
    private List<String> lists = new ArrayList<>();
    private MyScheduleQueryAdapter adapter;
    private List<MyScheduleQueryPagingResponse.DataBean> names = new ArrayList<>();
    private List<MyScheduleQueryNamePagingResponse.DataBean> data1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule_query);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        dialog.show();
        initData();
        NetworkRequest.request(new MyScheduleQueryPaging(String.valueOf(1),"50"), CommonUrl.MYSCHEDULEPAGING_NEW, Config.MYSCHEDULEPAGING_NEW);
    }
    private void initData() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        titleTv.setText("我的课表");
        adapter = new MyScheduleQueryAdapter(this,lists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (type){
                    intent.putExtra("teacherId",names.get(position).teacherId);
                    intent.putExtra("teacherName",names.get(position).name);
                }else {
                    intent.putExtra("teacherId",data1.get(position).getTeacherId());
                    intent.putExtra("teacherName",data1.get(position).getName());
                }
                setResult(RESULT_OK,intent);
                ViewTools.hideSoftInput(MyScheduleQueryActivity.this);
                finish();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        type = false;
                        dialog.show();
                        currentPage = 1;
                        NetworkRequest.request(new MyScheduleQueryNamePaging(editable.toString(),String.valueOf(1),"50"), CommonUrl.MYSCHEDULENAMEPAGING, Config.MYSCHEDULENAMEPAGING);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.title_back_img})
    public void clickEvent(View v){
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()){
            case R.id.title_back_img:
                ViewTools.hideSoftInput(MyScheduleQueryActivity.this);
                finish();
                break;
        }
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
                break;
            case Config.MYSCHEDULEPAGING_NEW:
                dialog.dismiss();
                if (currentPage == 1){
                    names.clear();
                    lists.clear();
                }
                JSONObject data = (JSONObject) event.getData();
                LogUtil.e("我的课表 = ",data.toString());
                MyScheduleQueryPagingResponse mMyScheduleQueryPagingResponse = GsonUtils.toObject(data.toString(),MyScheduleQueryPagingResponse.class);
                if (mMyScheduleQueryPagingResponse !=null){
                    names.addAll(mMyScheduleQueryPagingResponse.data);
                    for (int i = 0; i < mMyScheduleQueryPagingResponse.data.size() ; i++) {
                        lists.add(mMyScheduleQueryPagingResponse.data.get(i).name);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case Config.MYSCHEDULENAMEPAGING:
                dialog.dismiss();
                if (currentPage == 1){
                    data1.clear();
                    lists.clear();
                }
                JSONObject name = (JSONObject) event.getData();
                MyScheduleQueryNamePagingResponse mMyScheduleQueryNamePagingResponse = GsonUtils.toObject(name.toString(),MyScheduleQueryNamePagingResponse.class);
                if (mMyScheduleQueryNamePagingResponse != null){
                    data1.addAll(mMyScheduleQueryNamePagingResponse.getData());
                    for (int i = 0; i < mMyScheduleQueryNamePagingResponse.getData().size() ; i++) {
                        lists.add(mMyScheduleQueryNamePagingResponse.getData().get(i).getName());
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mPullToRefreshView.onHeaderRefreshComplete("更新于:"
//                        + Calendar.getInstance().getTime().toLocaleString());
                mPullToRefreshView.onHeaderRefreshComplete();
                lists.clear();
                dialog.show();
                currentPage = 1;
                if (type) {
                    NetworkRequest.request(new MyScheduleQueryPaging(String.valueOf(1),"50"), CommonUrl.MYSCHEDULEPAGING_NEW, Config.MYSCHEDULEPAGING_NEW);
                }else{
                    NetworkRequest.request(new MyScheduleQueryNamePaging(editText.getText().toString().trim(),String.valueOf(1),"50"), CommonUrl.MYSCHEDULENAMEPAGING, Config.MYSCHEDULENAMEPAGING);
                }
            }

        }, 10);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
//                ToastUtils.toast("加载更多数据!");
                if (lists.size() < 15) {

                }else{
//                    dialog.show();
                    ++currentPage;
                    if (type) {
                        NetworkRequest.request(new MyScheduleQueryPaging(String.valueOf(currentPage),"15"), CommonUrl.MYSCHEDULEPAGING_NEW, Config.MYSCHEDULEPAGING_NEW);
                    }else{
                        NetworkRequest.request(new MyScheduleQueryNamePaging(editText.getText().toString().trim(),String.valueOf(currentPage),"15"), CommonUrl.MYSCHEDULENAMEPAGING, Config.MYSCHEDULENAMEPAGING);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }, 500);
    }
}
