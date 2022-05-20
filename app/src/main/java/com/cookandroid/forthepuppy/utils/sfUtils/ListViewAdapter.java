package com.cookandroid.forthepuppy.utils.sfUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.model.category_search.Document;
import com.cookandroid.forthepuppy.ui.surrounding_facilities.SurroundingFacilitiesFragment;
import com.cookandroid.forthepuppy.utils.CalDistance;
import com.cookandroid.forthepuppy.utils.walkUtils.WalkData;
import com.cookandroid.forthepuppy.utils.walkUtils.WalkTab2Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SfTabItem> listViewItemList = new ArrayList<SfTabItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_sf_tab_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView place_name = (TextView) convertView.findViewById(R.id.place_name);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView road_address_name = (TextView) convertView.findViewById(R.id.road_address_name);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SfTabItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        place_name.setText(listViewItem.getPlace_name());
        distance.setText(listViewItem.getDistance());
        road_address_name.setText(listViewItem.getRoad_address_name());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public SfTabItem getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Document document) {
        double Lat = 37.51960712639453;
        double Lng = 126.88948858206284;
        CalDistance cal = new CalDistance(Lat,Lng,Double.parseDouble(document.getY()),Double.parseDouble(document.getX()));
        double dis = (int)(cal.getDistance() / 10) / 100.0;
        SfTabItem item = new SfTabItem(document.getPlaceName(), String.valueOf(dis) + "km",
                document.getPlaceUrl(),document.getRoadAddressName(),Double.parseDouble(document.getY()),Double.parseDouble(document.getX()));

        listViewItemList.add(item);
    }

    public void clear(){
        listViewItemList.clear();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
