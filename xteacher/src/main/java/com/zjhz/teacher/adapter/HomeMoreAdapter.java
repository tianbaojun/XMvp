package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.HomeGridView;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-9
 * Time: 15:57
 * Description: 首页更多适配器
 */
public class HomeMoreAdapter extends BaseAdapter {
    
    private Context context;
    private List<HomeGridView> lists;

    public HomeMoreAdapter(Context context, List<HomeGridView> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {
            return lists.size();
        }else{
            return 0;
        }
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_home_grid,viewGroup,false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.adapter_home_image);
        TextView text = (TextView) view.findViewById(R.id.adapter_home_text);
        GlideUtil.loadImage(lists.get(position).image,imageView);
        text.setText(lists.get(position).name);
        return view;
    }
}
