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
import com.zjhz.teacher.ui.activity.StatisticsOfMoralEducationMainActivity;
import com.zjhz.teacher.ui.view.TimePickerView;

import java.util.List;


public class StatisticsOfMoralEducationAdapter extends BaseAdapter {
    private StatisticsOfMoralEducationMainActivity activity;
    private List<StatisticsOfMoralEducationListBean> datas;
    private int type;

    public StatisticsOfMoralEducationAdapter(List<StatisticsOfMoralEducationListBean> datas, StatisticsOfMoralEducationMainActivity activity, int type) {
        this.datas = datas;
        this.activity = activity;
        this.type = type;
    }

    @Override
    public int getCount() {
        return datas.size();
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_statistics_of_moral_item, viewGroup, false);
        }
        TextView content = BaseViewHolder.get(convertView, R.id.adapter_statistics_of_moral_list_content);
        TextView score = BaseViewHolder.get(convertView, R.id.adapter_statistics_of_moral_list_score);
        View view = BaseViewHolder.get(convertView, R.id.adapter_statistics_of_moral_list_view);
        if (i == datas.size() - 1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        content.setText(datas.get(i).name);
        score.setText(datas.get(i).score);
        return convertView;
    }

}
