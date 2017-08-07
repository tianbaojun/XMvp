package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.MeetingBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 会议通知adapter
 * Created by xiangxue on 2016/6/17.
 */
public class MeetingNoticeAdapter extends BaseAdapter{
    private Context context;
    private List<MeetingBean> beans;
    private int rightWidth;

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
        ids.clear();
        notifyDataSetChanged();
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    //是否显示动画，
    private boolean showCheckBox = false;
    //多选选中的会议通知ids
    public List<String> ids = new ArrayList<>();

    public List<MeetingBean> getBeans() {
        return beans;
    }

    public void setOnItemRightClickListener(IOnItemRightClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    public void setBeans(List<MeetingBean> beans) {
        this.beans = beans;
    }
    public void addBeans(List<MeetingBean> beans) {
        this.beans.addAll(beans);
    }

    public MeetingNoticeAdapter(Context context) {
        this.context = context;
    }
    public MeetingNoticeAdapter(Context context,List<MeetingBean> beans,int rightWidth) {
        this.context = context;
        this.rightWidth = rightWidth;
        this.beans = beans;
    }
    public MeetingNoticeAdapter(Context context,int rightWidth) {
        this.context = context;
        this.rightWidth = rightWidth;
    }

    @Override
    public int getCount() {
        return beans == null ? 0:beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_meeting_item,null);
            viewHolder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            viewHolder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            viewHolder.user_tv = (TextView) convertView.findViewById(R.id.user_tv);
            viewHolder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
            viewHolder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);
            viewHolder.item_left =  convertView.findViewById(R.id.item_left);
            viewHolder.item_right =  convertView.findViewById(R.id.item_right);
            viewHolder.checked_iv = (ImageView) convertView.findViewById(R.id.checked_iv);
            viewHolder.data_ll = (LinearLayout) convertView.findViewById(R.id.data_ll);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MeetingBean bean = beans.get(position);
        if (bean.getEventTime() != null && bean.getEventTime().length() > 3) {
            String evetnTime = bean.getEventTime().substring(0, bean.getEventTime().length()-3);
            viewHolder.title_tv.setText(SharePreCache.isEmpty(bean.getTitle())?"会议通知【"+evetnTime+"】":bean.getTitle()+"【"+evetnTime+"】");
        }
        viewHolder.content_tv.setText(bean.getContent());
        String time = DateUtil.getStandardDate(bean.getCreateTime());
        viewHolder.time_tv.setText(time);
        viewHolder.user_tv.setText("发自"+bean.getNickName());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(rightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        viewHolder.item_right.setLayoutParams(lp2);
        viewHolder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, position);
                }
            }
        });

        //多选操作
        if (showCheckBox){
            int px = ToolsUtil.dip2px(context, 56);
            TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, -px,Animation.ABSOLUTE, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f);
            anim.setDuration(500);
            viewHolder.data_ll.startAnimation(anim);
            viewHolder.checked_iv.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checked_iv.setVisibility(View.GONE);
        }
        if (ids.contains(bean.getMsgId())){
            viewHolder.checked_iv.setImageResource(R.mipmap.selecttrue_icon);
        }else {
            viewHolder.checked_iv.setImageResource(R.mipmap.selectfalse_icon);
        }
        viewHolder.checked_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ids.contains(bean.getMsgId())){
                    viewHolder.checked_iv.setImageResource(R.mipmap.selectfalse_icon);
                    ids.remove(bean.getMsgId());
                }else {
                    ids.add(bean.getMsgId());
                    viewHolder.checked_iv.setImageResource(R.mipmap.selecttrue_icon);
                }
            }
        });

        Drawable drawableleft= context.getResources().getDrawable(R.mipmap.unread_icon);
        // 这一步必须要做,否则不会显示.
        drawableleft.setBounds(0, 0, drawableleft.getMinimumWidth(), drawableleft.getMinimumHeight());
        viewHolder.left_tv.setCompoundDrawables(drawableleft,null,null,null);
        Drawable drawablerigth= context.getResources().getDrawable(R.mipmap.read_icon);
        drawablerigth.setBounds(0, 0, drawablerigth.getMinimumWidth(), drawablerigth.getMinimumHeight());
        viewHolder.right_tv.setCompoundDrawables(drawablerigth,null,null,null);
        viewHolder.left_tv.setVisibility(View.VISIBLE);
        viewHolder.right_tv.setVisibility(View.VISIBLE);
        viewHolder.left_tv.setText(bean.getUnReadNum());
        viewHolder.right_tv.setText(bean.getStatusNum());
        return convertView;
    }

    class ViewHolder{
        TextView title_tv,content_tv,time_tv,user_tv,left_tv,right_tv;
        View item_left,item_right;
        ImageView checked_iv;
        LinearLayout data_ll;
    }
}
