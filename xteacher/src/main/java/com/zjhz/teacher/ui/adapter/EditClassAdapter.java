package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业管理的班级选择
 * Created by Administrator on 2016/6/20.
 */
public class EditClassAdapter extends BaseAdapter{
    private Context context;
    private List<ClassesBeans> classnames;
    private List<String> checked = new ArrayList<>();
    private List<ClassesBeans> isChecked = new ArrayList<>();
    public void setIsChecked(List<ClassesBeans> isChecked) {
        this.isChecked = isChecked;
    }
    public List<String> getChecked() {
        return checked;
    }
    public EditClassAdapter(Context context , List<ClassesBeans> classnames) {
        this.context = context;
        this.classnames = classnames;
    }
    @Override
    public int getCount() {
        return classnames.size();
    }
    @Override
    public Object getItem(int position) {
        return classnames.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.checkbox,null);
            viewHolder.class_cb = (CheckBox) convertView.findViewById(R.id.class_cb);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ClassesBeans beans = (ClassesBeans) getItem(position);
        if(beans != null && !beans.equals("null")){
            if (!SharePreCache.isEmpty(beans.getClassId())){
                if (isChecked.size() > 0){
                    boolean isRemove = false;
                    int index = -1;
                    for (int i = 0 ; i < isChecked.size() ; i ++){
                        if (isChecked.get(i) != null && !isChecked.get(i).equals("null")){
                            if (beans.getClassId().equals(isChecked.get(i).getClassId())){
                                isRemove = true;
                                index = i;
                                break;
                            }
                        }
                    }
                    if (isRemove){
                        if (!checked.contains(beans.getClassId())){
                            checked.add(beans.getClassId());
                        }
                        isChecked.remove(isChecked.get(index));
                        viewHolder.class_cb.setChecked(true);
                    }
                }
                viewHolder.class_cb.setText(beans.getName());
            }
        }
        viewHolder.class_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String classId = beans.getClassId();
                if (isChecked){
                    if (!checked.contains(classId)){
                        checked.add(classId);
                    }
                }else {
                    if (checked.contains(classId)){
                        checked.remove(classId);
                    }
                }
            }
        });
        return convertView;
    }
    class ViewHolder{
        CheckBox class_cb;
    }
}
