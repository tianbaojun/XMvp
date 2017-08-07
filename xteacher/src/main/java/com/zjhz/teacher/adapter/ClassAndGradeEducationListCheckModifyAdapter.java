package com.zjhz.teacher.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.AddBean;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
import com.zjhz.teacher.ui.activity.ClassAndGradeEducationListCheckModifyActivity;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ClassAndGradeEducationListCheckModifyAdapter extends BaseAdapter {

    private ClassAndGradeEducationListCheckModifyActivity activity;
    private List<ClassAndGradeEducationListCheckBean> lists;
    public List<AddBean> items = new ArrayList<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        items.clear();
        if (lists.size() > 0){
            for (int i = 0 ; i < lists.size() ; i ++){
                final AddBean addBean = new AddBean();
                addBean.classId = lists.get(i).classId;
                addBean.schemeId = activity.subjectId;
                addBean.checkTime = activity.checkTime;
                addBean.inspector = lists.get(i).inspector;
                addBean.score = lists.get(i).score;
                addBean.moralIdsWithScore = lists.get(i).moralId + "@@" + lists.get(i).score;
                items.add(addBean);
            }
        }
    }

    public ClassAndGradeEducationListCheckModifyAdapter(ClassAndGradeEducationListCheckModifyActivity activity, List<ClassAndGradeEducationListCheckBean> lists) {
        this.activity = activity;
        this.lists = lists;
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
        final ClassAndGradeEducationListCheckBean bean = lists.get(i);
        view = LayoutInflater.from(activity).inflate(R.layout.adapter_classandcrade_education_check, viewGroup, false);
        TextView clazz = (TextView) view.findViewById(R.id.adapter_classandcrade_education_check_clazz);
        TextView project = (TextView) view.findViewById(R.id.adapter_classandcrade_education_check_project);
        final ImageView image = (ImageView) view.findViewById(R.id.adapter_classandcrade_education_check_score);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.adapter_classandcrade_education_check_rl);
        final EditText editText = (EditText) view.findViewById(R.id.adapter_classandcrade_education_check_editText);
        LinearLayout add_score_ll = (LinearLayout) view.findViewById(R.id.add_score_ll);
        TextView minScore_tv = (TextView) view.findViewById(R.id.minScore_tv);
        TextView maxScore_tv = (TextView) view.findViewById(R.id.maxScore_tv);

        clazz.setText(bean.name);
        project.setText(bean.moralName);

        if (bean.meterMode == 0) {
            add_score_ll.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            if (bean.scoreMode == 0){
                if (items.get(i).score.equals("0")){
                    lists.get(i).isChange = false;
                    image.setImageResource(R.mipmap.edu_check_ok);
                }else {
                    lists.get(i).isChange = true;
                    image.setImageResource(R.mipmap.edu_check_error);
                }
            }else {
                if (items.get(i).score.equals("0")){
                    lists.get(i).isChange = true;
                    image.setImageResource(R.mipmap.edu_check_error);
                }else {
                    lists.get(i).isChange = false;
                   image.setImageResource(R.mipmap.edu_check_ok);
                }
            }
        }else if (bean.meterMode == 1) {
            add_score_ll.setVisibility(View.VISIBLE);
            minScore_tv.setText(bean.min+"<=");
            maxScore_tv.setText("<="+bean.max);
            editText.setText(SharePreCache.isEmpty(bean.score)?"0":bean.score);
            relativeLayout.setVisibility(View.GONE);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().trim();
                if (!TextUtils.isEmpty(s1)){
                    if (bean.scoreMode == 0){
                        updata0(s1, editText, i , bean);
                    }else if (bean.scoreMode == 1){
                        updata1(s1, editText, i , bean);
                    }
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lists.get(i).isChange){
                    lists.get(i).isChange = false;
                    image.setImageResource(R.mipmap.edu_check_ok);
                    if (bean.scoreMode == 0 ){
                        items.get(i).score = 0+"";
                        items.get(i).moralIdsWithScore = lists.get(i).moralId + "@@" +0;
                    }else {
                        items.get(i).score = lists.get(i).max+"";
                        items.get(i).moralIdsWithScore = lists.get(i).moralId + "@@" + lists.get(i).max;
                    }
                }else {
                    lists.get(i).isChange = true;
                    image.setImageResource(R.mipmap.edu_check_error);
                    if (bean.scoreMode == 0 ){
                        items.get(i).score = lists.get(i).min;
                        items.get(i).moralIdsWithScore = lists.get(i).moralId + "@@" + lists.get(i).min;
                    }else {
                        items.get(i).score = 0+"";
                        items.get(i).moralIdsWithScore = lists.get(i).moralId + "@@" +  0;
                    }
                }
            }
        });
        return view;
    }

    private void updata1(String s1, EditText editText, int j, ClassAndGradeEducationListCheckBean bean) {
        if (!s1.contains("-")){
            if (s1.contains(".")){
                if (!s1.substring(0,1).equals(".")){
                    int count = 0;
                    for (int i = 0; i < s1.length();i++){
                        if (s1.charAt(i)=='.'){
                            count++;
                        }
                    }
                    if (count <= 1){
                        try {
                            float f = Float.parseFloat(s1);
                            float max = Float.parseFloat(bean.meterRange);
                            if (f >= 0 && f <= max){
                                lists.get(j).score = s1;
                                items.get(j).score = s1;
                                items.get(j).moralIdsWithScore = lists.get(j).moralId + "@@" + s1;
                                SharedPreferencesUtils.putSharePrefInteger("fly_int",j);
                                LogUtil.e("修改后的分数 = ",lists.get(j).score + "  j = " +j);
                            }else {
                                editText.setText("");
                                ToastUtils.showShort("请输入区间值");
                            }
                        }catch (Exception E){
                            editText.setText("");
                            ToastUtils.showShort("请输入区间值");
                        }
                    }else {
                        editText.setText("");
                        ToastUtils.showShort("请输入区间值");
                    }
                }else {
                    editText.setText("");
                    ToastUtils.showShort("请输入区间值");
                }
            }else {
                try {
                    float f = Float.parseFloat(s1);
                    float max = Float.parseFloat(bean.meterRange);
                    if (f >= 0 && f <= max){
                        lists.get(j).score = s1;
                        items.get(j).score = s1;
                        items.get(j).moralIdsWithScore = lists.get(j).moralId + "@@" + s1;
                        SharedPreferencesUtils.putSharePrefInteger("fly_int",j);
                        LogUtil.e("修改后的分数 = ",lists.get(j).score + "  j = " +j);
                    }else {
                        editText.setText("");
                        ToastUtils.showShort("请输入区间值");
                    }
                }catch (Exception E){
                    editText.setText("");
                    ToastUtils.showShort("请输入区间值");
                }
            }
        }else {
            editText.setText("");
            ToastUtils.showShort("请输入区间值");
        }
    }

    private void updata0(String s1,EditText editText,int j ,ClassAndGradeEducationListCheckBean bean) {
        if (s1.contains("-") && s1.substring(0,1).equals("-")){
            int count = 0;
            int ponitCount = 0;
            for (int i = 0; i < s1.length();i++){
                if (s1.charAt(i)=='-'){
                    count++;
                }
                if (s1.charAt(i)=='.'){
                    ponitCount++;
                }
            }
            if (count <= 1 && ponitCount <=1){
                if (s1.length() >= 2 && !s1.substring(1,2).equals(".")){
                    if (s1.length() >= 3 && !s1.substring(2,3).equals(".")){
                        editText.setText("");
                        ToastUtils.showShort("请输入区间值");
                    }else {
                        try {
                            float f = Float.parseFloat(s1.substring(1,s1.length()));
                            float max = Float.parseFloat(bean.meterRange);
                            if (f >= 0 && f <= max){
                                if (s1.length() >= 2 && s1.length() <= 3 && s1.substring(1,2).equals("0")){
                                    lists.get(j).score = "0";
                                    items.get(j).score = "0";
                                    items.get(j).moralIdsWithScore = lists.get(j).moralId + "@@" + "0";
                                }else {
                                    lists.get(j).score = s1;
                                    items.get(j).score = s1;
                                    items.get(j).moralIdsWithScore = lists.get(j).moralId + "@@" + s1;
                                }
                                SharedPreferencesUtils.putSharePrefInteger("fly_int",j);
                                LogUtil.e("修改后的分数 = ",lists.get(j).score + "  j = " +j);
                            }else {
                                editText.setText("");
                                ToastUtils.showShort("请输入区间值");
                            }
                        }catch (Exception E){
                            editText.setText("");
                            ToastUtils.showShort("请输入区间值");
                        }
                    }
                }else if (s1.length() != 1){
                    editText.setText("");
                    ToastUtils.showShort("请输入区间值");
                }
            }else {
                editText.setText("");
                ToastUtils.showShort("请输入区间值");
            }
        }else {
            if (s1.substring(0,1).equals("0") && s1.length() == 1 ){
                lists.get(j).score = s1;
                items.get(j).score = s1;
                items.get(j).moralIdsWithScore = lists.get(j).moralId + "@@" + s1;
                SharedPreferencesUtils.putSharePrefInteger("fly_int",j);
            }else {
                editText.setText("");
                ToastUtils.showShort("请输入区间值");
            }
        }
    }

//    class ViewHolder{
//        TextView clazz,project;
//        RelativeLayout relativeLayout;
//        ImageView image;
//        EditText editText;
//    }
}
