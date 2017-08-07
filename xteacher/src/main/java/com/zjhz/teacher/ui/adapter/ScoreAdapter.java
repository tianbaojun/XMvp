package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.StuScoreSchemaClassBean;
import com.zjhz.teacher.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class ScoreAdapter extends BaseAdapter{
    private List<StuScoreSchemaClassBean> stringList ;
    private Context context;

    public ScoreAdapter(List<StuScoreSchemaClassBean> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_score,null);
            viewHolder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            viewHolder.subject_tv = (TextView) convertView.findViewById(R.id.subject_tv);
            viewHolder.class_tv = (TextView) convertView.findViewById(R.id.class_tv);
            viewHolder.teacher_name_tv = (TextView) convertView.findViewById(R.id.teacher_name_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.type_tv.setText(stringList.get(position).getStuscoreTypeVal());
        viewHolder.subject_tv.setText(stringList.get(position).getSubjectName());
        viewHolder.class_tv.setText(stringList.get(position).getClassName());
        viewHolder.teacher_name_tv.setText(stringList.get(position).getCreateUserName());
        viewHolder.time_tv.setText(stringList.get(position).getCreateTime().split(" ")[0]);
        return convertView;
    }
    class ViewHolder{
        TextView type_tv,subject_tv,class_tv, teacher_name_tv, time_tv;
    }
}
