package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.HomeGridView;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-6
 * Time: 9:57
 * Description: 首页GridView适配器
 */
public class HomeGridViewAdapter extends BaseAdapter {

    private List<HomeGridView>  lists;
    private Context context;

    public HomeGridViewAdapter(List<HomeGridView> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (lists.size() <= 12) {
            return lists.size();
        }else{
            return 12;
        }
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_grid,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.adapter_home_image);
        TextView text = (TextView) convertView.findViewById(R.id.adapter_home_text);
        GlideUtil.loadImage(lists.get(position).image,imageView);
        text.setText(lists.get(position).name);
        if (position == lists.size() - 1) {
            GlideUtil.homeImage("",imageView);
            text.setText(lists.get(position).name);
        }else{
            GlideUtil.loadImage(lists.get(position).image,imageView);
            text.setText(lists.get(position).name);
        }
        return convertView;
    }
}
