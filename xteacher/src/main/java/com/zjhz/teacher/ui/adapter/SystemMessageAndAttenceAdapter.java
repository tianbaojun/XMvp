package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class SystemMessageAndAttenceAdapter extends BaseAdapter{
    private Context context;
    private List<Message> datas;
    public SystemMessageAndAttenceAdapter(Context context, List<Message> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
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
        ViewHolder viewHolder;
        if (convertView ==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_systemmessage,null);
            viewHolder.dangerous_address = (TextView) convertView.findViewById(R.id.dangerous_address);
            viewHolder.dangerous_content = (TextView) convertView.findViewById(R.id.dangerous_content);
            viewHolder.dangerous_time = (TextView) convertView.findViewById(R.id.dangerous_time);
            viewHolder.handle_person = (TextView) convertView.findViewById(R.id.handle_person);
            viewHolder.handle_btn = (TextView) convertView.findViewById(R.id.handle_btn);
            viewHolder.handle_ll = (LinearLayout) convertView.findViewById(R.id.handle_ll);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Message message = datas.get(position);
        if (message != null){
            viewHolder.dangerous_content.setText(message.getContent());
            viewHolder.handle_person.setText(message.getCreateTime());
            if (message.getStatus()== 0){
                viewHolder.handle_ll.setBackgroundResource(R.drawable.shape_white_solid_round_ten_stork);
            }else {
                viewHolder.handle_ll.setBackgroundResource(R.drawable.shape_white_solid_round_ten);
            }
        }
        return convertView;
    }
    class ViewHolder{
        TextView dangerous_address,dangerous_content,dangerous_time,handle_person,handle_btn;
        LinearLayout handle_ll;
    }
}
