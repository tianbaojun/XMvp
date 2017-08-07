package com.zjhz.teacher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.ui.activity.SendMessageTeacherActivity;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 群发消息  校内老师 适配器
 */
public class SendMessageTeacherItemAdapter extends BaseAdapter {

    private SendMessageTeacherActivity activity;
    private List<TeacherBean> lists;
    private int index;
    public List<TeacherBean> getLists() {
        return lists;
    }

    public void setLists(List<TeacherBean> lists) {
        this.lists = lists;
    }

    public SendMessageTeacherItemAdapter(SendMessageTeacherActivity activity,int index) {
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

        title.setText(lists.get(position).getNickName());

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
                    image.setImageResource(R.mipmap.leave_expandble_child_unclicked);
                    if (activity.teacherBean.contains(lists.get(position))){
                        activity.teacherBean.remove(lists.get(position));
                    }
                }else {
                    lists.get(position).setChecd(true);
                    activity.teacherBean.add(lists.get(position));
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
