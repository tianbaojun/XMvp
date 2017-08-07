/*
 * 源文件名：ImageDetailActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：图片添加公共类   相册列表的界面
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */

package com.zjhz.teacher.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.adapter.FolderAdapter;
import com.zjhz.teacher.ui.view.selectmorepicutil.ImageUtils;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageListActivity extends Activity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.init_layout)
    LinearLayout init_layout;
    private final static String TAG = ImageListActivity.class.getSimpleName();
    private ListView local_album_list;
    private ImageView progress;

    private LocalImageHelper helper;
    private FolderAdapter adapter;
    Map<String, List<LocalImageHelper.LocalFile>> folders;
    private PhotoListBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelist);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        bean = (PhotoListBean) getIntent().getSerializableExtra("bean");
        initView();
        getImagefolders();
    }

    private void initView() {
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTv.setText("选择图片");
        local_album_list = (ListView) findViewById(R.id.local_album_list);
        progress = (ImageView) findViewById(R.id.progress_bar);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        progress.startAnimation(animation);
        local_album_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ImageListActivity.this, ImageDetailActivity.class);
                intent.putExtra("bean",bean);
                intent.putExtra(ImageUtils.LOCAL_FOLDER_NAME, String.valueOf(adapter.getItem(position)));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void getImagefolders() {
        helper = LocalImageHelper.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启线程初始化本地图片列表，该方法是synchronized的，因此当AppContent在初始化时，此处阻塞
                helper.initImage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        folders = helper.getFolderMap();
                        //初始化完毕后，显示文件夹列表
                        initAdapter();
                        progress.clearAnimation();
                        init_layout.setVisibility(View.GONE);
                        ((View) progress.getParent()).setVisibility(View.GONE);
                        local_album_list.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    public void initAdapter() {
        adapter = new FolderAdapter(this, helper, folders);
        local_album_list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setResult(RESULT_OK);
                finish();
            }
        }else if (resultCode == 100){
            bean = (PhotoListBean) data.getSerializableExtra("BEAN");
        }
    }
}
