package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.FoodBean;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.List;

/**
 * 食谱列表
 * Created by xiangxue on 2016/6/12.
 */
public class FoodAdapter extends BaseAdapter{
    private Context context ;
    private List<FoodBean> datasFood ;

    public List<FoodBean> getDatasFood() {
        return datasFood;
    }

    public void setDatasFood(List<FoodBean> datasFood) {
        this.datasFood = datasFood;
    }

    public void addAll(List<FoodBean> beans){
        datasFood.addAll(beans);
    }

    public FoodAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (datasFood != null && datasFood.size() > 0){
            return datasFood.size();
        }
        return 0;
    }

    @Override
    public FoodBean getItem(int position) {
        return datasFood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null){
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_food_item,null);
            viewHodler.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHodler.type_tv = (ImageView) convertView.findViewById(R.id.type_tv);
            viewHodler.user_head = (CircleImageView) convertView.findViewById(R.id.user_head);
            viewHodler.create_user_tv = (TextView) convertView.findViewById(R.id.create_user_tv);
            viewHodler.create_content_tv = (TextView) convertView.findViewById(R.id.create_content_tv);
            viewHodler.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            viewHodler.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            viewHodler.food_pic_gv = (LinearLayout) convertView.findViewById(R.id.food_pic_gv);
            viewHodler.iv0 = (ImageView) convertView.findViewById(R.id.iv0);
            viewHodler.iv1 = (ImageView) convertView.findViewById(R.id.iv1);
            viewHodler.iv2 = (ImageView) convertView.findViewById(R.id.iv2);
            viewHodler.iv3 = (ImageView) convertView.findViewById(R.id.iv3);
            viewHodler.bottom_line = (TextView) convertView.findViewById(R.id.bottom_line);
            convertView.setTag(viewHodler);
        }else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        if (datasFood != null && datasFood.size() > 0){
            FoodBean foodBean = datasFood.get(position);
            String imgUrl = foodBean.getPhotoUrl();
            if (!SharePreCache.isEmpty(imgUrl)){
                GlideUtil.loadImageHead(foodBean.getPhotoUrl(),viewHodler.user_head);
            }
            viewHodler.time_tv.setText(foodBean.getPublishTime().split(" ")[0]);
            viewHodler.create_user_tv.setText(foodBean.getName());
            viewHodler.create_content_tv.setText(foodBean.getContent());
            viewHodler.left_tv.setText(foodBean.getPraiseNum()+"");
            viewHodler.right_tv.setText(foodBean.getReplyNum()+"");
            List<ImageBean> imgs = foodBean.getImageUrls();
            if (imgs != null && imgs.size() > 0){
                if (imgs.size() == 1){
                    viewHodler.iv0.setVisibility(View.VISIBLE);
                    viewHodler.iv1.setVisibility(View.GONE);
                    viewHodler.iv2.setVisibility(View.GONE);
                    viewHodler.iv3.setVisibility(View.GONE);
                    GlideUtil.loadImage(imgs.get(0).getDocPath(),viewHodler.iv0);
                }else if (imgs.size() == 2){
                    viewHodler.iv0.setVisibility(View.GONE);
                    viewHodler.iv1.setVisibility(View.VISIBLE);
                    viewHodler.iv2.setVisibility(View.VISIBLE);
                    viewHodler.iv3.setVisibility(View.GONE);
                    GlideUtil.loadImage(imgs.get(0).getDocPath(),viewHodler.iv1);
                    GlideUtil.loadImage(imgs.get(1).getDocPath(),viewHodler.iv2);
                }else if (imgs.size() == 3){
                    viewHodler.iv0.setVisibility(View.GONE);
                    viewHodler.iv1.setVisibility(View.VISIBLE);
                    viewHodler.iv2.setVisibility(View.VISIBLE);
                    viewHodler.iv3.setVisibility(View.VISIBLE);
                    GlideUtil.loadImage(imgs.get(0).getDocPath(),viewHodler.iv1);
                    GlideUtil.loadImage(imgs.get(1).getDocPath(),viewHodler.iv2);
                    GlideUtil.loadImage(imgs.get(2).getDocPath(),viewHodler.iv3);
                }
            }
            int type =foodBean.getPattern();
            if (type == 1){
                viewHodler.type_tv.setImageResource(R.mipmap.food_icon1);
            }else if (type == 2){
                viewHodler.type_tv.setImageResource(R.mipmap.food_icon2);
            }else if (type == 3){
                viewHodler.type_tv.setImageResource(R.mipmap.food_icon3);
            }
            if (position == 0){
                viewHodler.bottom_line.setVisibility(View.GONE);
                viewHodler.time_tv.setVisibility(View.VISIBLE);
            }else {
                String f0 = datasFood.get(position - 1).getCreateTime().split(" ")[0];
                String f1 = datasFood.get(position).getCreateTime().split(" ")[0];
                if (f0.equals(f1)){
                    viewHodler.time_tv.setVisibility(View.GONE);
                    viewHodler.bottom_line.setVisibility(View.VISIBLE);
                }else {
                    viewHodler.bottom_line.setVisibility(View.GONE);
                    viewHodler.time_tv.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }

    class ViewHodler{
        ImageView iv0,iv1,iv2,iv3,type_tv;
        CircleImageView user_head;
        TextView time_tv,create_user_tv,create_content_tv,left_tv,right_tv;
        LinearLayout food_pic_gv;
        TextView bottom_line;
    }
}
