package com.cookandroid.forthepuppy.ui.walk;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.utils.walkUtils.ListViewAdapter;
import com.cookandroid.forthepuppy.utils.walkUtils.WalkData;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentWalkDailyRecord extends Fragment {
    View view;
    EditText et_Date;

    // 초기 년월 설정
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.KOREA);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM",Locale.KOREA);
    String year,month;

    ListView listview;
    ListViewAdapter adapter;

    ArrayList<WalkData> allItem = new ArrayList<>();
    ArrayList<WalkData> tempItem = new ArrayList<>();

    // 년월 선택창
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 1980;
    View dialogView;
    private DatePickerDialog.OnDateSetListener listener;
    public Calendar cal = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
    Button btnConfirm;
    Button btnCancel;
    NumberPicker monthPicker;
    NumberPicker yearPicker;

    AlertDialog ad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_walk_daily_record, container, false);

        InitializeView();
        SetListener();
        setWalkData();
        setListview(Integer.parseInt(year)-1900, Integer.parseInt(month)-1);
        return view;
    }

    public void InitializeView()
    {
        // 년월 대화박스 설정
        dialogView = (View) View.inflate(getActivity(), R.layout.year_month_picker, null);
        ad = new AlertDialog.Builder(getActivity()).create();
        btnConfirm =  (Button) dialogView.findViewById(R.id.btn_confirm);
        btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        monthPicker = (NumberPicker) dialogView.findViewById(R.id.picker_month);
        yearPicker = (NumberPicker) dialogView.findViewById(R.id.picker_year);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);

        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(cal.get(Calendar.YEAR));

        ad.setTitle("년/월 선택");
        ad.setView(dialogView);

        // 상단 월 선택
        et_Date = (EditText) view.findViewById(R.id.Date);

        // 초기 년월 설정
        year = yearFormat.format(currentTime);
        month = monthFormat.format(currentTime);
        et_Date.setText(year+"년 " + month + "월");

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }

    public void SetListener()
    {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int m = monthPicker.getValue();
                int y = yearPicker.getValue();

                et_Date.setText(y + "년 " + m + "월");

                setListview(y - 1900, m - 1);
                ad.dismiss();
            }
        });

        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.show();
            }
        });
    }

    public void setWalkData() {
        // 임시 데이터
        Long l = System.currentTimeMillis();
        Date date1 = new Date(l);
        long t = date1.getTime();
        java.sql.Date date = new java.sql.Date(t);
        Time startTime = new Time(t);
        Time endTime = new Time(t);
        double totalDistance = 1561.1;
        long totalTime = 156165;

        WalkData w1 = new WalkData(date,startTime,endTime,totalDistance,totalTime);
        l -= 1561651665;
        date1 = new Date(l);
        t = date1.getTime();
        date = new java.sql.Date(t);
        totalDistance = 61.1;
        totalTime = 561165;
        WalkData w2 = new WalkData(date,startTime,endTime,totalDistance,totalTime);

        totalDistance = 561.1;
        totalTime = 815605;
        WalkData w3 = new WalkData(date,startTime,endTime,totalDistance,totalTime);

        allItem.add(w1);
        allItem.add(w2);
        allItem.add(w3);
    }

    public void setListview(int y, int m) {
        System.out.println(y+" "+m);
        tempItem.clear();
        for (WalkData w:allItem) {
            if (w.getDate().getYear() == y && w.getDate().getMonth() == m){
                tempItem.add(w);
            }
        }
        adapter.clear();
        for (WalkData w:tempItem) {
            adapter.addItem(w);
        }

        adapter.notifyDataSetChanged(); // 리스트 목록 갱신
    }
}