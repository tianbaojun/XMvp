package pro.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.hx.EaseConstant;
import com.zjhz.teacher.hx.ui.EaseConversationListFragment;
import com.zjhz.teacher.ui.activity.ChatActivity;
import com.zjhz.teacher.ui.fragment.LinkmanFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import pro.kit.ViewTools;

//import com.zjhz.app.hxchat.ui.fragment.ContactListFragment;
//import com.zjhz.app.hxchat.ui.fragment.ConversationListFragment;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 交流
 */
public class ExChangeFragment extends BaseFragment {

    @BindView(R.id.fragment_ex_change_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fragment_ex_change_left_text)
    TextView fragmentExChangeLeftText;
    @BindView(R.id.fragment_ex_change_right_text)
    TextView fragmentExChangeRightText;
    private Fragment[] fragments;
    private EaseConversationListFragment conversationListFragment;
    private LinkmanFragment linkmanFragment;
    private TabPagerAdapter mPagerAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_ex_change, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        mPagerAdapter = new TabPagerAdapter(getFragmentManager());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        conversationListFragment = new EaseConversationListFragment();  // TODO
        linkmanFragment = new LinkmanFragment();
        fragments = new Fragment[]{conversationListFragment, linkmanFragment};
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();

        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(context, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
    }

    @OnClick({R.id.fragment_ex_change_left_text, R.id.fragment_ex_change_right_text})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_ex_change_left_text:
                changeBackground("1");
                break;
            case R.id.fragment_ex_change_right_text:
                changeBackground("-1");
                break;
        }
    }

    private void changeBackground(String type){
        if ("1".equals(type)) {
            fragmentExChangeLeftText.setBackgroundResource(R.drawable.yuanjiao_message_white);
            fragmentExChangeRightText.setBackgroundResource(R.drawable.yuanjiao_contacts);
            fragmentExChangeLeftText.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
            fragmentExChangeRightText.setTextColor(getResources().getColor(R.color.white));
            mViewPager.setCurrentItem(0);
        }else{
            fragmentExChangeLeftText.setBackgroundResource(R.drawable.yuanjiao_message);
            fragmentExChangeRightText.setBackgroundResource(R.drawable.yuanjiao_contacts_wright);
            fragmentExChangeLeftText.setTextColor(getResources().getColor(R.color.white));
            fragmentExChangeRightText.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
            mViewPager.setCurrentItem(1);
        }
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mViewPager.addOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            if (0 == i){
                changeBackground("1");
            }else{
                changeBackground("-1");
            }
        }
    }

}
