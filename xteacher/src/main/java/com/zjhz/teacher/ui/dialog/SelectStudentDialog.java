package com.zjhz.teacher.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.SelectStudentDialogAdapter;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.PersonMoralEducationStudentNumberBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-26
 * Time: 15:57
 * Description: 选择学生弹窗
 */
public class SelectStudentDialog extends Dialog implements View.OnClickListener,AdapterView.OnItemClickListener,SelectStudentDialogAdapter.OnMyCheckChangedListener {

    private Window window = null;
    private Context context;
    private ListView listView;
    private TextView cancel,title,ok;
    private SelectStudentDialogAdapter adapter;

    public SelectStudentDialog(Context context,List<PersonMoralEducationStudentNumberBean>  lists) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog(lists);
    }

    public void showDialog(List<PersonMoralEducationStudentNumberBean>  lists) {
        setContentView(R.layout.dialog_teacher_select);
        windowDeploy();
        setCanceledOnTouchOutside(false);
        cancel = (TextView) findViewById(R.id.title_text_left);
        title = (TextView) findViewById(R.id.title_text_center);
        ok = (TextView) findViewById(R.id.title_right_text);
        listView = (ListView) findViewById(R.id.dialog_teacher_select_list);
        listView.setOnItemClickListener(this);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new SelectStudentDialogAdapter(context,lists);
        adapter.setOncheckChanged(this);
        listView.setAdapter(adapter);
        cancel.setText("取消");
        title.setText("请选择学生");
        ok.setText("确认");
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        show();
    }

    public void windowDeploy() {
        window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_text_left:
                break;
            case R.id.title_right_text:
                if (!TextUtils.isEmpty(position)) {
                    EventBus.getDefault().post(new EventCenter<String>(Config.STUDENT_EDUCATION,position));
                }else{
                    EventBus.getDefault().post(new EventCenter<String>(Config.STUDENT_EDUCATION,"0"));
                }
                break;
        }
        dismiss();
    }

    public String getName(){
        return string;
    }

    String string,position;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView text = (TextView) view.findViewById(R.id.adapter_select_teacher_dialog_text);
        string = text.getText().toString().trim();
        position = i + "";
        adapter.setSelectID(i);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectID(int selectID) {
        adapter.setSelectID(selectID);
        adapter.notifyDataSetChanged();
    }
}
