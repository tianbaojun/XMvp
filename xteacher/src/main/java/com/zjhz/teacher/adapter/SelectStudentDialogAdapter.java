package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.PersonMoralEducationStudentNumberBean;

import java.util.List;

/**
 * Created by fei.wang on 2016/8/11.
 */
public class SelectStudentDialogAdapter extends BaseAdapter {

    private Context context;
    private List<PersonMoralEducationStudentNumberBean> lists;

    public SelectStudentDialogAdapter(Context context, List<PersonMoralEducationStudentNumberBean> lists) {
        this.context = context;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_select_teacher_dialog, viewGroup, false);
        }
        TextView text = BaseViewHolder.get(view, R.id.adapter_select_teacher_dialog_text);
        text.setText(lists.get(position).studentName);
        RadioButton radioButton = BaseViewHolder.get(view, R.id.adapter_select_teacher_dialog_radio_btn);
        radioButton.setClickable(false);
        if (selectID == position) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }

//        radioButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                selectID = position;
//                if (mCheckChange != null)
//                    mCheckChange.setSelectID(selectID);
//            }
//        });
        return view;
    }

    private int selectID;

    private OnMyCheckChangedListener mCheckChange;

    public void setOncheckChanged(OnMyCheckChangedListener l) {
        mCheckChange = l;
    }

    public void setSelectID(int position) {
        selectID = position;
    }

    public interface OnMyCheckChangedListener {
        void setSelectID(int selectID);
    }
}
