package pro.ui.activity.xkgl.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.ui.activity.xkgl.fragment.bzr.BanJiSelectionFramgent;
import pro.ui.activity.xkgl.fragment.bzr.LessonRecordFramgent;
import pro.ui.activity.xkgl.fragment.bzr.StudentSelectionFramgent;

public class LessonRecordActivity extends BaseActivity {

    @BindView(R.id.charge_lesson_record)
    TextView lesson_record;
    @BindView(R.id.charge_lesson_list)
    TextView lesson_list;
    @BindView(R.id.student_lesson_record)
    TextView student_record;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        fragments.add(new LessonRecordFramgent());
        fragments.add(new BanJiSelectionFramgent());
        fragments.add(new StudentSelectionFramgent());
        mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setAdapter(mPagerAdapter);
    }
    @OnClick({R.id.charge_lesson_record,R.id.charge_lesson_list,R.id.student_lesson_record})
    public void OnClick(View view){
        switch (view.getId()) {
            case R.id.charge_lesson_record:
                viewpager.setCurrentItem(0);
                break;
            case R.id.charge_lesson_list:
                viewpager.setCurrentItem(1);
                break;
            case R.id.student_lesson_record:
                viewpager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 根据position设置tab
     * @param position
     */
    private void changeBackground(int position){
        switch (position){
            case 0:
                lesson_record.setBackgroundResource(R.drawable.yuanjiao_message_white);
                lesson_list.setBackgroundResource(R.drawable.rec_contacts);
                student_record.setBackgroundResource(R.drawable.yuanjiao_contacts);
                lesson_record.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                lesson_list.setTextColor(getResources().getColor(R.color.white));
                student_record.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                lesson_record.setBackgroundResource(R.drawable.yuanjiao_message);
                lesson_list.setBackgroundResource(R.color.white);
                student_record.setBackgroundResource(R.drawable.yuanjiao_contacts);
                lesson_record.setTextColor(getResources().getColor(R.color.white));
                lesson_list.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                student_record.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                lesson_record.setBackgroundResource(R.drawable.yuanjiao_message);
                lesson_list.setBackgroundResource(R.drawable.rec_contacts);
                student_record.setBackgroundResource(R.drawable.yuanjiao_contacts_wright);
                lesson_record.setTextColor(getResources().getColor(R.color.white));
                lesson_list.setTextColor(getResources().getColor(R.color.white));
                student_record.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                break;
        }
    }


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

}
