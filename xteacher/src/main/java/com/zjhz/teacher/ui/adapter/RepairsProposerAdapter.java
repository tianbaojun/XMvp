package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.response.RepairsListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.RepairReSubmitActivity;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * 修理
 * Created by Administrator on 2016/7/1.
 */
public class RepairsProposerAdapter extends BaseAdapter{
    private Context context;
    private List<RepairsListBean> beans;
    private int type;

    public RepairsProposerAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public List<RepairsListBean> getBeans() {
        return beans;
    }
    public void setBeans(List<RepairsListBean> beans) {
        this.beans = beans;
    }
    public void addBeans(List<RepairsListBean> beans) {
        this.beans.addAll(beans);
    }
    @Override
    public int getCount() {
        if (beans == null){
            return 0;
        }
        return beans.size();
    }

    @Override
    public RepairsListBean getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_repairs,parent,false);
            viewHolder.repair_image = (CircleImageView)convertView.findViewById(R.id.repair_image);
            viewHolder.applyer_tv = (TextView) convertView.findViewById(R.id.applyer_tv);
            viewHolder.release_time_tv = (TextView) convertView.findViewById(R.id.release_time_tv);
            viewHolder.edit_iv = (ImageView) convertView.findViewById(R.id.edit_iv);
            viewHolder.summary_tv = (TextView) convertView.findViewById(R.id.summary_tv);
            viewHolder.type_tv_img = (ImageView) convertView.findViewById(R.id.type_tv_img);
            viewHolder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RepairsListBean bean = getItem(position);

        if(type == 1)
            GlideUtil.loadImageHead(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhotoUrlKey), viewHolder.repair_image);
        else if(type == 2)
            GlideUtil.loadImageHead(bean.getPhotoUrl(), viewHolder.repair_image);

        viewHolder.applyer_tv.setText(bean.getNickName());
        viewHolder.summary_tv.setText(bean.getOrginAddress()+"  需要维修  "+bean.getItemName());
        String time = DateUtil.getStandardDate(bean.getCreateTime());
        viewHolder.release_time_tv.setText(time);
//        Bitmap bitmap= BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.leave_fix);
        viewHolder.type_tv_img.setImageResource(R.mipmap.audit_ing);
        if (bean.getOrderStateMsg() != null){
            viewHolder.edit_iv.setVisibility(View.GONE);
            viewHolder.type_tv.setVisibility(View.VISIBLE);
            viewHolder.type_tv.setText(bean.getOrderStateMsg().getNodeName());
            if (bean.getOrderStateMsg().getCurNode() == 0){
//                viewHolder.edit_iv.setVisibility(View.VISIBLE);
                if (bean.getOrderStateMsg().getNodeName().equals("reject submit user")) {
                    viewHolder.type_tv_img.setImageResource(R.mipmap.audit_back);
//                    bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.leave_back);
                    viewHolder.type_tv.setText("审批驳回");
                    viewHolder.type_tv.setTextColor(context.getResources().getColor(R.color.leave_back));
                }
            }else {
                viewHolder.edit_iv.setVisibility(View.GONE);
            }
            if (bean.getOrderStateMsg().getFlowStatus() == 1){
                viewHolder.type_tv_img.setImageResource(R.mipmap.audit_complete);
//                bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.finish_icon);
                viewHolder.type_tv.setText("审批通过");
                viewHolder.type_tv.setTextColor(context.getResources().getColor(R.color.leave_agree));
            }
        }else if (bean.getOrderFlowState() != null){
            viewHolder.type_tv.setVisibility(View.VISIBLE);
            viewHolder.type_tv.setText(bean.getOrderFlowState().getNodeName());
            if (bean.getOrderFlowState().getCurNode() == 0){
                viewHolder.edit_iv.setVisibility(View.VISIBLE);
                if (bean.getOrderFlowState().getNodeName().equals("reject submit user")){
                    viewHolder.type_tv_img.setImageResource(R.mipmap.audit_back);
//                    bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.leave_back);
                    viewHolder.type_tv.setText("审批驳回");
                    viewHolder.type_tv.setTextColor(context.getResources().getColor(R.color.leave_back));
                }
            }else {
                viewHolder.edit_iv.setVisibility(View.GONE);
            }
            if (bean.getOrderFlowState().getFlowStatus() == 1){
                viewHolder.type_tv_img.setImageResource(R.mipmap.audit_complete);
//                bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.finish_icon);
                viewHolder.type_tv.setText("审批通过");
                viewHolder.type_tv.setTextColor(context.getResources().getColor(R.color.leave_agree));
            }



        }else {
            viewHolder.type_tv.setVisibility(View.GONE);
        }
        if(bean.getStatus() == 4 && type == 1){
            viewHolder.edit_iv.setVisibility(View.VISIBLE);
        }
//        viewHolder.type_tv.setBackgroundDrawable(new BitmapDrawable(bitmap));
        viewHolder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RepairReSubmitActivity.class);
                intent.putExtra("repairId",bean.getRepairId());
                intent.putExtra("update_index",position);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView repair_image;
        ImageView edit_iv, type_tv_img;
        TextView type_tv,applyer_tv,summary_tv,release_time_tv;
    }
}
