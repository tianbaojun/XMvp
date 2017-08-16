package xmvp.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.dialog.WaitDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: Activity的基类 对findviewbyid 进行泛型封装
 * 对startActity进行带bundle和不带bundle封装，以及finish和不finish
 * 添加进度条属性，提供相应展示和隐藏方法
 */
public abstract class BaseActivity extends XActivity implements View.OnClickListener {

    public WaitDialog dialog;
    protected TextView titleTv;
    protected Unbinder unbinder;

    @Override
    public void initData(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog = new WaitDialog(this);
        AppContext.getInstance().addActivity(getClass().getName(), this);
    }

    @Subscribe
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
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        View view = findViewById(R.id.title_back_img);
        if (view != null) {
            view.setOnClickListener(this);
        }
        titleTv = (TextView) findViewById(R.id.title_tv);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        View back = findViewById(R.id.title_back_img);
        if (back != null) {
            view.setOnClickListener(this);
        }
        titleTv = (TextView) findViewById(R.id.title_tv);
    }

    protected abstract boolean isBindEventBusHere();
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
     * startActivity
     *
     * @param clazz
     */
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param
     */
    protected void startActivity(Class<?> clazz, String key, String value) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz, String key, int value) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra(key, value);
        startActivity(intent);
//        finish();
    }

    protected <T extends View> T genericFindViewById(int id) {
        return (T) findViewById(id);
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     */
    protected void startActivityThenKill(Class<?> clazz, String key, int value) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtra(key, value);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void startActivityThenKill(Class<?> clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        startActivity(intent);
        finish();
    }

    /**
     * show toast
     *
     * @param
     */
/*    public void ToastUtils.showShort(String msg){
        Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show();
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
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

    public void setSystemBarTintDrawable(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(false);
            mTintManager.setNavigationBarTintEnabled(false);
            mTintManager.setTintColor(color);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
