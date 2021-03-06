package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.TeachSchduleParams;
import com.zjhz.teacher.NetworkRequests.response.TeachSchduleBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.adapter.MyScheduleAdapter;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表
 * Modify :fei.wang 2016.7.20
 */
public class MyScheduleActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon0)
    ImageView right_icon0;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.activity_my_schedule_grid)
    GridView gridView;
    private MyScheduleAdapter adapter;
    private final static String TAG = MyScheduleActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule_pro);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        titleTv.setText("我的课表");
        horizontal_layout();
        right_icon0.setVisibility(View.VISIBLE);
        right_icon0.setImageResource(R.mipmap.schedule_find);
        adapter = new MyScheduleAdapter(this);
        gridView.setAdapter(adapter);
        dialog.show();
        NetworkRequest.request(new TeachSchduleParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey),1,100), CommonUrl.MYSCHEDULE, Config.MYSCHEDULE);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.right_icon0, R.id.title_back_img})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_icon0:
                startActivityForResult(new Intent(this,MyScheduleQueryActivity.class),1);
                break;
        }
    }

    //gridview横向布局方法
    public void horizontal_layout(){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Point mPoint = new Point();
        wm.getDefaultDisplay().getSize(mPoint);
        //条目宽度为屏幕宽度的5.7/5/3
        float itemWidth = mPoint.x/5.7f*5/3;
        int allWidth = (int) (itemWidth * 7 );
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = allWidth;
        gridView.setLayoutParams(params);// 设置GirdView布局参数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                String teacherId = data.getStringExtra("teacherId");
                if (!SharePreCache.isEmpty(teacherId)){
                    String teacherName = data.getStringExtra("teacherName");
                    titleTv.setText(teacherName+"的课表");
                    dialog.show();
                    NetworkRequest.request(new TeachSchduleParams(teacherId,1,100), CommonUrl.otherSchdule, Config.MYSCHEDULE);
                }
            }
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
            case Config.MYSCHEDULE:
                dialog.dismiss();
                JSONObject jsonObject = (JSONObject) event.getData();
                List<TeachSchduleBean> beans = GsonUtils.toArray(TeachSchduleBean.class, jsonObject);
                if (beans != null && beans.size() > 0){
                    adapter.setNetDatas(beans);
                    adapter.notifyDataSetChanged();
                }else {
//                    try {
                        //  {"msg":"查询到17条记录!","code":0,"total":0,"data":[]}
//                        ToastUtils.showShort(jsonObject.getString("msg"));
                        ToastUtils.showShort("没有课表信息");
                        adapter.setNetDatas(null);
                        adapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
        }
    }
}
