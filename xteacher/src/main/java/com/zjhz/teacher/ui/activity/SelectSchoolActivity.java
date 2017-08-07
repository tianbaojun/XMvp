package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjhz.teacher.NetworkRequests.response.SchoolListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.adapter.SelectSchoolAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择学校
 * Created by Administrator on 2016/8/9.
 */
public class SelectSchoolActivity extends BaseActivity {
    @BindView(R.id.school_name_lv)
    ListView schoolNameLv;
    private SelectSchoolAdapter adapter;
    private List<SchoolListBean> beens = new ArrayList<>();
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        SchoolListBean bean = (SchoolListBean) intent.getSerializableExtra("bean");
        beens = bean.getBeen();
        adapter = new SelectSchoolAdapter(this,beens);
        schoolNameLv.setAdapter(adapter);
        schoolNameLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                beens.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
                Intent intent1 = new Intent();
                intent1.putExtra("schoolId",beens.get(position).getSchoolId());
                setResult(RESULT_OK,intent1);
                finish();
            }
        });
    }
}
