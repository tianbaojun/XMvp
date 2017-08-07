package com.zjhz.teacher.ui.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SupplyTeacherRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.LeaveStateSchedule;
import com.zjhz.teacher.bean.TipsayBean;
import com.zjhz.teacher.ui.activity.LeaveApplyForStateActivity;

import com.zjhz.teacher.ui.view.ScrollAnimatedExpandableListView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假申请状态
 */
public class LeaveApplyForStateDelegate {

    LeaveApplyForStateActivity activity;
    public ExampleAdapter adapter;
    public List<GroupItem> items = new ArrayList<>();
    public List<LeaveStateSchedule> lists = new ArrayList<>();
    List<TipsayBean>  tipsayBeans = new ArrayList<>();

    public int mGroupPosition,mChildPosition;

    public LeaveApplyForStateDelegate(LeaveApplyForStateActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        initView();
//        initList();
        initData();
    }

    private void initView() {
        adapter = new ExampleAdapter(activity);
        adapter.setData(items);

        activity.expandableListView.setGroupIndicator(null);
        activity.expandableListView.setAdapter(adapter);

        activity.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (activity.expandableListView.isGroupExpanded(groupPosition)) {
                    activity.expandableListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    activity.expandableListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
    }

    int type; // 2表示不可以修改代课老师
    public void initList(int type) {
        this.type = type;
        items.clear();
        for(int i = 0; i < lists.size(); i++) {

            GroupItem item = new GroupItem();

            item.title = lists.get(i).date;

            for(int j = 0; j < lists.get(i).lists.size(); j++) {
                ChildItem child = new ChildItem();
                child.knob = lists.get(i).lists.get(j).clazz;
                child.subject = lists.get(i).lists.get(j).subject;
                child.myClass = lists.get(i).lists.get(j).className;
                child.tipsay = lists.get(i).lists.get(j).tipsay;
                child.knowsStatus = lists.get(i).lists.get(j).knowsStatus;
                item.items.add(child);
            }

            ChildItem childFly = new ChildItem();
            childFly.knob = "节次 ";
            childFly.subject = "科目";
            childFly.myClass = "班级";
            childFly.tipsay = "代课";
            item.items.add(0,childFly);
            items.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void initData() {

    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<>();
    }

    private static class ChildItem {
        String knob;
        String subject;
        String myClass;
        String tipsay;
        int knowsStatus;
    }

    private static class ChildHolder {
        TextView knob;
        TextView subject;
        TextView myClass;
        TextView tipsay;  // 代课
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * 适配器
     */
    private class ExampleAdapter extends ScrollAnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildHolder holder;
            final ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.adapter_child_leave_content, parent, false);
                holder.knob = (TextView) convertView.findViewById(R.id.adapter_child_leave_content_knob);
                holder.subject = (TextView) convertView.findViewById(R.id.adapter_child_leave_content_subject);
                holder.myClass = (TextView) convertView.findViewById(R.id.adapter_child_leave_content_class);
                holder.tipsay = (TextView) convertView.findViewById(R.id.adapter_child_leave_content_substitute_tipsay);  // 代课
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.knob.setText(item.knob);
            holder.subject.setText(item.subject);
            holder.myClass.setText(item.myClass);
            holder.tipsay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activity.approveFlag.equals("1") && !activity.isCheck){
                        activity.dialog.show();
                        mGroupPosition = groupPosition;
                        mChildPosition = childPosition - 1;
                        LogUtil.e("父角标 = ",mGroupPosition + "" + "子角标 = " + mChildPosition);
                        SharedPreferencesUtils.putSharePrefString("fly_tipsay",items.get(groupPosition).title);
                        if (SharePreCache.isEmpty(item.tipsay)){
                            activity.isUpdate = false;
                        }else {
                            activity.isUpdate = true;
                        }
                        NetworkRequest.request(new SupplyTeacherRequest(activity.applyerTeacherId,items.get(groupPosition).title+" 00:00:00",items.get(groupPosition).title+" 00:00:00"), CommonUrl.SUPPLYTEACHER, Config.SUPPLYTEACHER);
                    }else {
                        if (!activity.isCheck){
                            ToastUtils.showShort("审批过，不可再修改");
                        }
                    }
                }
            });
            if (childPosition != 0) {
                if (type == 2) {
                    if (!TextUtils.isEmpty(item.tipsay)) {
                        Drawable drawable = activity.getResources().getDrawable(R.mipmap.substitute_teacher_subtract);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.tipsay.setCompoundDrawables(drawable, null, null, null);
                        holder.tipsay.setClickable(false);
                    } else {
                        Drawable drawable = activity.getResources().getDrawable(R.mipmap.substitute_teacher_add);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.tipsay.setCompoundDrawables(drawable, null, null, null);
                        holder.tipsay.setClickable(false);
                    }
                }else {
                    if (!TextUtils.isEmpty(item.tipsay)) {
                        Drawable drawable = activity.getResources().getDrawable(R.mipmap.substitute_teacher_subtract);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.tipsay.setCompoundDrawables(drawable, null, null, null);
                        holder.tipsay.setClickable(true);
                    } else {
                        Drawable drawable = activity.getResources().getDrawable(R.mipmap.substitute_teacher_add);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        holder.tipsay.setCompoundDrawables(drawable, null, null, null);
                        holder.tipsay.setClickable(true);
                    }
                }
            }else {
                Drawable drawable= activity.getResources().getDrawable(R.mipmap.substitute_teacher);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tipsay.setCompoundDrawables(drawable,null,null,null);
                holder.tipsay.setClickable(false);
//                holder.tipsay.setText("代课");
            }

            holder.tipsay.setText(item.tipsay);
            if(item.knowsStatus == 1){
                holder.tipsay.setTextColor(activity.getResources().getColor(R.color.red));
            }else {
                holder.tipsay.setTextColor(activity.getResources().getColor(R.color.text_color32));
            }

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.adapter_group_leave_content_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.adapter_group_leave_content_item_text);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            int dayofweek = DateUtil.dayForWeek(item.title);
            if (dayofweek == 1){
                holder.title.setText(item.title+"   星期一");
            }else if (dayofweek == 2){
                holder.title.setText(item.title+"   星期二");
            }else if (dayofweek == 3){
                holder.title.setText(item.title+"   星期三");
            }else if (dayofweek == 4){
                holder.title.setText(item.title+"   星期四");
            }else if (dayofweek == 5){
                holder.title.setText(item.title+"   星期五");
            }else if (dayofweek == 6){
                holder.title.setText(item.title+"   星期六");
            }else if (dayofweek == 7){
                holder.title.setText(item.title+"   星期日");
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }

}
