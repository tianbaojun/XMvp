package pro.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles;
    private Context context;
    private List<Fragment> fragments;
    private FragmentManager fm;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }
 
    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.context = context;
        this.fragments = fragments;
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<String> tabTitles, Context context, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.tabTitles = tabTitles;
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
 
    @Override
    public int getCount() {
        if(fragments != null)
            return fragments.size();
        return 0;
    }
 
    @Override
    public CharSequence getPageTitle(int position) {
        if(tabTitles == null)
            return null;
        return tabTitles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    public void changed() {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        notifyDataSetChanged();
    }
}