package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarDeleteRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarModifyRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.DeleteEvent;
import com.zjhz.teacher.bean.Event;
import com.zjhz.teacher.ui.dialog.ScreenPopWindow;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 个人行事历详情
 */
public class PersonalCalendarDetailActivity extends BaseActivity implements ScreenPopWindow.OnDialogItemClickListener{

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text_image)
    ImageView right;
    @BindView(R.id.activity_personal_calendar_detail_text)
    TextView time;
    @BindView(R.id.activity_personal_calendar_detail_image)
    ImageView fly;
    @BindView(R.id.activity_personal_calendar_detail_content)
    EditText title;
    @BindView(R.id.activity_personal_calendar_detail_content_ed)
    EditText contentEd;
    private String position;
    ScreenPopWindow mScreenPopWindow = null;
    String i = "0";
    String id;
    String day;
    int image;
    boolean delete = false; // 判断是否删除
    private final static String TAG = PersonalCalendarDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_calendar_detail);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        mScreenPopWindow = new ScreenPopWindow(this);
        mScreenPopWindow.setOnDialogItemClickListener(this);
        initData();
    }

    private void initData() {
        titleTv.setText("个人行事历");
        right.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("personal_bundle");
        time.setText(bundle.getString("person_detail_time"));
        title.setText(bundle.getString("person_detail_context"));
        contentEd.setText(bundle.getString("person_detail_summary"));
        position = bundle.getString("person_detail_position");
        day = bundle.getString("person_detail_day");
        id = bundle.getString("person_detail_id");
        image = bundle.getInt("person_detail_image");
        if (image == R.mipmap.person_hight_grade) {
            i = "3";
        }else if(image == R.mipmap.person_center_grade){
            i = "2";
        }else if(image == R.mipmap.person_low_grade){
            i = "1";
        }else if(image == R.mipmap.person_none_grade){
            i = "0";
        }
        fly.setImageResource(image);
        LogUtil.e("个人行事历",SharedPreferencesUtils.getSharePrefString("personal_content"));
//        contentEd.setText(SharedPreferencesUtils.getSharePrefString("personal_content"));
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @OnClick({R.id.activity_personal_calendar_detail_image,R.id.title_back_img,R.id.right_text_image})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                if (contentEd.getText().toString().trim().length() <= 32 && title.getText().toString().trim().length() <= 32) {
                    if (delete) {
                    }else {
                        SharedPreferencesUtils.putSharePrefString("PersonalCalendarModify", position);
                        SharedPreferencesUtils.putSharePrefString("PersonalCalendarModify_title", title.getText().toString().trim());
                        LogUtil.e("修改的日期 = ", day + "修改的备注 = " + contentEd.getText().toString().trim());
                        PersonalCalendarModifyRequest mPersonalCalendarModifyRequest = new PersonalCalendarModifyRequest(day,"0",id, i, title.getText().toString().trim(), contentEd.getText().toString().trim());
                        LogUtil.e("修改个人行事历参数", GsonUtils.toJson(mPersonalCalendarModifyRequest));
                        NetworkRequest.request(mPersonalCalendarModifyRequest, CommonUrl.PERSONEVENTMODIFY, Config.PERSONEVENTMODIFY);
                    }
                    finish();
                }else{
                    ToastUtils.toast("备注或标题输入的字数长度不能大于32");
                }
                break;
            case R.id.right_text_image:
                EventBus.getDefault().post(new EventCenter<Object>("delete", new DeleteEvent(position,day)));
                PersonalCalendarDeleteRequest mPersonalCalendarDeleteRequest = new PersonalCalendarDeleteRequest(id);
                LogUtil.e("删除个人行事历参数",GsonUtils.toJson(mPersonalCalendarDeleteRequest) + "position = " + position);
                NetworkRequest.request(mPersonalCalendarDeleteRequest, CommonUrl.PERSONEVENTDELETE, Config.PERSONEVENTDELETE);
                delete = true;
                title.setText("");
                contentEd.setText("");
                finish();
                break;
            case R.id.activity_personal_calendar_detail_image:  // 底部弹窗
                mScreenPopWindow.upPopWwindow();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScreenPopWindow = null;
    }

    @Override
    public void onClickListenerHeight() {
        i = "3";
        fly.setImageResource(R.mipmap.person_hight_grade);
        EventBus.getDefault().post(new EventCenter<Object>("height",new Event(position,contentEd.getText().toString().trim())));
    }

    @Override
    public void onClickListenerCenter() {
        i = "2";
        fly.setImageResource(R.mipmap.person_center_grade);
        EventBus.getDefault().post(new EventCenter<Object>("center",new Event(position,contentEd.getText().toString().trim())));
    }

    @Override
    public void onClickListenerLow() {
        i = "1";
        fly.setImageResource(R.mipmap.person_low_grade);
        EventBus.getDefault().post(new EventCenter<Object>("low",new Event(position,contentEd.getText().toString().trim())));
    }

    @Override
    public void onClickListenerNone() {
        i = "0";
        fly.setImageResource(R.mipmap.person_none_grade);
        EventBus.getDefault().post(new EventCenter<Object>("none",new Event(position,contentEd.getText().toString().trim())));
    }

//    @Subscribe
//    public void onEventMainThread(EventCenter event) {
//        switch (event.getEventCode()){
//            case "error":
//                ToastUtils.showShort("请求错误");
//                dialog.dismiss();
//                break;
//            case "noSuccess":
//                dialog.dismiss();
//                ToastUtils.toast("修改失败");
//                break;
//            case Config.PERSONEVENTMODIFY:
//                ToastUtils.toast("修改成功");
//                break;
//        }
//    }
}
