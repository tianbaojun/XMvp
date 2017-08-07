package com.zjhz.teacher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.ui.activity.SendMessageParentActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public class SendMessageParentItemAdapter extends BaseAdapter{
    private SendMessageParentActivity activity;
    private List<ClassesBeans> lists;
    private int index;
    public List<ClassesBeans> getLists() {
        return lists;
    }

    public void setLists(List<ClassesBeans> lists) {
        this.lists = lists;
    }

    public SendMessageParentItemAdapter(SendMessageParentActivity activity, int index) {
        this.activity = activity;
        this.index = index;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.leave_expandble_list_view_child, viewGroup, false);
        }
        TextView title = BaseViewHolder.get(convertView, R.id.leave_expandble_list_view_child_title);
        final ImageView image = BaseViewHolder.get(convertView, R.id.leave_expandble_list_view_child_image);

        title.setText(lists.get(position).getName());

        if (lists.get(position).isChecd()) {
            image.setImageResource(R.mipmap.leave_expandble_child_clicked);
        }else{
            image.setImageResource(R.mipmap.leave_expandble_child_unclicked);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).isChecd()){
                    lists.get(position).setChecd(false);
                    if (activity.classesId.contains(lists.get(position))){
                        activity.classesId.remove(lists.get(position));
                    }
                    image.setImageResource(R.mipmap.leave_expandble_child_unclicked);
                }else {
                    lists.get(position).setChecd(true);
                    activity.classesId.add(lists.get(position));
                    image.setImageResource(R.mipmap.leave_expandble_child_clicked);
                }
                int y = -1;
                for (int i = 0 ; i <lists.size() ; i ++){
                    if(lists.get(i).isChecd()){
                        y++;
                    }
                }
                if (y == lists.size() - 1){
                    activity.update(index,true);
                }else {
                    activity.update(index,false);
                }
            }
        });
        return convertView;
    }
}
