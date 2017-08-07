package pro.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsComment;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMomentCommentBean;
import com.zjhz.teacher.bean.ClasszMomentType;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.adapter.SimpleFragmentPagerAdapter;
import pro.kit.ViewTools;
import pro.ui.fragment.ClasszMomentsAllFragment;
import pro.ui.fragment.ClasszMomentsClasszMomentsFragment;
import pro.ui.fragment.ClasszMomentsDocumentShareFragment;
import pro.ui.fragment.ClasszMomentsHonourRollFragment;
import pro.widget.BubblePopupWindow.BubblePopupWindow;
import pro.widget.BubblePopupWindow.ListViewAdaptWidth;

//import android.widget.PopupWindow;

public class ClasszMomentsActivity extends BaseActivity {

    private final static String TAG = EducationCountActivity2.class.getSimpleName();
    //班级动态
    private final String MOMENT = "1001";
    //资源共享
    private final String DOCUMENT = "1002";
    //班级红榜
    private final String HONOUR = "1003";
    //viewPager
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    //TabLayout
    @BindView(R.id.sliding_tabs)
    TabLayout sliding_tabs;
    //新建班级动态按钮
    @BindView(R.id.right_icon0)
    ImageView right_icon0;
    //查询按钮
    @BindView(R.id.right_icon)
    ImageView right_icon;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //评论的布局
    @BindView(R.id.comment_rl)
    RelativeLayout comment_rl;
    //评论编辑框
    @BindView(R.id.comment_et)
    EditText comment_et;
    //评论发送按钮
    @BindView(R.id.send_tv)
    TextView send_tv;
    //popupWindow的listView
    ListViewAdaptWidth popListView;/*
    //评论
    @BindView(R.id.input_view)
    ChatInputMenu inputMenu;*/
    //添加班级圈动态的弹窗
    private LayoutInflater inflater;
    //viewPager适配器
    private SimpleFragmentPagerAdapter pagerAdapter;
    //Fragment数组
    private List<Fragment> fragments = new ArrayList<>();
    //tabTitles fragment标题数组
    private List<String> tabTitles = new ArrayList<>();
    // 班级集合
    private List<ClassesBeans> classnames = new ArrayList<>();
    //当前选中的班级index
    private int index = 0;
    //班级动态类型
    private List<ClasszMomentType> classzMomentsTypes = new ArrayList<>();
    //被评论的动态的id
    private String momentDid;
    //评论的类型   0：直接评论该条动态    1：在评论列表中点击回复评论
    private int commentType;
    //在评论列表中点击回复评论   中被回复的评论
    private ClasszMomentCommentBean beRepCommentBean;
    private BubblePopupWindow popupWindow;
    //popupWindow适配器
    private ArrayAdapter<String> popAdapter;
    //判断当前是哪个fragment显示
    private String tag;

    private DefaultLoadingLayout loadingLayout;


