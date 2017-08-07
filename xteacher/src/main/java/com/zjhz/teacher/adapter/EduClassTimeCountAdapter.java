package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.ClassMoral;
import com.zjhz.teacher.bean.IndividualMoral;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class EduClassTimeCountAdapter<T> extends BaseAdapter {

    private List<T>  list;
    private Context context;

    public EduClassTimeCountAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list != null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null)
         return list.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        T t = (T) getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_edu_count_second,null);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.item_name);
            vh.score = (TextView) convertView.findViewById(R.id.item_score);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        if(t instanceof IndividualMoral){
            vh.name.setText(((IndividualMoral) t).getMoralName());
            vh.score.setText(((IndividualMoral) t).getScore());
        }
        if(t instanceof ClassMoral){
            vh.name.setText(((ClassMoral) t).getSchemeName());
            vh.score.setText(((ClassMoral) t).getTotalScore());
        }
        return convertView;
    }

    class ViewHolder{
        TextView name,score;
    }

}
