/*
 * 源文件名：PicturesPreviewer
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：提供图片预览/图片操作 返回选中图片等功能
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zjhz.teacher.ui.view.images.activity.SelectImageActivity;
import com.zjhz.teacher.ui.view.images.adapter.SelectImageAdapter;
import com.zjhz.teacher.ui.view.popuwindow.SelectPicVideoPopupWindow;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.media.VideoPreviewActivity;
import com.zjhz.teacher.utils.media.VideoRecorderActivity;

import java.io.File;
import java.util.List;


public class PicturesPreviewer extends RecyclerView implements SelectImageAdapter.Callback, SelectImageActivity.Callback {

    public final static int PIC_ONLY = 1;
    public final static int PIC_AND_VIDEO = 2;

    public final static int SELECT_PIC = 30;
    public final static int SELECT_VIDEO = 40;

    private int type = PIC_ONLY;

    private int selectType = 0;
    private String videoPath;

    private Context context;
    private SelectImageAdapter mImageAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private SelectImageAdapter.RequestManagerWithVideo mCurImageLoader;
    public  int maxCount=9;//最多张数

    public PicturesPreviewer(Context context) {
        super(context);
        init(context);
    }

    public PicturesPreviewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PicturesPreviewer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mImageAdapter = new SelectImageAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        this.setLayoutManager(layoutManager);
        this.setAdapter(mImageAdapter);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);

        ItemTouchHelper.Callback callback = new PicturesPreviewerItemTouchCallback(mImageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    public void set(String[] paths) {
        mImageAdapter.clear();
        for (String path : paths) {
            if(path != null)
                mImageAdapter.add(path);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    public void setHasLoadPicture(String path){
        mImageAdapter.clear();
        mImageAdapter.addLoad(path);
        mImageAdapter.notifyDataSetChanged();
    }

    public void setHasLoadPicture(String[] paths){
        mImageAdapter.clear();
        for (String path : paths) {
            if(path != null)
                mImageAdapter.addLoad(path);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    public void setHasLoadPicture(List<String> paths){
        mImageAdapter.clear();
        for (String path : paths) {
            if(path != null)
                mImageAdapter.addLoad(path);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreClick() {
        if(type == PIC_ONLY) {
            int count = maxCount - ((mImageAdapter.getPaths() == null ? 0 : mImageAdapter.getPaths().length) - (
                    mImageAdapter.getNotLoadPaths() == null ? 0 : mImageAdapter.getNotLoadPaths().length));
            SelectImageActivity.showImage(getContext(), count, true, mImageAdapter.getNotLoadPaths(), this);
        }else if(type == PIC_AND_VIDEO){
            BaseUtil.hideSoftKeyBoard(context,this);
            new SelectPicVideoPopupWindow(context, this, new SelectPicVideoPopupWindow.PicOrVideo() {
                @Override
                public void pic() {
                    selectType = SELECT_PIC;
                    mImageAdapter.setMAX_SIZE(maxCount);
                    int count = maxCount - ((mImageAdapter.getPaths() == null ? 0 : mImageAdapter.getPaths().length) - (
                            mImageAdapter.getNotLoadPaths() == null ? 0 : mImageAdapter.getNotLoadPaths().length));
                    SelectImageActivity.showImage(getContext(), count, true, mImageAdapter.getNotLoadPaths(), PicturesPreviewer.this);
                }

                @Override
                public void video() {
                    selectType = SELECT_VIDEO;
                    mImageAdapter.setMAX_SIZE(1);
                    Intent intent = new Intent();
                    intent.setClass(context, VideoRecorderActivity.class);
                    ((Activity)context).startActivityForResult(intent, 0);
                }
            });
        }
    }

    @Override
    public SelectImageAdapter.RequestManagerWithVideo getImgLoader() {
        if (mCurImageLoader == null) {
            mCurImageLoader = mImageAdapter.new RequestManagerWithVideo();
            mCurImageLoader.loader = Glide.with(getContext());
        }
        if(selectType == SELECT_PIC){
            mCurImageLoader.isVideo = false;
        }else if(selectType == SELECT_VIDEO){
            mCurImageLoader.isVideo = true;
        }

        return mCurImageLoader;
    }

    @Override
    public void onStartDrag(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void clickItem(int position) {
        if(selectType == SELECT_VIDEO){
            Intent intent = new Intent();
            intent.setClass(context, VideoPreviewActivity.class);
            intent.putExtra("video_path", videoPath);
            ((Activity)context).startActivity(intent);
        }
    }

    @Override
    public void onDelete(int position) {
        if(selectType == SELECT_VIDEO && videoPath != null){
            File file = new File(videoPath);
            file.delete();
        }
    }

    /**
     * 获取图片的路径
     * @return String[] 如果没有则为null
     */
    public String[] getPaths() {
        return mImageAdapter.getPaths();
    }

    @Override
    public void doSelectDone(String[] images) {
        set(images);
    }

    public void setType(int type){
        this.type = type;
    }

    public int getSelectType(){
        return selectType;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
