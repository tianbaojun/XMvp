package com.zjhz.teacher.ui.fragment.homework;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.databinding.FragmentHomeworkDetailBinding;
import com.zjhz.teacher.ui.activity.homework.HomeworkManageDetailActivity;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.widget.NineGridLayout.NineGridlayout;

public class HomeworkDetailFragment extends BaseFragment{
    private static final String HOMEWORK_ID = "homework_id";
    private static final String TEACHER_ID = "teacher_id";

    private FragmentHomeworkDetailBinding binding;

    private CircleImageView header;

    private TextView ffbTv;

    public HomeworkDetailFragment(){

    }

    public static HomeworkDetailFragment newInstance(String homeworkId, String teacherId){
        HomeworkDetailFragment fragment = new HomeworkDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HOMEWORK_ID, homeworkId);
        bundle.putString(TEACHER_ID, teacherId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_homework_detail, null, false);
        ffbTv = (TextView) binding.getRoot().findViewById(R.id.homework_ffb);
        header = (CircleImageView)binding.getRoot().findViewById(R.id.homework_teacher_circleImageView);

        if(getArguments().getString(TEACHER_ID) != null &&
                (getArguments().getString(TEACHER_ID).equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey)))){
            ffbTv.setVisibility(View.VISIBLE);
        }else{
            ffbTv.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    @Override
    protected void initViewsAndEvents() {
        ffbTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("homeworkId", getArguments().getString(HOMEWORK_ID));
                map.put("status", "1");//状态 1待发布 2 已发布 3 已完结
                NetworkRequest.request(map, CommonUrl.HOMEWORK_MODIFY_STATUS, "homework_ffb");
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void lazyLoad() {
        Map<String, String> map = new HashMap<>();
        map.put("homeworkId", getArguments().getString(HOMEWORK_ID));
        NetworkRequest.request(map, CommonUrl.HOMEWORK_PUBLISH_DETAIL, "detail");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        switch (eventCenter.getEventCode()){
            case Config.NOSUCCESS:
                if(((JSONObject)eventCenter.getData()).optString(NetworkRequest.FROMCODE).equals("homework_ffb")){
                    ToastUtils.showShort(((JSONObject)eventCenter.getData()).optString("msg"));
                }
                break;
            case "detail":
                HomeWorkBean bean = GsonUtils.toObject((JSONObject) eventCenter.getData(), HomeWorkBean.class);
                if(bean != null) {
                    GlideUtil.loadImageHead(bean.getPhotoUrl(), header);
                    showPic((NineGridlayout)binding.getRoot().findViewById(R.id.pic_grid), bean);
                    if(getActivity() instanceof HomeworkManageDetailActivity){
                        ((HomeworkManageDetailActivity)getActivity()).setClassList(bean.getLink());
                    }
                }
                binding.setHomeWorkBean(bean);
                break;
            case "homework_ffb":
                ToastUtils.showShort("反发布成功");
                getActivity().setResult(HomeworkManageDetailActivity.DFB);
                getActivity().finish();
                break;
        }
    }

    private void showPic(NineGridlayout pic_grid, HomeWorkBean homeWorkBean) {
        List<ImageBean> picUrls = homeWorkBean.getImgs();
        final List<String> urlList = new ArrayList<>();
        if(picUrls!=null) {
            for (ImageBean imageBean : picUrls) {
                urlList.add(imageBean.getDocPath());
            }
        }else{
            pic_grid.setVisibility(View.GONE);
        }
        pic_grid.setImagesData(urlList);
        pic_grid.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view) {
                String[] urls = new String[urlList.size()];
                for (int i = 0; i < urlList.size(); i++) {
                    urls[i] = urlList.get(i);
                }
                ImageGalleryActivity.show(view.getContext(), urls, position, false);
            }
        });
    }
}
