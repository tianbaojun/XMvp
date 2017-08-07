package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业预览班级文字的适配器
 */
public class WorkPreviewGridAdapter extends BaseAdapter{

    private Context context;
    private List<ClassesBeans> lists;

    public WorkPreviewGridAdapter(List<ClassesBeans> lists, Context context) {
        this.lists = lists;
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_work_manager_text_grid,null,false);
            holder.textView = (TextView) view.findViewById(R.id.adapter_work_manager_text_grid_text);
            view.setTag(holder);
        }else{holder = (ViewHolder) view.getTag();}
        ClassesBeans beans = lists.get(i);
        if (beans != null && !beans.equals("null")){
            if (!SharePreCache.isEmpty(beans.getClassName())){
                holder.textView.setText(lists.get(i).getClassName());
            }
        }
        return view;
    }

    class ViewHolder{
        TextView textView;
    }
}
