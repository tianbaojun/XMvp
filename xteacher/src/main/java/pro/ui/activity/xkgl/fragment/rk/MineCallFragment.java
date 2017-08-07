package pro.ui.activity.xkgl.fragment.rk;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pro.kit.ViewTools;
import pro.ui.activity.xkgl.activity.MineSelectionActivity;
import pro.ui.activity.xkgl.testbean.RollCallbean;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class MineCallFragment extends BaseFragment{

//    @BindView(R.id.refresh_layout)
//    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_listview)
    ListView listView;
    @BindView(R.id.submit)
    Button submitBtn;

    //第一层
    private CommonAdapter firstAdapter;
    //第二层
    private CommonAdapter secondAdapter;
    //学生点名数据
    private RollCallbean mineCalls = new RollCallbean();
    //出勤类型
    private List<String> types = new ArrayList<>();

    private String optId = "";

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine_call, null);
    }

    @Override
    protected void initViewsAndEvents() {
        initType();
        optId = getArguments().getString("optId");
//        refreshLayout.setOnLoadListener(this);
//        refreshLayout.setOnRefreshListener(this);
        getRollCall();
        firstAdapter = new CommonAdapter<RollCallbean.SutList>(getContext(),R.layout.activity_class_selection_mine_call_list_item,mineCalls.getStuList()) {
            @Override
            protected void convert(ViewHolder viewHolder, final RollCallbean.SutList item, final int postionF) {
                GlideUtil.loadImageHead(item.getPhotoUrl(), (ImageView)viewHolder.getView(R.id.header_img));
                viewHolder.setText(R.id.name_tv,item.getName());
                final ScrollViewWithGridView grid = viewHolder.getView(R.id.call_grid);
                grid.setNumColumns(types.size());
                grid.setAdapter(new CommonAdapter<String>(getContext(),R.layout.textview,types) {
                    @Override
                    protected void convert(ViewHolder viewHolder, String item2, int position) {
                        viewHolder.setTextColor(R.id.text_tv, getResources().getColor(R.color.gray3));
                        viewHolder.setText(R.id.text_tv, item2);
                        if (String.valueOf(position).equals(item.getStatus())) {
                            ViewTools.tvAppendImage((TextView) viewHolder.getView(R.id.text_tv),types.get(position),R.mipmap.check_rec);
                        }
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(int i=0;i<types.size();i++) {
                            TextView tv = (TextView)grid.getChildAt(i);
                            if(tv == view){
                                ViewTools.tvAppendImage(tv,types.get(position),R.mipmap.check_rec);
//                                item.setName( String.valueOf(position));
                                mineCalls.getStuList().get(postionF).setStatus(position+"");
                            }else{
                                tv.setText(types.get(i));
                            }
                        }
                    }
                });
            }
        };
        listView.setAdapter(firstAdapter);
    }

    //点名
    private void getRollCall(){
        Map<String,String> map = new HashMap<>();
        map.put("optId", optId);
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.ROLLCALLlIST, Config.ROLLCALLlIST);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    @Override
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        super.onEventMainThread(eventCenter);
        switch (eventCenter.getEventCode()) {
            case Config.NOSUCCESS:
                String fromCode = ((JSONObject) eventCenter.getData()).optString(NetworkRequest.FROMCODE);
                switch (fromCode) {
                    case Config.ROLLCALLlISTSUB:
                        ToastUtils.showShort("提交失败");
                        break;
                }
                break;
            case MineSelectionActivity.COURSECHANGE:
                optId = eventCenter.getData().toString();
                getRollCall();
                break;
            case Config.ROLLCALLlIST:
                mineCalls.getStuList().clear();
                RollCallbean bean = GsonUtils.toObject((JSONObject) eventCenter.getData(),RollCallbean.class);
                if(bean!=null){
                    mineCalls.getStuList().addAll(bean.getStuList());
                    firstAdapter.notifyDataSetChanged();
                    if(mineCalls != null && mineCalls.getStatus() != null) {
                        switch (mineCalls.getStatus()) {
                            case "0":
                                submitBtn.setText("提交");
                                break;
                            case "1":
                                submitBtn.setVisibility(View.GONE);
                                break;
                            case "2":
                                submitBtn.setText("再次提交");
                                break;
                        }
                    }
                }
                break;
            case Config.ROLLCALLlISTSUB:
                ToastUtils.showShort("提交成功");
                break;
        }
    }

    @OnClick({R.id.submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                submit();
                break;
        }
    }

    private void submit() {
        Map<String,Object> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        map.put("optId", optId);
        List<stusub> list = new ArrayList<>();
        for (RollCallbean.SutList bean : mineCalls.getStuList()) {
            list.add(new stusub(bean.getStatus(), bean.getStudentId()));
        }
        map.put("stuList", list);
        NetworkRequest.request(map, CommonUrl.ROLLCALLlISTSUB,Config.ROLLCALLlISTSUB);
    }

    private void initType(){
        types.add("出勤");
        types.add("迟到");
        types.add("缺勤");
    }

    class stusub implements Serializable{
        long studentId;
        String status;

        public stusub(String status, long studentId) {
            this.status = status;
            this.studentId = studentId;
        }
    }

}
