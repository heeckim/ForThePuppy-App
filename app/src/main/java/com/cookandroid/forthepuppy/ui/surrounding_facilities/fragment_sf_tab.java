package com.cookandroid.forthepuppy.ui.surrounding_facilities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cookandroid.forthepuppy.MainActivity;
import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.model.category_search.Document;
import com.cookandroid.forthepuppy.utils.sfUtils.ListViewAdapter;
import com.cookandroid.forthepuppy.utils.sfUtils.SfTabItem;
import com.cookandroid.forthepuppy.webView;

import java.util.List;

public class fragment_sf_tab extends Fragment {
    List<Document> viewItems;
    ListView listview;

    // Adapter 생성
    ListViewAdapter adapter = new ListViewAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sf_tab, container, false);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SfTabItem item = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), webView.class);
                intent.putExtra("url", item.getPlace_url());
                startActivity(intent);
            }
        });


        return view;
    }

    public void setViewItems(List<Document> viewItems) {
        this.viewItems = viewItems;
        adapter.clear();
        for (Document d : viewItems) {
            adapter.addItem(d);
        }

        adapter.notifyDataSetChanged(); // 리스트 목록 갱신
    }

}