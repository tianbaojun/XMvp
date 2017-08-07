package pro.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteMessages;
import com.zjhz.teacher.NetworkRequests.request.ReceviceMessageParams;
import com.zjhz.teacher.NetworkRequests.request.UnreadMsgParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.ui.activity.MainActivity;
import com.zjhz.teacher.ui.activity.ReceiveMeetingNoticeListActivity;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.SwipeListView;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
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
import pro.adapter.MessageAdapterMsgType;


/**
 * Created by Tabjin on 2017/2/27.
 * 消息模块显示消息列表的fragment
 */

public class MessageListFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = ReceiveMeetingNoticeListActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_left_text)
    TextView cancle;
    @BindView(R.id.title_right_text)
    TextView titleDel;
    @BindView(R.id.right_icon)
    ImageView right_icon;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    SwipeListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_ll)
    LinearLayout refresh_ll;
    MessageAdapterMsgType adapter;
    private int page = 1, pageSize = 12;
    //群发信息，会议，危险警告的总数
    private int msgTotal, total;
    private List<Message> datas = new ArrayList<>();
    private int index = -1, isUnread = -1;
    //网络请求是否为删除
    private boolean isDelete;
    private DefaultLoadingLayout loadingLayout;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.title_refresh_delete_list, null, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //初始化view
    @Override
    protected void initViewsAndEvents() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        titleBackImg.setVisibility(View.GONE);
        titleDel.setText(R.string.ok);
        titleDel.setVisibility(View.GONE);
        cancle.setText(R.string.cancel);
        titleTv.setText(getResources().getText(R.string.message));
        right_icon.setImageResource(R.mipmap.work_delete);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new MessageAdapterMsgType(getActivity(), datas, refreshListview.getRightViewWidth());
        adapter.setOnItemRightClickListener(new MessageAdapterMsgType.IOnItemRightClickListener() {
            @Override
            public void onRightClick(View v, int position) {
                refreshListview.hiddenRight();
                NetworkRequest.request(new DeleteMessages(datas.get(position).getLinkId(), SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),
                        CommonUrl.DELETEMESSAGE,
                        "deleteMsg");
                index = position;
            }
        });
        refreshListview.setAdapter(adapter);
    }


    private int delsNum = 0;
    @OnClick({R.id.title_left_text, R.id.title_right_text, R.id.right_icon})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.title_left_text:   //取消删除
                adapter.closeCheckBox();
//                titleDel.setText(R.string.delete);
                right_icon.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.GONE);
                titleDel.setVisibility(View.GONE);
                break;
            case R.id.title_right_text:  //确定删除
                delsNum = adapter.getDelsList().size();
                new AlertDialog.Builder(context).setTitle("确定删除这" + delsNum + "条消息？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                right_icon.setVisibility(View.VISIBLE);
                                titleDel.setVisibility(View.GONE);
                                cancle.setVisibility(View.GONE);
                                NetworkRequest.request(new DeleteMessages(adapter.delsIds(), SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),
                                        CommonUrl.DELETEMESSAGE,
                                        "deleteMsgs");
                                isDelete = true;
                                adapter.closeCheckBox();
                            }
                        }).show();

                break;
            case R.id.right_icon:   //删除按钮设监听
                titleDel.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.GONE);
                cancle.setVisibility(View.VISIBLE);
                adapter.showCheckBox();
                break;
        }
    }
/*
    //多选删除操作
    private void MultiDelete(TextView v) {
        String str = v.getText().toString();
        if (str != null && !str.equals("")) {
            if (str.equals(getResources().getString(R.string.delete))) {
                adapter.showCheckBox();
                v.setText("确定删除");
                cancle.setVisibility(View.VISIBLE);
                cancle.setText(R.string.cancel);
            } else if (str.equals("确定删除")) {
                if (adapter.delsIds() != null) {
                    new AlertDialog.Builder(context).setTitle("确定删除这几条消息？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.closeCheckBox();
                                    titleDel.setText(R.string.delete);
                                    cancle.setVisibility(View.GONE);
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NetworkRequest.request(new DeleteMessages(adapter.delsIds(), SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),
                                            CommonUrl.DELETEMESSAGE,
                                            "deleteMsgs");
                                }
                            }).show();
                    v.setText(getResources().getString(R.string.delete));
                    cancle.setVisibility(View.GONE);
                } else {
                    ToastUtils.showShort("请至少选择一个。。。");
                }
            }
        }
    }*/

    @Override
    protected void initData(Bundle savedInstanceState) {
        getData();
        loadingLayout.onLoading();
    }

    public void getData() {
        NetworkRequest.request(new ReceviceMessageParams(page, pageSize,
                        SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey), "1,2,3,4,7", "0"),
                CommonUrl.ALLMESSAGES,
                "allMessages");
//        mLayout.onLoading();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    /**
     * 切换消息fragment是将已读的消息上传，展现是将本地消息已读缓存更新
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (adapter != null) {
            if (hidden) {
                {
                    adapter.closeCheckBox();
                    right_icon.setVisibility(View.VISIBLE);
                    cancle.setVisibility(View.GONE);
                    titleDel.setVisibility(View.GONE);
                }
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       /* if (!isVisibleToUser) {
            adapter.closeCheckBox();
//                titleDel.setText(R.string.delete);
            right_icon.setVisibility(View.VISIBLE);
            cancle.setVisibility(View.GONE);
            titleDel.setVisibility(View.GONE);
        }*/
        isDelete = false;
        NetworkRequest.request(new UnreadMsgParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey), "", "0"), CommonUrl.CHECKUNREADNUM, "unReadNum");

    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {

        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                stopRefreshListener();
                ToastUtils.showShort("请求错误");
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefreshListener();
                if (isDelete) {
                    ToastUtils.showShort("删除失败");
                }
                break;
            case "allMessages": //所有消息
                dialog.dismiss();
                total = getMessage(eventCenter);
                break;
            case "deleteMsg":  //单个消息删除
                ToastUtils.showShort("删除成功");
                page = 1;
                getData();
                break;
            case "deleteMsgs":  //多个消息删除
                dialog.dismiss();
                ToastUtils.showShort("成功删除" + delsNum + "条消息");
                page = 1;
                getData();
                break;
            case "setHasRead":
                dialog.dismiss();
                if (context instanceof MainActivity) {
                    ((MainActivity) context).hasReadMsg();
                }
                break;
        }


    }

    //将接收到的消息解析成Message并统一添加到datas（消息集合）,并返回消息的总数量
    private int getMessage(EventCenter eventCenter) {
        stopRefreshListener();
        JSONObject beansObj = (JSONObject) eventCenter.getData();
        List<Message> beans = GsonUtils.toArray(Message.class, beansObj);
        if (beans != null && beans.size() != 0) {
            if (page == 1) {
                datas.clear();
                datas.addAll(beans);
            } else {
                datas.addAll(beans);
            }
            loadingLayout.onDone();
            adapter.notifyDataSetChanged();
//            titleDel.setVisibility(View.VISIBLE);
            if(!adapter.isShowCheckBox()) {
                right_icon.setVisibility(View.VISIBLE);
            }
        } else {
            loadingLayout.onEmpty();
//            titleDel.setVisibility(View.GONE);
            if(!adapter.isShowCheckBox()){
                right_icon.setVisibility(View.GONE);
            }
        }
        try {
            return msgTotal = beansObj.getInt("total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void stopRefreshListener() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
        EventBus.getDefault().post(new EventCenter("updateDataNum", null));
    }

    @Override
    public void onLoad() {
        if (datas.size() < total) {
            page++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }
}
