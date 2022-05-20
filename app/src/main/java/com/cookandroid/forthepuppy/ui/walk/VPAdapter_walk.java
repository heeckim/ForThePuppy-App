package com.cookandroid.forthepuppy.ui.walk;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter_walk extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<>();

    public VPAdapter_walk(FragmentManager fm){
        super(fm);
        items = new ArrayList<>();
        items.add(new FragmentWalkSelf());
        items.add(new FragmentWalkSubstitute());
        items.add(new FragmentWalkDailyRecord());

        itext.add("산책하기");
        itext.add("대리산책");
        itext.add("산책일지");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
