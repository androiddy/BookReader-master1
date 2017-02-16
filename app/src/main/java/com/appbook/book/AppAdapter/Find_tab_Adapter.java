package com.appbook.book.AppAdapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by cg on 2015/9/26.
 */
public class Find_tab_Adapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;
    private final String[] titles;
    private Context context;
    private List<Fragment> fragments;

    public Find_tab_Adapter(List<Fragment> fragments,String[] titles, FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
