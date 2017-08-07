package pro.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.EduCountParams;
import com.zjhz.teacher.NetworkRequests.request.EduCountTermParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.EduCountAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.EduCount;
import com.zjhz.teacher.bean.Grade;
import com.zjhz.teacher.bean.Term;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

import static com.zjhz.teacher.utils.GsonUtils.toArray;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EducationCountActivity extends BaseActivity implements  RefreshLayout.OnLoadListener,RefreshLayout.OnRefreshListener {
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private final static String TAG = EducationCountActivity.class.getSimpleName();
    @BindView(R.id.term)
    TextView term;
    @BindView(R.id.grade)
    TextView grade;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.type_grade_check)
    TextView type_grade_check;
    @BindView(R.id.type_person_check)
    TextView type_person_check;
    @BindView(R.id.type_total)
    TextView type_total;
    @BindView(R.id.refresh_listview)
    ListView listview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.type_grade_check_image)
    ImageView type_grade_check_image;
    @BindView(R.id.type_person_check_image)
    ImageView type_person_check_image;
    @BindView(R.id.type_total_image)
    ImageView type_total_image;

    /**
     * sortType=1 按照班级德育排序，sortType=2 按照个人德育排序，sortType=3 按照总计排序
     */
    private final String CLASSCHECK = "1";
    private final String PERSONCHECK = "2";
    private final String TOTAL = "3";
    private String sortType ;

    /**
     * 班级德育排序  个人德育排   总计排序  三个按钮  上次按的是哪个
     */
    private TextView lastTextView;
    private ImageView lastImageView;

    /**
     * 排序是否正序
     */
    private boolean reverseFlag;
    /**
     * 是否弹出年级选择框
     */
    private boolean showGradeWindow;
    /**
     * 是否弹出学期选择框
     */
    private boolean showTermWindow;

    /**学期月份*/
    private List<Term> monthOfTerm;
    /**  年级  */
    private List<Grade> grades;
    /**德育统计数据*/
    private List<EduCount> counts = new ArrayList<>();

    private EduCountAdapter adapter;

    private int gradeIndex = -1, termIndex = -1;

    private DefaultLoadingLayout loadingLayout ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_count);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        titleTv.setText(R.string.education_count);
        title_back_img.setVisibility(View.VISIBLE);
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        adapter = new EduCountAdapter(this, counts);
        listview.setAdapter(adapter);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this,refreshLayout);
        //默认没有数据
        loadingLayout.onEmpty();
        //先获取年级和学期列表
        NetworkRequest.request(null, CommonUrl.gradeList, Config.gradeList);
        NetworkRequest.request(new EduCountTermParams("1"), CommonUrl.EDUCOUNTGETALLTERM, Config.EDUCOUNTGETALLTERM);
        dialog.show();
    }

    @OnClick({R.id.grade_rl, R.id.term_rl, R.id.title_back_img, R.id.type_total_ll, R.id.type_person_check_ll, R.id.type_grade_check_ll})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.grade_rl:   //年级
                if (grades == null || grades.size() == 0) {
                    NetworkRequest.request(null, CommonUrl.gradeList, Config.gradeList);
                    dialog.show();
                    showGradeWindow = true;
                } else {
                    showGradePickView();
                }
                break;
            case R.id.term_rl:   //学期
                if (monthOfTerm == null || monthOfTerm.size()==0) {
                    NetworkRequest.request(new EduCountTermParams("1"), CommonUrl.EDUCOUNTGETALLTERM, Config.EDUCOUNTGETALLTERM);
                    dialog.show();
                    showTermWindow = true;
                } else {
                    showTermPickView();
                }
                break;
            case R.id.type_total_ll:   //总计
                typeChoose(type_total, type_total_image, TOTAL);
                break;
            case R.id.type_person_check_ll:    //个人统计
                typeChoose(type_person_check,type_person_check_image, PERSONCHECK);
                break;
            case R.id.type_grade_check_ll:      //班级统计
                typeChoose(type_grade_check, type_grade_check_image, CLASSCHECK);
                break;
            case R.id.title_back_img:      //返回
                finish();
                break;
        }
    }

    /**
     * 排序的相关操作
     *
     * @param text     textView id
     * @param image       ImageView  图片
     * @param sortType 排序的类型选择
     */
    private void typeChoose(TextView text, ImageView image, String sortType) {
        if (!counts.isEmpty()) {   //没有结果的时候不进行排序
            text.setTextColor(getResources().getColor(R.color.red));
            pageNum = 1;
            this.sortType = sortType;
            Term term = monthOfTerm.get(termIndex);
            EduCountParams eduCountParams = new EduCountParams(term.getStartTime(), term.getEndTime(), grades.get(gradeIndex).getGradeId());
            eduCountParams.setSortType(sortType);
            if (text == lastTextView) {
                reverseFlag = !reverseFlag;
                if (reverseFlag) {
                    eduCountParams.setReverseFlag("1");
                    image.setImageResource(R.mipmap.sort_down);
                } else {
                    image.setImageResource(R.mipmap.sort_up);
                }
            } else {
                if (lastTextView != null) {
                    lastTextView.setTextColor(getResources().getColor(R.color.text_color28));
                    lastImageView.setImageResource(R.mipmap.sort);
                }
                reverseFlag = false;
                image.setImageResource(R.mipmap.sort_up);
            }
            eduCountParams.setPage(String.valueOf(pageNum));
            eduCountParams.setPageSize(String.valueOf(pageSize));
            NetworkRequest.request(eduCountParams, CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
            dialog.show();
            lastTextView = text;
            lastImageView = image;
        }
    }


    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "noSuccess":
                break;
            case Config.gradeList:  //获取年级信息
                grades = toArray(Grade.class, (JSONObject) event.getData());
                if (null != grades && grades.size()>0) {
                    Grade grade = new Grade();
                    grade.setName(getResources().getString(R.string.all_grade));
                    grades.add(0,grade);
                    gradeIndex = 0;
                    if(showGradeWindow){
                        showTermWindow = false;
                        showGradePickView();
                    }
                } else {
                    ToastUtils.showShort(R.string.grade_data_not_found);
                }
                break;
            case Config.EDUCOUNTGETALLTERM: //获取学期信息
                monthOfTerm = toArray(Term.class, ((JSONObject) event.getData()));
                if (monthOfTerm == null || monthOfTerm.size() == 0) {
                    ToastUtils.showShort(R.string.term_not_found);
                } else {
                    if(showTermWindow) {
                        showTermPickView();
                        showTermWindow = false;
                    }/*else{
                        termIndex = 0;
                        Term term = monthOfTerm.get(termIndex);
                        NetworkRequest.request(new EduCountParams(term.getStartTime(), term.getEndTime()), CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
                        loadingLayout.onLoading();
                    }*/

                }
                break;
            case Config.EDUCOUNTGETCOUNT:  //获取德育统计信息
                try {
                    total = ((JSONObject) event.getData()).getInt("total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<EduCount> list = GsonUtils.toArray(EduCount.class, ((JSONObject) event.getData()));
                if(pageNum == 1) {
                    counts.clear();
                    counts.addAll(list);
                    if (null == counts || counts.isEmpty()) {
                        loadingLayout.onEmpty();
                    }
                }else{
                    if(null!=list) {
                        counts.addAll(list);
                    }
                    refreshLayout.setLoading(false);
                }
                if(counts.size()>0){
                    loadingLayout.onDone();
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 展示选择器公用代码
     */
    private void showPickView(final List<String> list, final TextView tv, final boolean getData) {
        setDefault();
        OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker((ArrayList) list, 0, 0, 0);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                tv.setText(list.get(options1));
                pageNum = 1;
                if (getData) {  //选择学期
                    Term term = monthOfTerm.get(options1);
                    termIndex = options1;
                    if (gradeIndex == -1 || termIndex == - 1) {  //请求所有年级
                        NetworkRequest.request(new EduCountParams(term.getStartTime(), term.getEndTime()), CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
                    } else {  //请求指定年级
                        NetworkRequest.request(new EduCountParams(term.getStartTime(), term.getEndTime(), grades.get(gradeIndex).getGradeId()), CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
                    }
                    dialog.show();
                } else {  //选择年级
                    gradeIndex = options1;
                    if(termIndex >-1){
                        Term term = monthOfTerm.get(termIndex);
                        NetworkRequest.request(new EduCountParams(term.getStartTime(), term.getEndTime(), grades.get(gradeIndex).getGradeId()), CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
                        dialog.show();
                    }
                }
            }
        });
        optionsPickerView.show();
    }

    /**
     * 展示年级选择器
     */
    private void showGradePickView() {
        List<String> gradeNames = new ArrayList<>();
        for (Grade grade : grades) {
            gradeNames.add(grade.getName());
        }
        showPickView(gradeNames, grade, false);
    }

    /**
     * 展示学期选择器
     */
    private void showTermPickView() {
        List<String> list = new ArrayList<>();
        for (Term term : monthOfTerm) {
            list.add(term.getTitle());
        }
        showPickView(list, term, true);
    }

    /**
     *设置默认
     */
    private void setDefault(){
        if(null!=lastImageView){
            lastImageView.setImageResource(R.mipmap.sort);
            reverseFlag = false;
        }
        if(null!=lastTextView){
            lastTextView.setTextColor(getResources().getColor(R.color.text_color28));
            reverseFlag = false;
        }
    }



    private int pageNum = 1;
    private int pageSize = 20;
    private int total = 0;

    @Override
    public void onLoad() {
        if(gradeIndex!=-1 && termIndex!=-1 && counts.size()<total) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    pageNum++;
                    getNewDate();
                }});
        }else{
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    private void getNewDate(){
        Term term = monthOfTerm.get(termIndex);
        EduCountParams params = new EduCountParams(term.getStartTime(), term.getEndTime(), grades.get(gradeIndex).getGradeId());
        if(reverseFlag){
            params.setReverseFlag("1");
        }
        if(sortType!=null){
            params.setSortType(sortType);
        }
        params.setPageSize(String.valueOf(pageSize));
        params.setPage(String.valueOf(pageNum));
        NetworkRequest.request(params, CommonUrl.EDUCOUNTGETCOUNT, Config.EDUCOUNTGETCOUNT);
        dialog.show();
    }
}
