package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.NewsBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.List;

/**
 * 通知公告
 * Created by xiangxue on 2016/6/16.
 */
public class AnnouncementAdapter extends BaseAdapter{
    private Context context;
    private List<NewsBean> datas;

    public List<NewsBean> getDatas() {
        return datas;
    }

    public void setDatas(List<NewsBean> datas) {
        this.datas = datas;
    }
    public void addDatas(List<NewsBean> datas) {
        this.datas.addAll(datas) ;
    }
    public AnnouncementAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (datas != null){
            return datas.size();
        }
        return 0;
    }

    @Override
    public NewsBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_announcement,null);
            viewHolder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            viewHolder.img_iv = (ImageView) convertView.findViewById(R.id.img_iv);
            viewHolder.create_user_tv = (TextView) convertView.findViewById(R.id.bottom_left_tv);
            viewHolder.create_time_tv = (TextView) convertView.findViewById(R.id.bottom_right_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title_tv.setText(datas.get(position).getTitle());
        viewHolder.content_tv.setText(datas.get(position).getSummary());
        String url = datas.get(position).getHeadImg();
        if (!SharePreCache.isEmpty(url)){
            GlideUtil.loadImageNews(url,viewHolder.img_iv);
        }
        String date = DateUtil.getStandardDate(datas.get(position).getPublishTime());
        viewHolder.create_time_tv.setText(date);
        viewHolder.create_user_tv.setText(SharePreCache.isEmpty(datas.get(position).getCreateUserVal())?"":"发自"+datas.get(position).getCreateUserVal());
        return convertView;
    }

    class ViewHolder{
        TextView title_tv,content_tv,create_user_tv,create_time_tv;
        ImageView img_iv;
    }
}
