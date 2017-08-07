package pro.ui.activity.xkgl.fragment.rk;


import android.os.Bundle;
import android.util.TypedValue;
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
import com.zjhz.teacher.utils.SharePreCache;
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
import pro.ui.activity.xkgl.activity.MineSelectionActivity;
import pro.ui.activity.xkgl.testbean.ScoreRecordbean;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class RecordFragment extends BaseFragment{

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
    private ScoreRecordbean records = new ScoreRecordbean();
    //出勤类型
    private List<String> types = new ArrayList<>();

    private String optId = "";

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine_call, null);
    }

    @Override
    protected void initViewsAndEvents() {
        optId = getArguments().getString("optId");
//        refreshLayout.setOnLoadListener(this);
//        refreshLayout.setOnRefreshListener(this);
        getRecord();
        firstAdapter = new CommonAdapter<ScoreRecordbean.StuList>(getContext(),R.layout.activity_class_selection_mine_call_list_item,records.getStuList()) {
            @Override
            protected void convert(final ViewHolder viewHolder, final ScoreRecordbean.StuList item, final int postionF) {
                GlideUtil.loadImageHead(item.getPhotoUrl(), (ImageView)viewHolder.getView(R.id.header_img));
                viewHolder.setText(R.id.name_tv,item.getName());
                final ScrollViewWithGridView grid = viewHolder.getView(R.id.call_grid);
                grid.setNumColumns(types.size());
                grid.setAdapter(new CommonAdapter<String>(getContext(),R.layout.textview,types) {
                    @Override
                    protected void convert(ViewHolder viewHolder, String item2, int position) {
                        if(types.size() > 3)
                            ((TextView)viewHolder.getView(R.id.text_tv)).setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.sp_12));
                        viewHolder.setTextColor(R.id.text_tv, getResources().getColor(R.color.gray3));
                        viewHolder.setText(R.id.text_tv, item2);
                        if (String.valueOf(position).equals(item.getLevel())) {
                            viewHolder.setBackgroundColor(R.id.text_tv, getResources().getColor(R.color.title_background_red));
//                            ViewTools.tvAppendImage((TextView) viewHolder.getView(R.id.text_tv),types.get(position),R.mipmap.check_rec);
                        }
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(int i=0;i<types.size();i++) {
                            TextView tv = (TextView)grid.getChildAt(i);
                            if(tv == view){
                                tv.setBackgroundColor(getResources().getColor(R.color.title_background_red));
//                                ViewTools.tvAppendImage(tv,types.get(position),R.mipmap.check_rec);
//                                item.setName( String.valueOf(position));
                                records.getStuList().get(postionF).setLevel(position +"");
                            }else{
                                tv.setBackgroundColor(getResources().getColor(R.color.white));
                                tv.setText(types.get(i));
                            }
                        }
                    }
                });
            }
        };
        listView.setAdapter(firstAdapter);
    }

    //成绩
    private void getRecord(){
        Map<String,String> map = new HashMap<>();
        map.put("optId", optId);
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.SCORElIST, Config.SCORElIST);
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
                    case Config.SCORElISTSUB:
                        ToastUtils.showShort("提交失败");
                        break;
                }
                break;
            case MineSelectionActivity.COURSECHANGE:
                optId = eventCenter.getData().toString();
                getRecord();
                break;
            case Config.SCORElIST:
                records.getStuList().clear();
                ScoreRecordbean bean = GsonUtils.toObject((JSONObject) eventCenter.getData(),ScoreRecordbean.class);
                if(bean!=null){
                    records.getStuList().addAll(bean.getStuList());
                    records.setCommentLevel(bean.getCommentLevel());
                    //// TODO: 2017/7/12 根据状态隐藏提交按钮
                    if(records != null && records.getStatus() != null) {
                        switch (records.getStatus()) {
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
                    initType();
                    firstAdapter.notifyDataSetChanged();
                }
                break;
            case Config.SCORElISTSUB:
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
        for (ScoreRecordbean.StuList bean : records.getStuList()) {
            list.add(new stusub(bean.getLevel(), bean.getStudentId()));
        }
        map.put("stuList", list);
        NetworkRequest.request(map, CommonUrl.SCORElISTSUB,Config.SCORElISTSUB);
    }

    private void initType(){
        if(records == null || records.getCommentLevel() == null)
            return;
        types.clear();
        if(!SharePreCache.isEmpty(records.getCommentLevel().getLevel1())){
            types.add(records.getCommentLevel().getLevel1());
        }
        if(!SharePreCache.isEmpty(records.getCommentLevel().getLevel2())){
            types.add(records.getCommentLevel().getLevel2());
        }
        if(!SharePreCache.isEmpty(records.getCommentLevel().getLevel3())){
            types.add(records.getCommentLevel().getLevel3());
        }
        if(!SharePreCache.isEmpty(records.getCommentLevel().getLevel4())){
            types.add(records.getCommentLevel().getLevel4());
        }
        if(!SharePreCache.isEmpty(records.getCommentLevel().getLevel5())){
            types.add(records.getCommentLevel().getLevel5());
        }
    }

    class stusub implements Serializable{
        long studentId;
        String level;

        public stusub(String level, long studentId) {
            this.level = level;
            this.studentId = studentId;
        }
    }

}
