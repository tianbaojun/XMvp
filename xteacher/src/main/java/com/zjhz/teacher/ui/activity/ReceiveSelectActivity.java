package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.adapter.SelectReceviceAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选中会议接收通知的对象
 * Created by xiangxue on 2016/6/2.
 */
public class ReceiveSelectActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.title_back_img)
    TextView title_back_img;

    @BindView(R.id.name_lv)
    ListView name_lv;
    private final static String TAG = ReceiveSelectActivity.class.getSimpleName();
    private SelectReceviceAdapter adapter;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectrecevice);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
//        initData();
    }

    private void initData() {
    }

    public void initView() {
        title_tv.setText("会议通知");
        title_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new SelectReceviceAdapter(ReceiveSelectActivity.this);
        name_lv.setAdapter(adapter);
    }
}
