package com.zjhz.teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.SendDeptBean;
import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.SendMessageTeacherActivity;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 群发消息  校内老师 适配器
 */
public class SendMessageTeacherAdapter extends BaseAdapter {

    private Context context;
    private List<SendDeptBean> deptList;
    private List<List<TeacherBean>> itemLists;
    private boolean isOpen = true;
    private boolean isAlls = false;

    public boolean isAll() {
        return isAlls;
    }

    public void setAll(boolean all) {
        isAlls = all;
    }

    SendMessageTeacherActivity activity;
    public SendMessageTeacherAdapter(Context context, SendMessageTeacherActivity activity, List<SendDeptBean> lists, List<List<TeacherBean>> itemLists) {
        this.context = context;
        this.activity = activity;
        this.deptList = lists;
        this.itemLists = itemLists;
    }
    @Override
    public int getCount() {
        return deptList.size();
    }
    @Override
    public Object getItem(int i) {
        return deptList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.leave_expandble_list_view_group, viewGroup, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.leave_expandble_list_view_group_title);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.leave_expandble_list_view_group_cb);
            holder.image = (ImageView) convertView.findViewById(R.id.leave_expandble_list_view_group_image);
            holder.list = (ListView) convertView.findViewById(R.id.leave_expandble_list_view_group_List);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.leave_expandble_list_view_group_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.list.setSelector(new ColorDrawable(Color.TRANSPARENT));
        final List<TeacherBean>  child = itemLists.get(position);
        //对下面的联系人进行排序（按姓氏）
//        Collections.sort(child);
        holder.title.setText(deptList.get(position).getName()+"("+child.size()+"人)");
        final SendMessageTeacherItemAdapter itemAdapter = new SendMessageTeacherItemAdapter(activity,position);
        itemAdapter.setLists(child);
        holder.list.setAdapter(itemAdapter);
        if (deptList.get(position).isChecked()){
            holder.checkBox.setImageResource(R.mipmap.btn_check_on);
            for (int i = 0 ; i < child.size() ; i ++){
                child.get(i).setChecd(true);
            }
            itemAdapter.notifyDataSetChanged();
        }else {
            holder.checkBox.setImageResource(R.mipmap.btn_check_off);
            if (isAlls){
                for (int i = 0 ; i < child.size() ; i ++){
                    child.get(i).setChecd(false);
                }
                itemAdapter.notifyDataSetChanged();
            }
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    isOpen = false;
                    holder.list.setVisibility(View.VISIBLE);
                    holder.image.setImageResource(R.mipmap.leave_expandble_up_arrow);
                } else {
                    isOpen = true;
                    holder.list.setVisibility(View.GONE);
                    holder.image.setImageResource(R.mipmap.leave_expandble_down_arrow);
                }
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deptList.get(position).isChecked()){
                    deptList.get(position).setChecked(false);
                    for (int i = 0 ; i < child.size() ; i ++){
                        child.get(i).setChecd(false);
                        if (activity.teacherBean.contains(child.get(i))){
                            activity.teacherBean.remove(child.get(i));
                        }
                    }
                    holder.checkBox.setImageResource(R.mipmap.btn_check_off);
                    activity.all_cb.setImageResource(R.mipmap.btn_check_off);
                    activity.isSelectAll = true;
                    activity.hint_text.setText("全选");
                }else {
                    deptList.get(position).setChecked(true);
                    for (int i = 0 ; i < child.size() ; i ++){
                        child.get(i).setChecd(true);
                        activity.teacherBean.add(child.get(i));
                    }
                    holder.checkBox.setImageResource(R.mipmap.btn_check_on);
                    int isIndex = 0;
                    for (int i = 0 ; i < deptList.size() ; i++){
                        if (deptList.get(i).isChecked()){
                            isIndex ++;
                        }
                    }
                    if (isIndex == deptList.size()){
                        activity.all_cb.setImageResource(R.mipmap.btn_check_on);
                        activity.isSelectAll = false;
                        activity.hint_text.setText("取消");
                    }
                }
                itemAdapter.setLists(child);
                itemAdapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView title;
        ImageView checkBox;
        ImageView image;
        ListView list;
        RelativeLayout relativeLayout;
    }
}
