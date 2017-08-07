package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.HomeListData;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-6
 * Time: 9:57
 * Description: 首页ListView适配器
 */
public class HomeListViewAdapter extends BaseAdapter {

    private List<HomeListData>  lists;
    private Context context;

    public HomeListViewAdapter(List<HomeListData> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
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
        HomeListData homeListData = lists.get(position);
        ViewAHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_list,null);
            holder = new ViewAHolder();
            convertView.setTag(holder);
            holder.left = (TextView) convertView.findViewById(R.id.adapter_home_list_text_top);
            holder.center = (TextView) convertView.findViewById(R.id.adapter_home_list_text_left);
            holder.right = (TextView) convertView.findViewById(R.id.adapter_home_list_text_right);
        }else{
            holder = (ViewAHolder) convertView.getTag();
        }
        holder.center.setText(homeListData.top);
        holder.left.setText(homeListData.left);
        holder.right.setText(homeListData.right);
        return convertView;
    }

    class ViewAHolder {
        TextView center,left,right;
    }
}
