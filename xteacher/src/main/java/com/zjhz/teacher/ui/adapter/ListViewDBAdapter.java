package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzd on 2017/6/4.
 */

public class ListViewDBAdapter<E> extends BaseAdapter {

    private final static String BASE = "base";
    private final static String FOOT = "foot";

    private int footLayoutId = -1;
    private int footVariableId;
    private List<E> footList = new ArrayList<>();
    private ItemCallBack callBack;
    private Context context;
    private int listItemLayoutId;
    private List<E> dataList;
    private int variableId;//布局文件里面的variable 的name
    private List<Variable> variableList = new ArrayList<>();
    private List<Variable> variableLists = new ArrayList<>();

    public ListViewDBAdapter(Context context, List<E> dataList, int listItemLayoutId, int variableId) {
        this.context = context;
        this.dataList = dataList;
        this.listItemLayoutId = listItemLayoutId;
        this.variableId = variableId;
    }

    @Override
    public int getCount() {
        if(dataList != null && footList != null)
            return dataList.size()+footList.size();
        else if(dataList != null)
            return dataList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;

        if (convertView == null) {
            if(position >= getCount() - 1 && footLayoutId != -1) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), footLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, FOOT);
            }
            else {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), listItemLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, BASE);
            }
        } else {
            if(position >= getCount() - 1 && footLayoutId != -1) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), footLayoutId, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(R.id.list_item_type, FOOT);
            }
            else {
                if(convertView.getTag(R.id.list_item_type).equals(BASE)) {
                    binding = DataBindingUtil.getBinding(convertView);
                    convertView = binding.getRoot();
                }
                else {
                    binding = DataBindingUtil.inflate(LayoutInflater.from(context), listItemLayoutId, parent, false);
                    convertView = binding.getRoot();
                    convertView.setTag(R.id.list_item_type, BASE);
                }
            }
        }

        if (position >= getCount() - footList.size() && footLayoutId != -1 && footList != null) {
            binding.setVariable(footVariableId, footList.get(position-dataList.size()));
        }else {
//            binding.setVariable(BR.position, position);
            binding.setVariable(variableId, dataList.get(position));
            for (Variable variable : variableList) {
                binding.setVariable(variable.variableId, variable.value);
            }

            for (Variable variable : variableLists) {
                if (variable.list != null && variable.list.size() > position)
                    binding.setVariable(variable.variableId, variable.list.get(position));
            }

            if (callBack != null && convertView != null)
                callBack.item(convertView, position);
        }

        return convertView;
    }


    /**
     * 必须在ListView.setAdapter()之后调用
     *
     * @param footLayoutId
     * @param footVariableId
     * @param list
     */
    public void addFootLayout(int footLayoutId, int footVariableId, List<E> list) {
        this.footLayoutId = footLayoutId;
        if (list != null)
            this.footList = list;
        this.footVariableId = footVariableId;
        this.notifyDataSetChanged();
    }

    /**
     * 必须在ListView.setAdapter()之后调用
     *
     * @param footLayoutId
     * @param footVariableId
     * @param entity
     */
    public void addFootLayout(int footLayoutId, int footVariableId, E entity) {
        this.footLayoutId = footLayoutId;
        this.footList.add(entity);
        this.footVariableId = footVariableId;
        this.notifyDataSetChanged();
    }

    public void setItemCallBack(ItemCallBack callBack){
        this.callBack = callBack;
    }

    public void addVariable(int variableId, Object value) {
        Variable variable = new Variable(variableId, value);
        variableList.add(variable);
    }

    public void addListVariable(int variableId, List list) {
        Variable variable = new Variable(variableId, list);
        variableLists.add(variable);
    }

//    public void setDataAddNotify(List<E> dataList,List footList){
//        this.dataList = dataList;
//        this.footList = footList;
//        this.notifyDataSetChanged();
//    }

    public interface ItemCallBack {
        void item(View rootView, int position);
    }

    public class Variable {
        int variableId;
        Object value;
        List list;

        public Variable(int variableId, Object value) {
            this.variableId = variableId;
            this.value = value;
        }

        public Variable(int variableId, List list) {
            this.variableId = variableId;
            this.list = list;
        }
    }
}
