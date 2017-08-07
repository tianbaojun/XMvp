package com.zjhz.teacher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.LeaveApplyForList;
import com.zjhz.teacher.ui.activity.LeaveApplyForListActivity;
import com.zjhz.teacher.ui.activity.LeaveApplyForStateRejectActivity;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表适配器
 */
public class LeaveApplyForListAdapter extends BaseAdapter{

    private Context context;
    private List<LeaveApplyForList> lists;
    private int myOrApply;

    public LeaveApplyForListAdapter(Context context, List<LeaveApplyForList> lists,int myOrApply) {
        this.context = context;
        this.lists = lists;
        this.myOrApply = myOrApply;
    }

    @Override
    public int getCount() {
        return lists.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_leave_apply_list_item, viewGroup, false);
        }
        CircleImageView head = BaseViewHolder.get(view, R.id.activity_leave_image);
        TextView name = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_name);
        TextView type = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_type);
        final TextView content = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_content);
        TextView time = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_time);
        ImageView stateImg = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_state_img);
        TextView state = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_state);
        ImageView image = BaseViewHolder.get(view, R.id.adapter_leave_apply_list_item_image);

        name.setText(lists.get(i).name);
        type.setText(lists.get(i).type);
        content.setText(lists.get(i).content);

        switch (lists.get(i).type){
            case "事假":
                type.setBackgroundColor(context.getResources().getColor(R.color.leave_sj));
                break;
            case "婚假":
                type.setBackgroundColor(context.getResources().getColor(R.color.leave_hj));
                break;
            case "病假":
                type.setBackgroundColor(context.getResources().getColor(R.color.leave_bj));
                break;
            case "其他":
                type.setBackgroundColor(context.getResources().getColor(R.color.leave_qt));
                break;
        }

        if(myOrApply == 1)
            GlideUtil.loadImageHead(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhotoUrlKey),head);
        else
            GlideUtil.loadImageHead(lists.get(i).photoUrl,head);

        String timeF = DateUtil.getStandardDate(lists.get(i).time);
        time.setText(timeF);

//        Bitmap bitmap= BitmapFactory.decodeResource(view.getResources(), R.mipmap.leave_fix);
        if ("2".equals(lists.get(i).flowStatus)) {
          //  state.setText("审批通过");
//             bitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.finish_icon);
            stateImg.setImageResource(R.mipmap.audit_complete);
            state.setText("审批通过");
            state.setTextColor(context.getResources().getColor(R.color.leave_agree));
        }else{
            stateImg.setImageResource(R.mipmap.audit_ing);
            state.setTextColor(context.getResources().getColor(R.color.leave_audit));
            state.setText(lists.get(i).state);
        }

        if (0 == lists.get(i).curNode) {
            if (myOrApply == 1) {
//                type.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }else{
//                type.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }
//            bitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.leave_back);
            stateImg.setImageResource(R.mipmap.audit_back);
            state.setText("审批驳回");
            state.setTextColor(context.getResources().getColor(R.color.leave_back));

        }else{
//            type.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
        }
        if("3".equals(lists.get(i).flowStatus)){
            image.setVisibility(View.VISIBLE);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,LeaveApplyForStateRejectActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("leaver_list_adapter_oid",lists.get(i).oid);
                intent.putExtra("leaver_list_adapter_startTime",lists.get(i).startTime);
                intent.putExtra("leaver_list_adapter_startTime_sma",lists.get(i).sma);
                intent.putExtra("leaver_list_adapter_endTime",lists.get(i).endTime);
                intent.putExtra("leaver_list_adapter_endTime_ema",lists.get(i).ema);
                intent.putExtra("leaver_list_adapter_appType",lists.get(i).appType);
                intent.putExtra("leaver_list_adapter_reason",lists.get(i).reason);
                ((Activity)context).startActivityForResult(intent, LeaveApplyForListActivity.REFRESH_ONE);
            }
        });
//        state.setBackgroundResource(R.mipmap.leave_fix);
//        state.setBackgroundDrawable(new BitmapDrawable(bitmap));
        return view;
    }
}
