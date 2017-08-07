package pro.ui.activity.xkgl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import pro.ui.activity.xkgl.testbean.ZHbean;

public class ZoneHeDtlActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listView;


    List<Bean> list = new ArrayList<>();
    private CommonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_only);
        Intent intent = getIntent();
        ZHbean item = (ZHbean) intent.getSerializableExtra("item");
        getDtl(item.getOptId());
//        makeDtlList(list,item);
        setMyTitle("选课管理");
        adapter = new CommonAdapter<Bean>(this, R.layout.left_right_text_item,list) {
            @Override
            protected void convert(ViewHolder viewHolder, Bean item, int position) {
                viewHolder.setText(R.id.left, item.left);
                viewHolder.setText(R.id.right, item.right);
            }
        };
        listView.setAdapter(adapter);
    }

    private void getDtl(String id){
        Map<String,String> map = new HashMap<>();
        map.put("optId", id);
        map.put("__curentUser", SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey));
        map.put("__curentSchool", SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey));
        NetworkRequest.request(map, CommonUrl.COURSE_DTL_ADMIN, Config.COURSE_DTL_ADMIN);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    @Override
    public void onEventMainThread(EventCenter event) {
        super.onEventMainThread(event);
        switch (event.getEventCode()) {
            case Config.COURSE_DTL_ADMIN:
                ZHbean bean = GsonUtils.toObject((JSONObject) event.getData(),ZHbean.class);
                makeDtlList(list,bean);
                adapter.notifyDataSetChanged();
                break;
        }

    }

    private void makeDtlList(List<Bean> list, ZHbean bean){
        list.clear();
        if(bean!=null) {
            list.add(new Bean("课程编号", bean.getCourseNo()));
            list.add(new Bean("科目", bean.getSubjectName()));
            list.add(new Bean("任课教师", bean.getTeacherName()));
            list.add(new Bean("辅助教师", bean.getSupName()));
            list.add(new Bean("上课地点", bean.getOptAddr()));
            list.add(new Bean("每班人数上限", String.valueOf(bean.getLimMax())));
            list.add(new Bean("课程人数上限", String.valueOf(bean.getStuMax())));
            list.add(new Bean("课程人数下限", String.valueOf(bean.getStuMin())));
            list.add(new Bean("科目属性", bean.getPropName()));
            list.add(new Bean("科目类名", bean.getTypeName()));
            list.add(new Bean("开课年级", bean.getGradesName()));
            list.add(new Bean("已选课学生", String.valueOf(bean.getStuNum())));
        }
    }

    class Bean {
        String left,right;

        public Bean(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}
