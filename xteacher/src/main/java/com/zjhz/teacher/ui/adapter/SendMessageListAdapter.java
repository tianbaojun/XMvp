package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.MassgeBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.DateUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class SendMessageListAdapter extends BaseAdapter{
    private Context context;
    private List<MassgeBean> beans ;

    public List<MassgeBean> getBeans() {
        return beans;
    }

    public void setBeans(List<MassgeBean> beans) {
        this.beans = beans;
    }

    public void addBeans(List<MassgeBean> beans) {
        this.beans.addAll(beans);
    }
    public SendMessageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_meeting_item,null);
            viewHolder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.user_tv = (TextView) convertView.findViewById(R.id.user_tv);
            viewHolder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            viewHolder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.title_tv.setText("发送对象："+beans.get(position).getUserNames());
//        viewHolder.title_tv.setTextSize(14);
//        viewHolder.title_tv.setTextColor(context.getResources().getColor(R.color.text_color65));
        viewHolder.title_tv.setVisibility(View.GONE);
        viewHolder.content_tv.setText(beans.get(position).getContent());
        viewHolder.content_tv.setTextSize(16);
        viewHolder.content_tv.setTextColor(context.getResources().getColor(R.color.text_color28));
        Drawable drawableleft= context.getResources().getDrawable(R.mipmap.unread_icon);
        // 这一步必须要做,否则不会显示.
        drawableleft.setBounds(0, 0, drawableleft.getMinimumWidth(), drawableleft.getMinimumHeight());
        viewHolder.left_tv.setCompoundDrawables(drawableleft,null,null,null);

        Drawable drawablerigth= context.getResources().getDrawable(R.mipmap.read_icon);
        drawablerigth.setBounds(0, 0, drawablerigth.getMinimumWidth(), drawablerigth.getMinimumHeight());
        viewHolder.right_tv.setCompoundDrawables(drawablerigth,null,null,null);
        viewHolder.left_tv.setVisibility(View.VISIBLE);
        viewHolder.right_tv.setVisibility(View.VISIBLE);
        viewHolder.left_tv.setText(beans.get(position).getUnReadNum());
        viewHolder.right_tv.setText(beans.get(position).getStatusNum());
        String time = DateUtil.getStandardDate(beans.get(position).getCreateTime());
        viewHolder.time_tv.setText(time);
        viewHolder.user_tv.setText("发自"+beans.get(position).getNickName());
        return convertView;
    }

    class ViewHolder{
        TextView title_tv,content_tv,time_tv,user_tv,left_tv,right_tv;
    }
}
