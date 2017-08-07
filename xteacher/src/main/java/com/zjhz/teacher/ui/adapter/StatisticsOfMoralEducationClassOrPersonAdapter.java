package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.StatisticsOfMoralEducationListBean;
import com.zjhz.teacher.ui.activity.StatisticsOfMoralEducationClassActivity;
import com.zjhz.teacher.ui.activity.StatisticsOfMoralEducationPersonActivity;


import java.util.List;

public class StatisticsOfMoralEducationClassOrPersonAdapter extends BaseAdapter {

    private Context mContext;
    private StatisticsOfMoralEducationClassActivity statisticsOfMoralEducationClassActivity;
    private StatisticsOfMoralEducationPersonActivity statisticsOfMoralEducationPersonActivity;
    private List<StatisticsOfMoralEducationListBean> datas;

    public StatisticsOfMoralEducationClassOrPersonAdapter(Context context, StatisticsOfMoralEducationClassActivity statisticsOfMoralEducationClassActivity) {
        this.mContext = context;
        this.statisticsOfMoralEducationClassActivity = statisticsOfMoralEducationClassActivity;
    }

    public StatisticsOfMoralEducationClassOrPersonAdapter(Context context, StatisticsOfMoralEducationPersonActivity statisticsOfMoralEducationPersonActivity) {
        this.mContext = context;
        this.statisticsOfMoralEducationPersonActivity = statisticsOfMoralEducationPersonActivity;
    }

    public List<StatisticsOfMoralEducationListBean> getDatas() {
        return datas;
    }

    public void setDatas(List<StatisticsOfMoralEducationListBean> datas) {
        this.datas = datas;
    }

    public void addDatas(List<StatisticsOfMoralEducationListBean> data) {
        datas.addAll(data);
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_statistics_of_moral_class_person, viewGroup, false);
        }
        TextView content = BaseViewHolder.get(convertView, R.id.adapter_statistics_of_moral_class_person_content);
        TextView score = BaseViewHolder.get(convertView, R.id.adapter_statistics_of_moral_class_person_score);

        content.setText(datas.get(i).name);
        score.setText(datas.get(i).score);
        return convertView;
    }

}
