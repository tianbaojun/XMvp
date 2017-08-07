package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.SchoolListBean;
import com.zjhz.teacher.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SelectSchoolAdapter extends BaseAdapter{
    private Context context;
    private List<SchoolListBean> beanList;

    public SelectSchoolAdapter(Context context, List<SchoolListBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @Override
    public int getCount() {
        return beanList.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_school,null);
            viewHolder.line_view1 = convertView.findViewById(R.id.line_view1);
            viewHolder.select_iv = (ImageView) convertView.findViewById(R.id.select_iv);
            viewHolder.school_name_tv = (TextView) convertView.findViewById(R.id.school_name_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.school_name_tv.setText(beanList.get(position).getSchoolName());
        if (position == beanList.size() - 1){
            viewHolder.line_view1.setVisibility(View.GONE);
        }else {
            viewHolder.line_view1.setVisibility(View.VISIBLE);
        }
        if (beanList.get(position).isSelect()){
            viewHolder.select_iv.setImageResource(R.mipmap.person_grade_selected);
        }else {
            viewHolder.select_iv.setImageResource(R.mipmap.school_selected_false);
        }
        return convertView;
    }
    class ViewHolder{
        View line_view1;
        ImageView select_iv;
        TextView school_name_tv;
    }
}
