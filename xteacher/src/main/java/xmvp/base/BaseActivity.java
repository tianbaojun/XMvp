package xmvp.base;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.dialog.WaitDialog;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import pro.kit.ViewTools;

public abstract class BaseActivity extends XActivity implements View.OnClickListener {

    public WaitDialog dialog;
    protected TextView titleTv;

    @Override
    public void initData(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog = new WaitDialog(this);
        AppContext.getInstance().addActivity(getClass().getName(), this);
        //返回键的监听
        View view = findViewById(R.id.title_back_img);
        if (view != null) {
            view.setOnClickListener(this);
        }
        //标题
        titleTv = (TextView) findViewById(R.id.title_tv);
    }

    protected void setMyTitle(String title){
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    protected void setMyTitle(@StringRes int title){
        if (titleTv != null) {
            setMyTitle(getResources().getString(title));
        }
    }

    /**
     * eventbus对网络请求异常处理
     * @param event
     */
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.ERROR_JSON:
                dialog.dismiss();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewTools.hideSoftInput(this);
    }

    public void hideWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showWaitDialog(String msg) {
        if (dialog != null) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
