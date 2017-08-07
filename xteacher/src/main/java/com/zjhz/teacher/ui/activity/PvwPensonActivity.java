package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 个人行事例值日值周  预览
 */
public class PvwPensonActivity extends BaseActivity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_pvw_person_text)
    TextView activityPvwText;
    private final static String TAG = PvwPensonActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvw_penson);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        titleTv.setText("个人行事历");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getBundleExtra("pvwPensonActivity_bundle");
        ArrayList<String> leader = bundle.getStringArrayList("pvwPensonActivity_leader");
        ArrayList<String> teacher = bundle.getStringArrayList("pvwPensonActivity_teacher");
        String ss = null;
        if (teacher != null && teacher.size() > 0) {
            ss = teacher.get(0);
        }
        activityPvwText.setText("值日领导："+ listToString(leader)+"\r\n值日老师："+ listToString(teacher));
    }

    @Override
    protected boolean isBindEventBusHere() {return false;}

    public String listToString(ArrayList<String> stringList){
        if (stringList == null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
