package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议通知接收者adapter
 * Created by Administrator on 2016/6/2.
 */
public class SelectReceviceAdapter extends BaseAdapter{
    private Context context;
    private List<Integer> lists;
    private List<Integer> selects;

    public SelectReceviceAdapter(Context context){
        lists = new ArrayList<>();
        selects = new ArrayList<>();
        this.context = context;
        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        lists.add(5);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_recevice_item,null);
            viewHolder.recevice_name_tv  = (TextView) view.findViewById(R.id.recevice_name_tv);
            viewHolder.select_rl = (RelativeLayout) view.findViewById(R.id.select_rl);
            viewHolder.select_true_iv = (ImageView) view.findViewById(R.id.select_true_iv);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.select_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selects.contains(lists.get(i))){
                    viewHolder.select_true_iv.setVisibility(View.GONE);
                    selects.remove(lists.get(i));
                }else {
                    selects.add(lists.get(i));
                    viewHolder.select_true_iv.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    class ViewHolder{
        TextView recevice_name_tv;
        ImageView select_true_iv;
        RelativeLayout select_rl;
    }
}
