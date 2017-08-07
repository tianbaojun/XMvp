package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.ClassAndGradeEduNormalBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ClassAndGradeEduNormalAdapter extends BaseAdapter {
    private Context context;
    private List<ClassAndGradeEduNormalBean> list;

    public ClassAndGradeEduNormalAdapter(Context context, List<ClassAndGradeEduNormalBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ClassAndGradeEduNormalBean bean = list.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_classandcrade_education_check_normal, viewGroup, false);
        }
        TextView clazz = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_check_normal_clazz);
        TextView project = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_check_normal_project);
        TextView score = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_check_normal_text);
        clazz.setText(bean.clazz);
        project.setText(bean.project);
        score.setText(bean.score);
        return view;
    }
}
