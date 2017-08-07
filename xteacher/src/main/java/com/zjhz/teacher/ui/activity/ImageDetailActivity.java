/*
 * 源文件名：ImageDetailActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：图片添加公共类   图片选择列表
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */

package com.zjhz.teacher.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.adapter.MyAdapter;
import com.zjhz.teacher.ui.view.selectmorepicutil.ImageUtils;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

public class ImageDetailActivity extends Activity{
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    private String fileImageName;
    private LocalImageHelper helper;
    private List<LocalImageHelper.LocalFile> list;
    private GridView gridview;
    private MyAdapter adapter;
    private int currentSize = 0;
    private PhotoListBean bean;
    private final static String TAG = ImageDetailActivity.class.getSimpleName();
    //选择的图片数据了
    private int selectPicNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_album_detail);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        fileImageName = getIntent().getStringExtra(ImageUtils.LOCAL_FOLDER_NAME);
        bean = (PhotoListBean) getIntent().getSerializableExtra("bean");
        initView();
    }

    private void initView() {
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setDatas(adapter.getCheckedItems());
                Intent intent = new Intent();
                intent.putExtra("BEAN",bean);
                setResult(100,intent);
                finish();
            }
        });
        titleTv.setText("选择图片");
        rightText.setVisibility(View.VISIBLE);
        gridview = (GridView) findViewById(R.id.gridview);
        helper = LocalImageHelper.getInstance();
        list = helper.getFolder(fileImageName);
        if (bean != null){
            if (bean.getDatas() != null){
                currentSize = bean.getDatas().size();
                rightText.setText("完成("+currentSize + "/"+bean.getTotalCount()+")");
            }else {
                rightText.setText("完成(0/"+bean.getTotalCount()+")");
            }
        }
        adapter = new MyAdapter(ImageDetailActivity.this, list, helper,bean);
        gridview.setAdapter(adapter);



//        preview.setOnClickListener(new PreviewListener());
    }

    public void updateCurrentSize(int count) {
        selectPicNum=count;
        if (count <= 0) {
            rightText.setText("完成(0/"+bean.getTotalCount()+")");
        } else {
            rightText.setText("完成("+ count + "/"+bean.getTotalCount()+")");
        }
    }


    @OnClick({R.id.right_text})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.right_text:
                helper.setCheckedItems(adapter.getCheckedItems());
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
