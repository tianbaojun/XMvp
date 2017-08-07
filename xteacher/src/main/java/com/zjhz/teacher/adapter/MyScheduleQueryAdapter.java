package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师适配器
 */
public class MyScheduleQueryAdapter extends BaseAdapter {
    private Context context;
    private List<String> lists;

    public MyScheduleQueryAdapter(Context context, List<String> lists) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_my_schedule_query_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.adapter_my_schedule_query_item_text);
        tv.setText(lists.get(position));
        return convertView;
    }
}
