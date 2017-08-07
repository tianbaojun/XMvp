package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.PayOrderResponseBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayCompleteActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView back;
    @BindView(R.id.pay_money)
    TextView payMoney;
    @BindView(R.id.pay_des)
    TextView payDes;
    @BindView(R.id.pay_time)
    TextView payTime;

    PayOrderResponseBean orderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);
        ButterKnife.bind(this);

        if(getIntent() != null && getIntent().getExtras()!= null) {
            orderBean = (PayOrderResponseBean) getIntent().getExtras().getSerializable("result");
            if(orderBean != null){
                payMoney.setText(orderBean.getAmount());
                payDes.setText(orderBean.getBody());
                payTime.setText(orderBean.getCreateTime());
            }
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}