   /* //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {

        InputMethodManager m = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments);
        ButterKnife.bind(this);
        initView();

        inflater = LayoutInflater.from(this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, viewPager);
        /*inputMenu.setChatInputMenuListener(new ChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                if (!TextUtils.isEmpty(content)) {
                    if(commentType == 0) {
                        ClasszMomentsComment commentParams = new ClasszMomentsComment(momentDid, content, "1", SharedPreferencesUtils.getSharePrefString("login_userid"));
                        NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT);
                    }else if(commentType == 1){  //回复
                        ClasszMomentsComment commentParams = new ClasszMomentsComment(beRepCommentBean.getdId(), content, "1", SharedPreferencesUtils.getSharePrefString("login_userid"));
                        commentParams.setBrepUserId(beRepCommentBean.getUserId());
                        commentParams.setParentId(beRepCommentBean.getDcId());
                        NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT);
                    }
                }
            }
        });*/
    }

    private void initView() {
        initTitleBar();
        initData();
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), tabTitles, this, fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        sliding_tabs.setupWithViewPager(viewPager);
        sliding_tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == 101) {
                    EventBus.getDefault().post(new EventCenter(Config.REFRESHLASTEST ));
                }
                break;
            case 103:
                if(resultCode == 1031){
                    initTag();
                    EventBus.getDefault().post(new EventCenter(Config.REFRESHLASTEST + tag));
                }
                break;
        }
    }

    /**
     * 给tag赋值
     */
    private void initTag() {
        int p = sliding_tabs.getSelectedTabPosition();
        LogUtil.e(String.valueOf(p));
        if (p == 0) {
            tag = "ALL";
        } else {
            switch (classzMomentsTypes.get(p - 1).getDcCode()) {
                case "1001":
                    tag = "SHARE";
                    break;
                case "1002":
                    tag = "DOC";
                    break;
                case "1003":
                    tag = "HONOUR";
                    break;
            }
        }
    }

    private void initData() {
        //请求动态类型
        NetworkRequest.request(null, CommonUrl.MOMENTTYPELIST, Config.MOMENTTYPELIST);
        //请求教师班级列表
        NetworkRequest.request(new CenterParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey), 1, 100), CommonUrl.homeworkClassesList, "classList");
        dialog.setMessage("正在请求...");
        dialog.show();
    }

    /**
     * 初始化Fragment和TabLayout标题
     * 在请求班级列表和动态类型列表成功之后调用
     * issue 导致tabLayout初始化很慢
     */
    private void initFragmentsAndTitles() {
        if (classzMomentsTypes.size() > 0 && classnames.size() > 0) {
            tabTitles.clear();
            fragments.clear();
            if (classzMomentsTypes.size() > 0) {
//                tabTitles = new String[classzMomentsTypes.size()+1];
                tabTitles.add(getResources().getString(R.string.classz_moments_activity_tab_title_all));
                Bundle bundle = new Bundle();
                bundle.putString("dcCode", "0");
//                fragments = new Fragment[classzMomentsTypes.size()+1];
                fragments.add(ClasszMomentsAllFragment.newInstance());
//                fragments[0].setArguments(bundle);
                for (int i = 0; i < classzMomentsTypes.size(); i++) {
                    if ("1001".equals(classzMomentsTypes.get(i).getDcCode())||
                            "1002".equals(classzMomentsTypes.get(i).getDcCode())||
                            "1003".equals(classzMomentsTypes.get(i).getDcCode())) {
                        tabTitles.add(classzMomentsTypes.get(i).getDcName());
                    }/*
                    Bundle dcCode = new Bundle();
                    dcCode.putString("dcCode",classzMomentsTypes.get(i).getDcCode());*/
                    switch (classzMomentsTypes.get(i).getDcCode()) {
                        case "1001":
                            fragments.add(ClasszMomentsClasszMomentsFragment.newInstance());
                            break;
                        case "1002":
                            fragments.add(ClasszMomentsDocumentShareFragment.newInstance());
                            break;
                        case "1003":
                            fragments.add(ClasszMomentsHonourRollFragment.newInstance());
                            break;
                    }
                }
            }
            pagerAdapter.changed();
//            viewPager = (ViewPager) findViewById(R.id.viewpager);
//            pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),tabTitles,this,fragments);
//            viewPager.setAdapter(pagerAdapter);
//            sliding_tabs = (TabLayout) findViewById(R.id.sliding_tabs);
//            sliding_tabs.setupWithViewPager(viewPager);
//            sliding_tabs.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    /**
     * 初始化标题 TitleBar
     */
    private void initTitleBar() {
        //设置添加安钮可见
        right_icon0.setVisibility(View.VISIBLE);
        //设置查询按钮
        right_icon.setImageResource(R.mipmap.search);
        //查询按钮可见
        right_icon.setVisibility(View.VISIBLE);
        //设置标题
        title_tv.setText(R.string.classz_moments_activity_title);
    }

    @OnClick({R.id.title_back_img, R.id.right_icon0, R.id.right_icon, R.id.title_tv, R.id.send_tv})
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.title_back_img:      //返回
                finish();
                break;
            case R.id.right_icon0:
                if (classzMomentsTypes.size() > 0) {
                    final String[] str = new String[classzMomentsTypes.size()];
                    for (int i = 0; i < classzMomentsTypes.size(); i++) {
                        str[i] = classzMomentsTypes.get(i).getDcName();
                    }
                    showPopupWindow(v, str, new ListView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ClasszMomentType type = classzMomentsTypes.get(position);
                            switch (type.getDcCode()) {
                                case MOMENT:
                                    Bundle shareBundle = new Bundle();
                                    shareBundle.putString("classId", classnames.get(index).getClassId());
                                    shareBundle.putString("dcId", classzMomentsTypes.get(position).getDcId());
//                                    startActivity(ClasszMomentsAddShareActivity.class,shareBundle);
                                    Intent intent = new Intent(ClasszMomentsActivity.this, ClasszMomentsAddShareActivity.class);
                                    intent.putExtras(shareBundle);
                                    startActivityForResult(intent, 100);
                                    break;
                                case DOCUMENT:
                                    Bundle documentBundle = new Bundle();
                                    documentBundle.putString("classId", classnames.get(index).getClassId());
                                    documentBundle.putString("dcId", classzMomentsTypes.get(position).getDcId());
//                                    startActivity(ClasszMomentsAddDocumentActivity.class,documentBundle);
                                    Intent intent1 = new Intent(ClasszMomentsActivity.this, ClasszMomentsAddDocumentActivity.class);
                                    intent1.putExtras(documentBundle);
                                    startActivityForResult(intent1, 100);
                                    break;
                                case HONOUR:
                                    Bundle honourBundle = new Bundle();
                                    honourBundle.putString("classId", classnames.get(index).getClassId());
                                    honourBundle.putString("dcId", classzMomentsTypes.get(position).getDcId());
//                                    startActivity(ClasszMomentsAddHonourRollActivity.class,honourBundle);
                                    Intent intent2 = new Intent(ClasszMomentsActivity.this, ClasszMomentsAddHonourRollActivity.class);
                                    intent2.putExtras(honourBundle);
                                    startActivityForResult(intent2, 100);
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.right_icon:
                Intent searchIntent = new Intent(this,ClasszMomentsSearchActivity.class);
                searchIntent.putExtra( "class_id", classnames.get(index).getClassId());
                startActivityForResult(searchIntent,103);
//                startActivity(ClasszMomentsSearchActivity.class, "class_id", classnames.get(index).getClassId());
                break;
            case R.id.title_tv:
                if (classnames.size() > 0) {
                    final String[] strs = new String[classnames.size()];
                    for (int i = 0; i < classnames.size(); i++) {
                        strs[i] = classnames.get(i).getName();
                    }
                    showPopupWindow(v, strs, new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            index = position;
                            ((TextView) v).setText(strs[position]);
                            TextPaint tp = ((TextView) v).getPaint();
                            tp.setFakeBoldText(true);
                            EventBus.getDefault().post(new EventCenter(Config.REFRESHMOMENTLIST, index));
                            popupWindow.dismiss();
                        }
                    });
                }
                break;
            case R.id.send_tv:  //发送评论
                String content = ((TextView) comment_et).getText().toString();
                comment_et.setText("");
                if (!SharePreCache.isEmpty(content)) {
                    ViewTools.hideSoftInput(this);
                    comment_rl.setVisibility(View.GONE);
                    initTag();
                    if (!TextUtils.isEmpty(content)) {
                        if (commentType == 0) {
                            ClasszMomentsComment commentParams = new ClasszMomentsComment(momentDid, content, "1", SharedPreferencesUtils.getSharePrefString("login_userid"));
                            NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT + tag);
                        } else if (commentType == 1) {  //回复
                            ClasszMomentsComment commentParams = new ClasszMomentsComment(beRepCommentBean.getdId(), content, "1", SharedPreferencesUtils.getSharePrefString("login_userid"));
                            commentParams.setBrepUserId(beRepCommentBean.getUserId());
                            commentParams.setParentId(beRepCommentBean.getDcId());
                            NetworkRequest.request(commentParams, CommonUrl.COMMENT, Config.COMMENT + tag);
                        }
                    }
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "noSuccess":
                break;
            case Config.MOMENTTYPELIST:   //获取动态类型集合
                JSONObject jo = (JSONObject) event.getData();
                classzMomentsTypes.clear();
                classzMomentsTypes.addAll(GsonUtils.toArray(ClasszMomentType.class, jo));
                initFragmentsAndTitles();
                break;
            case "classList":    //获取班级集合
                JSONObject os = (JSONObject) event.getData();
                classnames = GsonUtils.toArray(ClassesBeans.class, os);
                if (classnames != null && classnames.size() > 0) {
                    title_tv.setClickable(true);
                    right_icon0.setClickable(true);
                    loadingLayout.onDone();
                    title_tv.setText(classnames.get(index).getName());
                    TextPaint tp = title_tv.getPaint();
                    tp.setFakeBoldText(true);
                } else if (classnames == null || classnames.size() == 0) {
                    title_tv.setClickable(false);
                    right_icon0.setClickable(false);
                    loadingLayout.onEmpty();
                }
                initFragmentsAndTitles();
                break;
            case Config.SHOWCOMMENTDIALOG:   //显示评论弹窗，在点击弹窗中的评论
//                inputMenu.setVisibility(View.VISIBLE);
                comment_rl.setVisibility(View.VISIBLE);
                comment_et.requestFocus();
                ViewTools.popSoftInput(this);
                momentDid = event.getData().toString();
                commentType = 0;
                break;
            case Config.SHOWCOMMENTDIALOGINLIST:
//                inputMenu.setVisibility(View.VISIBLE);
                comment_rl.setVisibility(View.VISIBLE);
                comment_et.requestFocus();
                ViewTools.popSoftInput(this);
                beRepCommentBean = (ClasszMomentCommentBean) event.getData();
                commentType = 1;
                break;
            case Config.HIDDENINPUTWINDOW:
                comment_rl.setVisibility(View.GONE);
                comment_et.setText("");
                break;
        }
    }

    /**
     * 初始化弹窗
     */
    private void initPopupWindow() {
        popupWindow = new BubblePopupWindow(this);
//        View bubbleView = inflater.inflate(R.layout.listview,null);
//        ListView listView = (ListView) bubbleView.findViewById(R.id.listview);
        popListView = new ListViewAdaptWidth(this);
        popListView.setCacheColorHint(Color.WHITE);
        popListView.setDivider(new ColorDrawable(Color.WHITE));
        popListView.setDividerHeight(1);
        popAdapter = new ArrayAdapter<>(this, R.layout.textview);
//        popAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        popListView.setAdapter(popAdapter);
        popupWindow.setBubbleView(popListView, ContextCompat.getColor(this, R.color.title_background_red));
    }

    /**
     * 新增弹窗
     * @param v
     * @param strs
     * @param listener
     */
   /* private void showAddPop(View v,String[] strs, ListView.OnItemClickListener listener){

        popupWindow = new BubblePopupWindow(this);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
        popListView = new ListViewAdaptWidth(this);
        popListView.setCacheColorHint(Color.WHITE);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_background_red)));
        popListView.setDivider(new ColorDrawable(Color.WHITE));
        popListView.setDividerHeight(1);
        popupWindow.setContentView(popListView);
        popAdapter = new ArrayAdapter<>(this,R.layout.textview);
//        popAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        popListView.setAdapter(popAdapter);
        popAdapter.clear();
        popAdapter.addAll(strs);
        popAdapter.notifyDataSetChanged();
        popListView.setOnItemClickListener(listener);
    }*/

    /**
     * 显示添加新动态的弹窗
     */
    private void showPopupWindow(View v, String[] strs, ListView.OnItemClickListener itemClickListener) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        popAdapter.clear();
        popAdapter.addAll(strs);
        popAdapter.notifyDataSetChanged();
        popListView.setOnItemClickListener(itemClickListener);
        popupWindow.showAsDropDown(v, -20, 0);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onResume() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onResume();
    }

    /**
     * 用于给Fragment传递参数  获取动态类型集合   classzMomentsTypes
     * 使这两个方法需要保证Fragment在获取班级列表和动态类型列表请求成功后 才实例化
     *
     * @return classzMomentsTypes
     */
    public List<ClasszMomentType> getClasszMomentType() {
        return classzMomentsTypes;
    }

    /**
     * 用于给Fragment传递参数  获取动态类型集合   classnames
     *
     * @return classnames
     */
    public List<ClassesBeans> getClassNames() {
        return classnames;
    }

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }
}
