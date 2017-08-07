package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CheckRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VersionListActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.version_list)
    public ListView versionListView;
    @BindView(R.id.title_back_img)
    TextView back_icon;
    private List<UpdateInfo> infoList = new ArrayList<>();
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_list);
        ButterKnife.bind(this);
        titleTv.setText("系统通知");
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new CommonAdapter<UpdateInfo>(this, R.layout.version_list_item, infoList) {
            @Override
            protected void convert(ViewHolder viewHolder, UpdateInfo item, int position) {
                ((TextView) viewHolder.getView(R.id.version_code)).setText(item.getVersionName());
            }
        };

        versionListView.setAdapter(adapter);

        versionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", infoList.get(position));
                startActivity(VersionInfoActivity.class, bundle);
            }
        });

        NetworkRequest.request(new CheckRequest("1", "TEACHER_APP"), CommonUrl.VERSION_LIST, "version_List");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter ev) {
        JSONObject jsonObject = (JSONObject) ev.getData();
        switch (ev.getEventCode()) {
            case "version_List":
                try {

                    JSONArray data = jsonObject.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        UpdateInfo info = new UpdateInfo();
                        info.setVersionName(data.getJSONObject(i).getString("appVersion"));
                        info.setChangeLog(data.getJSONObject(i).getString("description"));
                        infoList.add(info);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                }

                break;
        }
    }
}
