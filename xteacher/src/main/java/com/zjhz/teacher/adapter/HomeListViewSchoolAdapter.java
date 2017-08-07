package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.HomeListData;
import com.zjhz.teacher.utils.DateUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-6
 * Time: 9:57
 * Description: 首页ListViewSchool适配器
 */
public class HomeListViewSchoolAdapter extends BaseAdapter {

    private List<HomeListData>  lists;
    private Context context;

    public HomeListViewSchoolAdapter(List<HomeListData> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public HomeListData getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_home_list_one,null);
            holder = new ViewAHolder();
            convertView.setTag(holder);
            holder.top = (TextView) convertView.findViewById(R.id.adapter_home_list_one_top_top);
            holder.center = (TextView) convertView.findViewById(R.id.adapter_home_list_one_top);
            holder.left = (TextView) convertView.findViewById(R.id.adapter_home_list_one_left);
            holder.right = (TextView) convertView.findViewById(R.id.adapter_home_list_one_right);
        }else{
            holder = (ViewAHolder) convertView.getTag();
        }
        holder.top.setText(homeListData.top);
        holder.center.setText(homeListData.center);
        holder.left.setText("发自"+homeListData.left);
        String timeF = DateUtil.getStandardDate(homeListData.right);
        holder.right.setText(timeF);
        return convertView;
    }

    class ViewAHolder {
        TextView top,center,left,right;
    }
}
