package com.cookandroid.forthepuppy.ui.surrounding_facilities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cookandroid.forthepuppy.model.category_search.Document;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter_sf extends FragmentPagerAdapter {
    public ArrayList<fragment_sf_tab> fragments;
    private ArrayList<String> itext = new ArrayList<>();
    private Double Lat = 0.0;
    private Double Lng = 0.0;

    public void setLatLng(double Lat, double Lng){
        this.Lat = Lat;
        this.Lng = Lng;
    }

    public VPAdapter_sf(FragmentManager fm){
        super(fm);
        fragments = new ArrayList<>();
        for (int i =0; i< 3 ; i++){
            fragments.add(new fragment_sf_tab());
        }

        itext.add("병원");
        itext.add("애견카페");
        itext.add("애견호텔");
    }

    public void setSfListViewItem(List<Document> items, int n) {
        switch (n){
            case 1:
                fragments.get(0).setViewItems(items);
                break;
            case 2:
                fragments.get(1).setViewItems(items);
                break;
            case 3:
                fragments.get(2).setViewItems(items);
                break;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
