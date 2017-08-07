package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布图片BaseAdapter
 * Created by xiangxue on 2016/6/13.
 */
public class EditImageAdapter extends BaseAdapter{
    private Context context;
    private int totalCount;
    private List<LocalImageHelper.LocalFile> picLists;
    private List<LocalImageHelper.LocalFile> needUpload = new ArrayList<>();

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public List<LocalImageHelper.LocalFile> needUpDatas(){
        needUpload.clear();
        if (picLists != null && picLists.size() > 0){
            for (int i = 0 ; i < picLists.size() ; i ++){
                if (!picLists.get(i).getThumbnailUri().startsWith("http")){
                    needUpload.add(picLists.get(i));
                }
            }
        }
        return needUpload;
    }
    public List<LocalImageHelper.LocalFile> getPicLists() {
        return picLists;
    }
    public void setPicLists(List<LocalImageHelper.LocalFile> picLists) {
        this.picLists = picLists;
    }
    public EditImageAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        if (picLists != null && picLists.size() < totalCount){
            return picLists.size() + 1;
        }else if (picLists != null && picLists.size() >= totalCount){
            return totalCount;
        }
        return 1;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_foodimg,null);
            viewHolder.filter_img = (ImageView) convertView.findViewById(R.id.filter_img);
//            viewHolder.img_ll = (LinearLayout) convertView.findViewById(R.id.img_ll);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (picLists != null){
            if (position == picLists.size()){
                GlideUtil.loadImageNull("",viewHolder.filter_img);
            }else if (position < picLists.size()){
                GlideUtil.loadImage(picLists.get(position).getThumbnailUri(),viewHolder.filter_img);
            }
        }
        return convertView;
    }
    class ViewHolder{
        ImageView filter_img;
//        LinearLayout img_ll;
    }
}
