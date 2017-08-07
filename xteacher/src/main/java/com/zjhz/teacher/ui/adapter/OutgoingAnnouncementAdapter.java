package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.OutgoingBean;
import com.zjhz.teacher.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/27.
 */
public class OutgoingAnnouncementAdapter extends BaseAdapter{
    private Context context;
    private List<OutgoingBean> datas;

    public OutgoingAnnouncementAdapter(Context context) {
        this.context = context;
    }

    public List<OutgoingBean> getDatas() {
        return datas;
    }

    public void setDatas(List<OutgoingBean> datas) {
        this.datas = datas;
    }

    public void addAllDatas(List<OutgoingBean> datas) {
        this.datas.addAll(datas);
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0){
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (datas != null && datas.size() > 0){
            return datas.get(position);
        }
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_outgoing,null);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.bottom_left_tv);
            viewHolder.user_tv = (TextView) convertView.findViewById(R.id.bottom_right_tv);
            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            viewHolder.view = (TextView) convertView.findViewById(R.id.view);

            viewHolder.tvs = (TextView) convertView.findViewById(R.id.tvs);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OutgoingBean bean = (OutgoingBean) getItem(position);
        if (bean != null){
            viewHolder.content_tv.setText(bean.getAnnContent());
//            String bespeakTime = DateUtil.getStandardDate(bean.getPublishTime());
            viewHolder.time_tv.setText(bean.getPublishTime().split(" ")[0]);
            viewHolder.user_tv.setText("发自"+bean.getName());
            viewHolder.date_tv.setText(bean.getPublishTime().split(" ")[0]);
            if (position == 0){
                viewHolder.date_tv.setVisibility(View.VISIBLE);
            }else {
                String p0 = datas.get(position - 1).getPublishTime().split(" ")[0];
                String p1 = bean.getPublishTime().split(" ")[0];
                if (p0.equals(p1)){
                    viewHolder.view.setVisibility(View.GONE);
                    viewHolder.date_tv.setVisibility(View.GONE);
                }else {
                    viewHolder.view.setVisibility(View.VISIBLE);
                    viewHolder.date_tv.setVisibility(View.VISIBLE);
                }
            }
            if (position == datas.size() - 1){
                viewHolder.tvs.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvs.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    class ViewHolder{
        TextView content_tv,user_tv,time_tv,date_tv,view,tvs;
    }
}
