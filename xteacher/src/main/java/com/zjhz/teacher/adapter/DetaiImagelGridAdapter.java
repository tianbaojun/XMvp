/*
 * 源文件名：DetaiImagelGridAdapter
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：食谱详情图片适配器
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

public class DetaiImagelGridAdapter extends BaseAdapter{

    private Context context;
    private List<ImageBean> images;

    public DetaiImagelGridAdapter(Context context, List<ImageBean> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        if (images.size() > 9){
            return 9;
        }
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_food_detail_grid,null,false);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.imageView = (ImageView) view.findViewById(R.id.adapter_food_detail_grid_image);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        GlideUtil.loadImage(images.get(i).getDocPath(),holder.imageView);
        return view;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
