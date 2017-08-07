package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 详情资料页面
 */
public class DetailDataActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.user_head_detail)
    CircleImageView headImage;
    @BindView(R.id.activity_detail_data_name)
    TextView name;
    @BindView(R.id.activity_detail_data_type)
    TextView type;

    @BindView(R.id.activity_detail_data_student_name)
    TextView studentName;
    @BindView(R.id.activity_detail_data_student_name_tv)
    TextView studentNameTv;

    @BindView(R.id.activity_detail_data_student_number_tv)
    TextView studentNumberTv;
    @BindView(R.id.name_rl_three)
    RelativeLayout nameRlFive;

    @BindView(R.id.activity_detail_data_student_mtv)
    TextView studentMtv;
    @BindView(R.id.name_rl_four)
    RelativeLayout nameRlFour;

    @BindView(R.id.activity_detail_data_student_phone_tv)
    TextView studentPhoneTv;
    private String userId;
    private String userName;
    private String userHead;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);
        ButterKnife.bind(this);
        titleTv.setText("详细资料");
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userHead = intent.getStringExtra("userHead");
        phone_number = intent.getStringExtra("phone_number");
        if (TextUtils.isEmpty(intent.getStringExtra("teacher_number"))) {
            name.setText(intent.getStringExtra("userName"));
            GlideUtil.loadImage(intent.getStringExtra("userHead"),headImage);
            studentPhoneTv.setText(intent.getStringExtra("phone_number"));
            studentNameTv.setText(intent.getStringExtra("student_name"));
            studentNumberTv.setText(intent.getStringExtra("student_number"));
            studentMtv.setText(intent.getStringExtra("clan"));
            type.setText("家长");
        }else{
            type.setText("老师");
            nameRlFive.setVisibility(View.GONE);
            nameRlFour.setVisibility(View.GONE);
            studentName.setText("教师工号");
            studentNameTv.setText(intent.getStringExtra("teacher_number"));  // 教工号
        }
    }

    @Override
    protected boolean isBindEventBusHere() {return false;}

    @OnClick({R.id.activity_detail_data_student_phone_btn, R.id.activity_detail_data_student_notify_btn, R.id.activity_detail_data_student_message_btn,R.id.title_back_img})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.activity_detail_data_student_phone_btn:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone_number));
                startActivity(intent);
                break;
            case R.id.activity_detail_data_student_notify_btn:
                Uri smsToUri = Uri.parse("smsto:"+phone_number);
                Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                startActivity(mIntent);
                break;
            case R.id.activity_detail_data_student_message_btn:
                Intent  intent1 = new Intent(this,ChatActivity.class);
                LogUtil.e("环信userId = " + userId);
                intent1.putExtra("userId",userId);
                intent1.putExtra("userName",userName);
                intent1.putExtra("userHead",userHead);
                startActivity(intent1);
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }
}
