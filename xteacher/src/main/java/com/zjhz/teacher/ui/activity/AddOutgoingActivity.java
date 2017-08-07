package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.OutgoingParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 添加外出公告
 * Created by Administrator on 2016/6/27.
 * modify : fei.wang 2016/7/19
 */
public class AddOutgoingActivity extends BaseActivity {
    private final static String TAG = AddOutgoingActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.size_tv)
    TextView size_tv;
    @BindView(R.id.outgoing_reason_et)
    EditText outgoingReasonEt;
    private int maxSize = 20;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing);
        AppContext.getInstance().addActivity(TAG, this);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        titleTv.setText("外出公告");
        titleBackImg.setText("取消");
        titleBackImg.setCompoundDrawables(null, null, null, null);
        rightText.setText("发布");
        rightText.setVisibility(View.VISIBLE);
        outgoingReasonEt.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        private String temp;
        private int selectionStart;
        private int selectionEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s.toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {
            int number = maxSize - s.toString().length();
            selectionStart = outgoingReasonEt.getSelectionStart();
            selectionEnd = outgoingReasonEt.getSelectionEnd();
            size_tv.setText(number + "/" + maxSize);
            if (temp.length() > maxSize) {
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionEnd;
                outgoingReasonEt.setText(number + "/" + maxSize);
                outgoingReasonEt.setSelection(tempSelection);//设置光标在最后
            }
        }
    };

    @OnClick({R.id.title_back_img, R.id.right_text})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
//                SharePreCache.hintInput(this);
                String content = outgoingReasonEt.getText().toString().trim();
                if (!SharePreCache.isEmpty(content)) {
                    dialog.show();
                    NetworkRequest.request(new OutgoingParams(content), CommonUrl.outgoingAdd, Config.ADDOUTGOING);
                } else {
                    ToastUtils.showShort("事由不能为空字符串,请重新输入");
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("发布失败");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.ADDOUTGOING:
                dialog.dismiss();
                ToastUtils.showShort("发布成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

}
