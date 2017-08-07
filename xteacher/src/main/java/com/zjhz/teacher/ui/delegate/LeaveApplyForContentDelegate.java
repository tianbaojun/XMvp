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
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SupplyTeacherRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.LeaveStateSchedule;
import com.zjhz.teacher.ui.activity.LeaveApplyForContentActivity;
import com.zjhz.teacher.ui.dialog.SelectTeacherDialog;
import com.zjhz.teacher.ui.view.AnimatedExpandableListView;
import com.zjhz.teacher.ui.view.ScrollAnimatedExpandableListView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-01
 * Time: 15:57
 * Description: 请假申请内容列表
 */
public class LeaveApplyForContentDelegate {

    LeaveApplyForContentActivity activity;
    public ExampleAdapter adapter;
    public List<GroupItem> items = new ArrayList<>();
    public List<LeaveStateSchedule> lists = new ArrayList<>();

    public int mGroupPosition,mChildPosition;

    public LeaveApplyForContentDelegate(LeaveApplyForContentActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        initView();
        initList();
        initData();
    }

    private void initView() {
        activity.left.setText("取消");
        activity.center.setText("请假申请内容");
        activity.right.setText("提交");

        adapter = new ExampleAdapter(activity);
        adapter.setData(items);

        activity.listView.setGroupIndicator(null);
        activity.listView.setAdapter(adapter);

        activity.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (activity.listView.isGroupExpanded(groupPosition)) {
                    activity.listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    activity.listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
    }

    public void initList() {

//
//        for(int i = 1; i < 10; i++) {
//            GroupItem item = new GroupItem();
//
//            item.title = "时间 " + i;
//
//            for(int j = 0; j < i; j++) {
//                ChildItem child = new ChildItem();
//                child.knob = "节次 " + j;
//                child.subject = "科目";
//                child.myClass = "二年级10班";
//                item.items.add(child);
//            }
//
//            items.add(item);
//        }

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
            if(childPosition > 0) {
                holder.tipsay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGroupPosition = groupPosition;
                        mChildPosition = childPosition - 1;
                        if (activity.tipsayBeans.size() > 0) {
                            SelectTeacherDialog dialog = new SelectTeacherDialog(activity, -1, activity.tipsayBeans);
                            dialog.setGroupChild((groupPosition<<16)+childPosition);
                        } else {
                            NetworkRequest.request(new SupplyTeacherRequest(
                                            SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey),
                                            items.get(groupPosition).title + " 00:00:00",
                                            items.get(groupPosition).title + " 00:00:00"),
                                    CommonUrl.SUPPLYTEACHER, Config.SUPPLYTEACHER);
                        }
                    }
                });
            }

            if (childPosition != 0) {
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
            }else {
                Drawable drawable= activity.getResources().getDrawable(R.mipmap.substitute_teacher);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tipsay.setCompoundDrawables(drawable,null,null,null);
                holder.tipsay.setClickable(false);
//                holder.tipsay.setText("代课");
            }

            holder.tipsay.setText(item.tipsay);

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

//            holder.title.setText(item.title);
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
