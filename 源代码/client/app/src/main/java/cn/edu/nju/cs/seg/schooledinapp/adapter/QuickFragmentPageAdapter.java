package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class QuickFragmentPageAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> fragmentList;

    private List<String> fragmentTitleList;

    public QuickFragmentPageAdapter(FragmentManager fm,
                                    List<T> fragmentList, List<String> fragmentTitleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.fragmentTitleList = fragmentTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList == null ?
                super.getPageTitle(position) :
                fragmentTitleList.get(position);
    }

}
