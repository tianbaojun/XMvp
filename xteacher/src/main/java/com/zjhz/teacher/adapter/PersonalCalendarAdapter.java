package com.zjhz.teacher.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.PersonalCalendar;
import com.zjhz.teacher.utils.BaseUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 个人行事历适配器
 */
public class PersonalCalendarAdapter extends BaseAdapter{

    private Context context;
    private List<PersonalCalendar> lists;

    public PersonalCalendarAdapter(Context context, List<PersonalCalendar> lists) {
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_personal_calendar_item,null,false);
            holder.image = (ImageView) view.findViewById(R.id.adapter_personal_calendar_item_image);
            holder.adapter_personal_calendar_item_image_one = (ImageView) view.findViewById(R.id.adapter_personal_calendar_item_image_one);
            holder.content = (TextView) view.findViewById(R.id.adapter_personal_calendar_item_content);
            holder.time = (TextView) view.findViewById(R.id.adapter_personal_calendar_item_time);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if(!BaseUtil.isEmpty(lists)){
            holder.image.setImageResource(lists.get(i).image);
            holder.content.setText(lists.get(i).text);
            holder.time.setText(lists.get(i).time);
            if (!TextUtils.isEmpty(lists.get(i).summary)) {
                holder.adapter_personal_calendar_item_image_one.setVisibility(View.VISIBLE);
            }else{
                holder.adapter_personal_calendar_item_image_one.setVisibility(View.GONE);
            }
        }
        return view;
    }

    class ViewHolder{
        ImageView image,adapter_personal_calendar_item_image_one;
        TextView content,time;
    }
}
