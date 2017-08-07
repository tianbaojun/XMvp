package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 校历管理适配器
 */
public class SchoolCalendarManagerAdapter extends BaseAdapter{
    private Context context;
    private List<String> lists;

    public SchoolCalendarManagerAdapter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_school_calendar_manager,null,false);
            holder.text = (TextView) view.findViewById(R.id.adapter_school_calendar_manager_text);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(lists.get(i));
        return view;
    }

    class ViewHolder{
        TextView text;
    }
}
