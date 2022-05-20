package com.cookandroid.forthepuppy.ui.walk;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookandroid.forthepuppy.MainActivity;
import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.ui.surrounding_facilities.VPAdapter_sf;
import com.google.android.material.tabs.TabLayout;

public class WalkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walk, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.walkToolbar);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ViewPager vp = (ViewPager) view.findViewById(R.id.viewpager_walk);
        VPAdapter_walk adapter = new VPAdapter_walk(getActivity().getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3); // fragment 상태유지 하는 개수

        // 탭바랑 연동
        TabLayout tab = (TabLayout) view.findViewById(R.id.walk_tab);
        tab.setupWithViewPager(vp);

        return view;
    }
}