package com.zjhz.teacher.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.FoodDetailList;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 食谱详情评论适配器
 */
public class FoodDetailListAdapter extends BaseAdapter{

    private Context context;
    private List<FoodDetailList> datas;

    public FoodDetailListAdapter(Context context,List<FoodDetailList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {

        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_food_detail_list,null,false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.adapter_food_detail_list_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.adapter_food_detail_list_time);
            viewHolder.description = (TextView) convertView.findViewById(R.id.adapter_food_detail_list_description);
            viewHolder.user_head = (CircleImageView) convertView.findViewById(R.id.adapter_food_detail_list_imageview);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(datas.get(position).name);
        viewHolder.description.setText(datas.get(position).description);
        if (!TextUtils.isEmpty(datas.get(position).time)) {
            String date = DateUtil.getStandardDate(datas.get(position).time);
            viewHolder.time.setText(date);
        }
        GlideUtil.loadImage(datas.get(position).imageUrl,viewHolder.user_head);
        return convertView;
    }

    class ViewHolder{
        TextView name,time,description;
        CircleImageView user_head;
    }
}
