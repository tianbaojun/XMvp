package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.AttendanceListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 考勤
 */
public class AttendanceAdapter extends BaseAdapter {

    private Context context;
    private List<AttendanceListBean> datas ;

    public AttendanceAdapter(Context context, List<AttendanceListBean> datas ) {
        this.context = context;
        this.datas = datas;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_attendance_list_item, viewGroup, false);
        }
        TextView name = BaseViewHolder.get(view, R.id.adapter_attendance_list_item_name);
        TextView time = BaseViewHolder.get(view, R.id.adapter_attendance_list_item_time);
        TextView location = BaseViewHolder.get(view, R.id.adapter_attendance_list_item_location);
        TextView campu = BaseViewHolder.get(view, R.id.adapter_attendance_list_item_campu);
        name.setText(datas.get(i).getName());
        time.setText(datas.get(i).getRecordTime());
        location.setText(datas.get(i).getStationAddress());
        int status = datas.get(i).getFunction1();
        int flag = datas.get(i).getFlag();
        if (status == 1 && flag == 0){
            campu.setText("入校");
            name.setTextColor(context.getResources().getColor(R.color.text_color28));
            time.setTextColor(context.getResources().getColor(R.color.text_color28));
            location.setTextColor(context.getResources().getColor(R.color.text_color28));
            campu.setTextColor(context.getResources().getColor(R.color.text_color28));
        }else if (status == 0 && flag == 0){
            campu.setText("出校");
            name.setTextColor(context.getResources().getColor(R.color.text_color28));
            time.setTextColor(context.getResources().getColor(R.color.text_color28));
            location.setTextColor(context.getResources().getColor(R.color.text_color28));
            campu.setTextColor(context.getResources().getColor(R.color.text_color28));
        }else if (status == 1 && flag == 1){
            campu.setText("迟到");
            name.setTextColor(context.getResources().getColor(R.color.text_coloree452f));
            time.setTextColor(context.getResources().getColor(R.color.text_coloree452f));
            location.setTextColor(context.getResources().getColor(R.color.text_coloree452f));
            campu.setTextColor(context.getResources().getColor(R.color.text_coloree452f));
        }else if (status == 0 && flag == 2){
            campu.setText("早退");
            name.setTextColor(context.getResources().getColor(R.color.text_colore4288c4));
            time.setTextColor(context.getResources().getColor(R.color.text_colore4288c4));
            location.setTextColor(context.getResources().getColor(R.color.text_colore4288c4));
            campu.setTextColor(context.getResources().getColor(R.color.text_colore4288c4));
        }else if (status == 2 && flag == 0){
            campu.setText("未知");
            name.setTextColor(context.getResources().getColor(R.color.text_color28));
            time.setTextColor(context.getResources().getColor(R.color.text_color28));
            location.setTextColor(context.getResources().getColor(R.color.text_color28));
            campu.setTextColor(context.getResources().getColor(R.color.text_color28));
        }
        return view;
    }
}
