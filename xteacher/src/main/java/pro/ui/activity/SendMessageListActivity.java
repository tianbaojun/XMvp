package pro.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteToAllMsg;
import com.zjhz.teacher.NetworkRequests.request.SendMessageListParams;
import com.zjhz.teacher.NetworkRequests.response.MassgeBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.AddMeetingOrMessageNoticeActivity;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.SwipeListView;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.adapter.MessageAdapterMsgType;
import pro.adapter.SendMessageListAdapter;

import static com.zjhz.teacher.R.id.right_icon0;

/**
 * 群发信息
 * Created by Administrator on 2016/7/12.
 *
 *----------------
 * Changed by Tabjin on 2017/4/7
 * Des:给群发消息添加单个删除
 */
public class SendMessageListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private final static String TAG = SendMessageListActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;

    @BindView(right_icon0)
    ImageView add;
    @BindView(R.id.right_icon)
    ImageView delete;
    @BindView(R.id.refresh_listview)
    SwipeListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_ll)
    LinearLayout refresh_ll;
    SendMessageListAdapter adapter;
    private DefaultLoadingLayout loadingLayout;
    private int pageNum = 1, pageSize = 12, total = 0;
    private int index = -1;
    private HintPopwindow hintPopwindow;
    private boolean isDelete;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_delete_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initTitle();
        initView();
//        dialog.setMessage("正在获取消息列表...");
//        dialog.show();
        getData();
    }

    private void initTitle() {
        titleTv.setText("群发信息");
        add.setVisibility(View.VISIBLE);
        add.setImageResource(R.mipmap.add_icon);
        delete.setVisibility(View.VISIBLE);
        delete.setImageResource(R.mipmap.work_delete);
        right_text.setText(R.string.delete);
    }

    private void initView() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

        adapter = new SendMessageListAdapter(this,refreshListview.getRightViewWidth());
        adapter.setOnItemRightClickListener(new MessageAdapterMsgType.IOnItemRightClickListener() {
            @Override
            public void onRightClick(View v, int position) {
                refreshListview.hiddenRight();
                // TODO: 2017/4/7 删除消息
                NetworkRequest.request(new DeleteToAllMsg(adapter.getBeans().get(position).getMsgId()),
                        CommonUrl.DELETETOALLMSG,
                        Config.DELETETOALLMSG);
                index = position;

            }
        });
        refreshListview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                pageNum = 1;
                getData();
            }
        }
    }

    private void getData() {
        NetworkRequest.request(new SendMessageListParams(pageNum, pageSize, "0"), CommonUrl.MessageList, Config.MESSAGELIST);
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                stopRefresh();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefresh();
                if (isDelete) {
                    delete.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    right_text.setVisibility(View.GONE);
                    adapter.setShowCheckBox(false);
                    ToastUtils.showShort("删除失败");
                    isDelete = false;
                }
                break;
            case Config.MESSAGELIST:
                dialog.dismiss();
                stopRefresh();
                JSONObject jsonObject = (JSONObject) eventCenter.getData();
                List<MassgeBean> beans = GsonUtils.toArray(MassgeBean.class, jsonObject);
                total = jsonObject.optInt("total");
                if (beans != null && beans.size() > 0) {
                    if (pageNum == 1) {
                        adapter.setBeans(beans);
                    } else {
                        adapter.addBeans(beans);
                    }
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                    if(!isDelete) {
                        delete.setVisibility(View.VISIBLE);
                    }
                } else {
                    loadingLayout.onEmpty();
                    delete.setVisibility(View.GONE);
                }
                break;
            case Config.DELETETOALLMSG:
                dialog.dismiss();
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                isDelete = false;
                adapter.setShowCheckBox(false);
                getData();
                ToastUtils.showShort("删除成功");

                break;
        }
    }

    @OnClick({R.id.right_icon, R.id.right_icon0,R.id.right_text,R.id.title_back_img})
    public void onclick(View view){
        switch (view.getId()){
            case R.id.right_icon0:   //新增
                Intent in = new Intent(SendMessageListActivity.this, AddMeetingOrMessageNoticeActivity.class);  //群发消息
                in.putExtra("from", 1);
                startActivityForResult(in, 1);
                break;
            case R.id.right_icon:  //多选删除
                isDelete = true;
                delete.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                right_text.setVisibility(View.VISIBLE);
                if (adapter != null) {
                    adapter.setShowCheckBox(true);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.title_back_img:   //回退按钮
                if(adapter.isShowCheckBox()){
                    adapter.setShowCheckBox(false);
                    delete.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    right_text.setVisibility(View.GONE);
                    isDelete = false;
                }else {
                    finish();
                }
                break;
            case R.id.right_text:
                MultiDelete();
                break;
        }
    }
    /**
     * 多选删除的逻辑
     */
    private void MultiDelete() {
        if(adapter.ids.size()>0){
            new AlertDialog.Builder(SendMessageListActivity.this).setTitle("确定删除这"+adapter.ids.size()+"条消息？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.setShowCheckBox(false);
                            right_text.setVisibility(View.GONE);
                            delete.setVisibility(View.VISIBLE);
                            add.setVisibility(View.VISIBLE);
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showWaitDialog("删除中...");
                            Map<String,String> map = new HashMap<>();
                            StringBuilder sb = new StringBuilder();
                            for(String str:adapter.ids){
                                sb.append(str+",");
                            }
                            map.put("msgIds",sb.substring(0,sb.length()-1));
                            NetworkRequest.request(map, CommonUrl.SETREMOVEALL, Config.DELETETOALLMSG);
                        }
                    }).show();
        }else{
            ToastUtils.showShort("没有选择...");
        }
    }

    private void stopRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        getData();
    }

    @Override
    public void onLoad() {
        if (adapter.getBeans() != null && adapter.getBeans().size() < total) {
            pageNum++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }
}
