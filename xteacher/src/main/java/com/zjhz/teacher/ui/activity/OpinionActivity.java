package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.utils.ToastUtils;


import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;import butterknife.BindView;

/**
 * 意见反馈
 * Created by 向雪 on 2016/6/3.
 */
public class OpinionActivity extends BaseActivity {

    @BindView(R.id.bt_commit)
    Button bt_commit;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.title_tv)
    TextView title_tv;
    private final static String TAG = OpinionActivity.class.getSimpleName();

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
    }

    private void initView() {
        title_tv.setText("意见反馈");
        title_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.bt_commit)
    public void clickEvent(View view){
        if(ViewTools.avoidRepeatClick(view)){
            return;
        }
        switch (view.getId()){
            case R.id.bt_commit:
                ToastUtils.showShort("待接口。。。");
                break;
        }
    }

}
