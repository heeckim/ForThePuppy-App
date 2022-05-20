package com.cookandroid.forthepuppy.ui.surrounding_facilities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookandroid.forthepuppy.MainActivity;
import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.api.ApiClient;
import com.cookandroid.forthepuppy.api.ApiInterface;
import com.cookandroid.forthepuppy.model.category_search.CategoryResult;
import com.cookandroid.forthepuppy.model.category_search.Document;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurroundingFacilitiesFragment extends Fragment {
    List<Document> st1Item;
    List<Document> st2Item;
    List<Document> st3Item;

    VPAdapter_sf adapter;

    // 서울역의 위경도값을 디폴트 값
    static double Lat = 37.555946;
    static double Lng = 126.972317;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sf, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.surroundingFacilitiesToolbar);
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ViewPager vp = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new VPAdapter_sf(getActivity().getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(3); // fragment 상태유지 하는 개수

        // 탭바랑 연동
        TabLayout tab = (TabLayout) view.findViewById(R.id.sf_tab);
        tab.setupWithViewPager(vp);


        st1Item = new ArrayList<>();
        st2Item = new ArrayList<>();
        st3Item = new ArrayList<>();

        if (((MainActivity)getActivity()).getLatitude() != 0 && ((MainActivity)getActivity()).getLongitude() != 0){
            Lat = ((MainActivity)getActivity()).getLatitude();
            Lng = ((MainActivity)getActivity()).getLongitude();
        }
        System.out.println(Lat + " " + Lng);
        initListView(Lat,Lng);

        return view;
    }

    public void initListView(double Lat, double Lng){
        adapter.setLatLng(Lat,Lng);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CategoryResult> call = apiInterface.getSearchLocationDetail(getString(R.string.restapi_key), "동물병원" , String.valueOf(Lat), String.valueOf(Lng), 15);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    st1Item = response.body().getDocuments();
                    adapter.setSfListViewItem(st1Item,1);
                }
                Log.d("성공","동물병원 call success");
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("실패","동물병원 call fails");
            }
        });

        call = apiInterface.getSearchLocationDetail(getString(R.string.restapi_key), "애견카페" , String.valueOf(Lat), String.valueOf(Lng), 15);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    st2Item = response.body().getDocuments();
                    adapter.setSfListViewItem(st2Item,2);
                }
                Log.d("성공","애견카페 call success");
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("실패","애견카페 call fails");
            }
        });

        call = apiInterface.getSearchLocationDetail(getString(R.string.restapi_key), "애견호텔" , String.valueOf(Lat), String.valueOf(Lng), 15);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    st3Item = response.body().getDocuments();
                    adapter.setSfListViewItem(st3Item,3);
                }
                Log.d("성공","애견호텔 call success");
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {
                Log.d("실패","애견호텔 call fails");
            }
        });


    }
}