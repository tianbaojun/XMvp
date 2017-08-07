package com.zjhz.teacher.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
import com.zjhz.teacher.ui.activity.ClassAndGradeEducationDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ClassAndGradeEducationDetailAdapter extends BaseAdapter{

    private ClassAndGradeEducationDetailActivity activity;
    private List<ClassAndGradeEducationListCheckBean> lists;

    public ClassAndGradeEducationDetailAdapter(ClassAndGradeEducationDetailActivity activity, List<ClassAndGradeEducationListCheckBean> lists) {
        this.activity = activity;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.adapter_classandcrade_education_check, viewGroup, false);
            holder.clazz = (TextView) view.findViewById(R.id.adapter_classandcrade_education_check_clazz);
            holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.adapter_classandcrade_education_check_rl);
            holder.project = (TextView) view.findViewById(R.id.adapter_classandcrade_education_check_project);
            holder.editText = (EditText) view.findViewById(R.id.adapter_classandcrade_education_check_editText);
            holder.text = (TextView) view.findViewById(R.id.adapter_classandcrade_education_check_text);
            holder.add_score_ll = (LinearLayout) view.findViewById(R.id.add_score_ll);
            holder.image = (ImageView) view.findViewById(R.id.adapter_classandcrade_education_check_score);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        ClassAndGradeEducationListCheckBean bean = lists.get(i);
        holder.clazz.setText(bean.name);
        holder.project.setText(bean.moralName);
        if (bean.meterMode == 0) {
            holder.add_score_ll.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
            if (bean.scoreMode == 0){
                if (bean.score.equals("0")){
                    holder.image.setImageResource(R.mipmap.edu_check_ok);
                }else {
                    holder.image.setImageResource(R.mipmap.edu_check_error);
                }
            }else {
                if (bean.score.equals("0")){
                    holder.image.setImageResource(R.mipmap.edu_check_error);
                }else {
                    holder.image.setImageResource(R.mipmap.edu_check_ok);
                }
            }
        }else if (bean.meterMode == 1) {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.add_score_ll.setVisibility(View.VISIBLE);
            holder.editText.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(bean.score);
        }
        return view;
    }
    class ViewHolder{
        TextView clazz,project;
        TextView text;
        EditText editText;
        RelativeLayout relativeLayout;
        LinearLayout add_score_ll;
        ImageView image;
    }
}
