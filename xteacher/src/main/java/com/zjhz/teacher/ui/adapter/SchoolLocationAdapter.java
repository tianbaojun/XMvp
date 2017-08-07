package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.SchoolLocationBean;
import com.zjhz.teacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SchoolLocationAdapter extends BaseAdapter{
    private Context context;
    private List<SchoolLocationBean> beans = new ArrayList<>();
    private boolean isShowCurrent ;

    public boolean isShowCurrent() {
        return isShowCurrent;
    }

    public void setShowCurrent(boolean showCurrent) {
        isShowCurrent = showCurrent;
    }

    public void setBeans(List<SchoolLocationBean> beans) {
        this.beans = beans;
    }
    public SchoolLocationAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_school_location,null);
            viewHolder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            viewHolder.location_iv = (ImageView) convertView.findViewById(R.id.location_iv);
            viewHolder.location_line = (ImageView) convertView.findViewById(R.id.location_line);
            viewHolder.location_time = (TextView) convertView.findViewById(R.id.location_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SchoolLocationBean bean = (SchoolLocationBean) getItem(position);
        viewHolder.location_tv.setText(bean.getStationAddress());
        viewHolder.location_time.setText(bean.getRecordTime());
        if (position == 0 ){
            if (isShowCurrent){
                viewHolder.location_iv.setImageResource(R.mipmap.current_location);
            }else {
                viewHolder.location_iv.setImageResource(R.mipmap.gray_shool_location);
            }
        }else {
            viewHolder.location_iv.setImageResource(R.mipmap.gray_shool_location);
        }
        if (position == (beans.size() - 1)){
            viewHolder.location_line.setVisibility(View.GONE);
        }else {
            viewHolder.location_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class ViewHolder{
        TextView location_tv,location_time;
        ImageView location_iv,location_line;
    }
}
