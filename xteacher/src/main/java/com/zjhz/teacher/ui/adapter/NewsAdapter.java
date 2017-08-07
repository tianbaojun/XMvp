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
 * 新闻
 * Created by xiangxue on 2016/6/8.
 * Modify: fei.wang 2016.6.18
 */
public class NewsAdapter extends BaseAdapter{
    private Context context;
    private List<NewsBean> datas;

    public List<NewsBean> getDatas() {
        return datas;
    }

    public void setDatas(List<NewsBean> datas) {
        this.datas = datas;
    }

    public void addDatas(List<NewsBean> data){
        datas.addAll(data);
    }

    public NewsAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_news,null);
            viewHolder.news_title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.news_content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            viewHolder.news_pic = (ImageView) convertView.findViewById(R.id.img_iv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.user_tv = (TextView) convertView.findViewById(R.id.user_tv);
            viewHolder.dianzan_left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            viewHolder.pinglun_right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.news_title_tv.setText(SharePreCache.isEmpty(datas.get(position).getTitle())?"":datas.get(position).getTitle());
        viewHolder.news_content_tv.setText(SharePreCache.isEmpty(datas.get(position).getSummary())?"":datas.get(position).getSummary());
        String date = DateUtil.getStandardDate(SharePreCache.isEmpty(datas.get(position).getPublishTime())?"":datas.get(position).getPublishTime());
        if(date != null && date.length() > 11)
            viewHolder.time_tv.setText(date.substring(0,11));
        viewHolder.user_tv.setText("发自"+datas.get(position).getPublishUserVal());
        viewHolder.dianzan_left_tv.setText(datas.get(position).getPraiseNum() + "");
        viewHolder.pinglun_right_tv.setText(datas.get(position).getReplyNum() + "");
        String imgUrl = datas.get(position).getHeadImg();
        if (!SharePreCache.isEmpty(imgUrl)){
//            GlideUtil.loadImageWithDefault(imgUrl,viewHolder.news_pic, R.mipmap.adapter_news_none);
            GlideUtil.loadImageNews(imgUrl, viewHolder.news_pic);
        }else {
            viewHolder.news_pic.setImageResource(R.mipmap.adapter_news_none);
        }
        return convertView;
    }

    class ViewHolder{
        TextView news_title_tv,news_content_tv,time_tv,user_tv,dianzan_left_tv,pinglun_right_tv;
        ImageView news_pic;
    }
}
