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
import com.zjhz.teacher.adapter.SelectTeacherDialogAdapter;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.TipsayBean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-3
 * Time: 15:57
 * Description: 选择代课教师弹窗
 */
public class SelectTeacherDialog extends Dialog implements View.OnClickListener,AdapterView.OnItemClickListener,SelectTeacherDialogAdapter.OnMyCheckChangedListener {

    private Window window = null;
    private Context context;
    private int type;
    private ListView listView;
    private TextView cancel,title,ok;
    private SelectTeacherDialogAdapter adapter;
    List<TipsayBean>  lists;
    private int groupChild = 0;//标记是修改的第几个代课老师
    private String linkId;

    /**
     * @param context 上下文
     * @param type 1代表驳回界面
     */
    public SelectTeacherDialog(Context context,int type,List<TipsayBean>  lists) {
        super(context);
        this.context = context;
        this.type = type;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        showDialog(lists);
    }

    public void showDialog(List<TipsayBean>  lists) {
        setContentView(R.layout.dialog_teacher_select);
        windowDeploy();
        setCanceledOnTouchOutside(false);
        this.lists = lists;
        if(lists != null && lists.size() > 0)
            string = lists.get(0).getName();
        cancel = (TextView) findViewById(R.id.title_text_left);
        title = (TextView) findViewById(R.id.title_text_center);
        ok = (TextView) findViewById(R.id.title_right_text);
        listView = (ListView) findViewById(R.id.dialog_teacher_select_list);
        listView.setOnItemClickListener(this);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new SelectTeacherDialogAdapter(context,lists);
        adapter.setOncheckChanged(this);
        listView.setAdapter(adapter);
        cancel.setText("取消");
        title.setText("请添加代课老师");
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
                if (!TextUtils.isEmpty(string) && lists != null && lists.size() > 0) {
                    if (1 == type) { // 修改
                        EventBus.getDefault().post(new EventCenter<String>(Config.TEACHER,string));
                    }else if(groupChild != 0){
                        Map<String, String> map = new HashMap<>();
                        map.put("position", groupChild+"");
                        map.put("value", string);
                        map.put("linkId",linkId);
                        EventBus.getDefault().post(new EventCenter<Map>(Config.TEACHERFLY, map));
                    } else{
                        EventBus.getDefault().post(new EventCenter<String>(Config.TEACHERFLY,string));
                    }
                }else{
                    if (1 == type) { // 修改
                        EventBus.getDefault().post(new EventCenter<String>(Config.TEACHER,lists.get(0).getName()));
                    }else{
                        EventBus.getDefault().post(new EventCenter<String>(Config.TEACHERFLY,lists.get(0).getName()));
                    }
                }
                break;
        }
        dismiss();
    }

    public String getName(){
        return string;
    }

    String string;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView text = (TextView) view.findViewById(R.id.adapter_select_teacher_dialog_text);
        string = text.getText().toString().trim();
        adapter.setSelectID(i);
        adapter.notifyDataSetChanged();
    }

    public void setGroupChild(int groupChild){
        this.groupChild = groupChild;
    }
    public void setLinkId(String linkId){
        this.linkId = linkId;
    }

    @Override
    public void setSelectID(int selectID) {
        adapter.setSelectID(selectID);
        adapter.notifyDataSetChanged();
    }
}
