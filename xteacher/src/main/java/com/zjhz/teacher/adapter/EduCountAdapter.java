package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.EduCount;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class EduCountAdapter extends BaseAdapter {

    private Context context;
    private List<EduCount> counts;

    public EduCountAdapter(Context context, List<EduCount> counts) {
        this.context = context;
        this.counts = counts;
    }

    @Override
    public int getCount() {
        if (counts == null) {
            return 0;
        }else{
            return counts.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (counts == null) {
            return null;
        }else{
            return counts.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        EduCount eduCount = (EduCount) getItem(position);
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.activity_edu_count_item,null);
            vh.num = (TextView) convertView.findViewById(R.id.num);
            vh.classz = (TextView) convertView.findViewById(R.id.grade);
            vh.classCount = (TextView) convertView.findViewById(R.id.grade_check);
            vh.personCount = (TextView) convertView.findViewById(R.id.person_check);
            vh.total = (TextView) convertView.findViewById(R.id.total);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.num.setText(String.valueOf(position+1));
        vh.classz.setText(eduCount.getClassName());
        vh.classCount.setText(eduCount.getClassMoralScore());
        vh.personCount.setText(eduCount.getIndividualMoralScore());
        vh.total.setText(eduCount.getTotalScore());
        return convertView;
    }

    private class ViewHolder{
        TextView num,classz,classCount,personCount,total;
    }
}
