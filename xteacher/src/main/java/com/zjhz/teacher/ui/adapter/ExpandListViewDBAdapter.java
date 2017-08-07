package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.R;

import java.util.List;

/**
 * Created by zzd on 2017/6/3.
 */
public class ExpandListViewDBAdapter<G, C> extends BaseExpandableListAdapter {

    private final static String BASE = "base";
    private final static String TOP = "top";

    private Context context;

    private List<G> groupList;
    private List<List<C>> childList;
    private int listItemGroupLayoutId;
    private int variableGroupId;//布局文件里面的variable 的name
    private int listItemChildLayoutId;
    private int variableChildId;//布局文件里面的variable 的name

    private ItemCallBack callBack;
    private int topViewLayoutId = -1;

    public ExpandListViewDBAdapter(Context context, List<G> groupList, List<List<C>> childList,
                                   int listItemGroupLayoutId, int variableGroupId,
                                   int listItemChildLayoutId, int variableChildId) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        this.listItemGroupLayoutId = listItemGroupLayoutId;
        this.variableGroupId = variableGroupId;
        this.listItemChildLayoutId = listItemChildLayoutId;
        this.variableChildId = variableChildId;
    }

    @Override
    public int getGroupCount() {
        if (groupList == null)
            return 0;
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childList == null || childList.size() == 0 || childList.get(groupPosition) == null)
            return 0;
        if (topViewLayoutId != -1)
            return childList.get(groupPosition).size() + 1;
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupList == null || groupPosition < 0)
            return null;
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (childList == null || childList.size() == 0 || childList.get(groupPosition) == null
                || groupPosition < 0 || childPosition < 0)
            return null;
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), listItemGroupLayoutId, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableGroupId, groupList.get(groupPosition));
        binding.setVariable(BR.isExpandGroupOpen, isExpanded);

        return binding.getRoot();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewDataBinding binding = null;

        if (convertView == null) {
            if (childPosition == 0 && topViewLayoutId != -1) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), topViewLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, TOP);
            } else {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), listItemChildLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, BASE);
            }
        } else {
            if (childPosition == 0 && topViewLayoutId != -1) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), topViewLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, TOP);
            } else {
                if (convertView.getTag(R.id.list_item_type).equals(BASE)) {
                    binding = DataBindingUtil.getBinding(convertView);
                    convertView = binding.getRoot();
                } else {
                    binding = DataBindingUtil.inflate(LayoutInflater.from(context), listItemChildLayoutId, parent, false);
                    convertView = binding.getRoot();
                    convertView.setTag(R.id.list_item_type, BASE);
                }
            }
        }

        if (topViewLayoutId == -1)
            binding.setVariable(variableChildId, childList.get(groupPosition).get(childPosition));
        else if (topViewLayoutId != -1 && childPosition >= 1)
            binding.setVariable(variableChildId, childList.get(groupPosition).get(childPosition - 1));
//        binding.setVariable(BR.groupPosition, groupPosition);
//        binding.setVariable(BR.childPosition, childPosition);

        if (callBack != null && convertView != null)
            callBack.item(convertView, groupPosition, childPosition);

        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addChildTopView(int topViewLayoutId) {
        this.topViewLayoutId = topViewLayoutId;
    }

    public void setCallBack(ItemCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ItemCallBack {
        void item(View rootView, int groupPosition, int childPosition);
    }
}
