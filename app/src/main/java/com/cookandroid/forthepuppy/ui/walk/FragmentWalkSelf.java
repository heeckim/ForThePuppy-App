package com.cookandroid.forthepuppy.ui.walk;

import static android.content.Context.LOCATION_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cookandroid.forthepuppy.R;

public class FragmentWalkSelf extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walk_self, container, false);
    }
}
/*
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cookandroid.forthepuppy.R;
import com.cookandroid.forthepuppy.utils.CalDistance;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentWalkSelf extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {
    private Button mStartBtn, mStopBtn, mPauseBtn;
    private TextView mTimeTextView;
    private LinearLayout runtime, stoptime;

    //- 각각의 숫자는 독립적인 개별 '상태' 의미
    public static final int INIT = 0;//처음
    public static final int RUN = 1;//실행중
    public static final int PAUSE = 2;//정지
    // 현재 상태
    public static int status = INIT;
    //기록할때 순서 체크를 위한 변수
    private int cnt = 1;
    //타이머 시간 값을 저장할 변수
    private long baseTime, pauseTime;

    //거리
    CalDistance calDistance;

    double s_lat, s_long, bef_lat, bef_long, cur_lat, cur_long;
    double sum_dist = 0.0f;

    java.sql.Date date; // 산책날짜
    Time startTime; // 산책시작시간 문자열 쓰면 안되나요...
    Time endTime; // 산책종료시간
    double totalDistance; // 총 산책 거리
    long totalTime; // 총 산책 시간


    // 카카오맵
    private static final String LOG_TAG = "KakaoMap";
    MapView mapView;
    private ViewGroup mapViewContainer;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    LinearLayout tempView;
    // value
    MapPoint currentMapPoint;

    MapPolyline currentPolyline;
    List<MapPolyline> tempPolyline = new ArrayList<>();
    int polylineColer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walk_self, container, false);

        // Location 제공자에서 정보를 얻어오기(GPS)
        // 1. Location을 사용하기 위한 권한을 얻어와야한다 AndroidManifest.xml
        //     ACCESS_FINE_LOCATION : NETWORK_PROVIDER, GPS_PROVIDER
        //     ACCESS_COARSE_LOCATION : NETWORK_PROVIDER
        // 2. LocationManager 를 통해서 원하는 제공자의 리스너 등록
        // 3. GPS 는 에뮬레이터에서는 기본적으로 동작하지 않는다
        // 4. 실내에서는 GPS_PROVIDER 를 요청해도 응답이 없다.  특별한 처리를 안하면 아무리 시간이 지나도
        //    응답이 없다.
        //    해결방법은
        //     ① 타이머를 설정하여 GPS_PROVIDER 에서 일정시간 응답이 없는 경우 NETWORK_PROVIDER로 전환
        //     ② 혹은, 둘다 한꺼번헤 호출하여 들어오는 값을 사용하는 방식.

        mStartBtn = (Button) view.findViewById(R.id.btn_start);
        mStopBtn = (Button) view.findViewById(R.id.btn_stop);
        mPauseBtn = (Button) view.findViewById(R.id.btn_pause);
        mTimeTextView = (TextView) view.findViewById(R.id.tvTime);

        runtime = (LinearLayout) view.findViewById(R.id.layout_run_time);
        stoptime = (LinearLayout) view.findViewById(R.id.layout_stop_time);

        // 지도 띄우기
        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        tempView = view.findViewById(R.id.tempView);
        mapView.setVisibility(View.GONE);
        tempView.setVisibility(View.VISIBLE);

        polylineColer = Color.argb(255, 255, 51, 0);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setVisibility(View.VISIBLE);
                tempView.setVisibility(View.GONE);


                // 폴리라인 생성
                currentPolyline = new MapPolyline();
                currentPolyline.setLineColor(polylineColer); // Polyline 컬러 지정
                mapView.addPolyline(currentPolyline);

                Long l = System.currentTimeMillis();
                Date date1 = new Date(l);

                long t = date1.getTime();

                // 시작시간
                date = new java.sql.Date(t);
                startTime = new Time(t);
                System.out.println(date);
                System.out.println(startTime);

                // 스톱워치
                stoptime.setVisibility(View.GONE);
                runtime.setVisibility(View.VISIBLE);

                baseTime = SystemClock.elapsedRealtime();

                //핸들러 실행
                handler.sendEmptyMessage(0);
                mPauseBtn.setText("일시정지");

                //상태 변환
                status = RUN;
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setVisibility(View.GONE);
                tempView.setVisibility(View.VISIBLE);

                // 지도에 찍힌 모든 폴리라인 삭제
                for (MapPolyline mapPolyline : tempPolyline) {
                    mapView.removePolyline(mapPolyline);
                }

                s_lat = 0;
                s_long = 0;
                sum_dist = 0;

                Long l = System.currentTimeMillis();
                Date date1 = new Date(l);
                long t = date1.getTime();

                // 산책종료시간
                endTime = new Time(t);

//                    // 총 시간
//                    totalTime = endTime.getTime() - startTime.getTime();
//                    System.out.println(totalTime);

                // 스톱워치
                stoptime.setVisibility(View.VISIBLE);
                runtime.setVisibility(View.GONE);

                // 핸들러 정지
                handler.removeMessages(0);

                mTimeTextView.setText("00:00");
                baseTime = 0;
                pauseTime = 0;

                cnt = 1;

                status = INIT;
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스톱워치
                switch (status) {
                    case RUN:
                        // 멈추기전까지는 저장
                        tempPolyline.add(currentPolyline);

                        // 다시시작할때 그위치에서부터 다시 거리 추가
                        s_lat = 0;
                        s_long = 0;

                        //핸들러 정지
                        handler.removeMessages(0);
                        //정지 시간 체크
                        pauseTime = SystemClock.elapsedRealtime();
                        mPauseBtn.setText("시작");
                        //상태변환
                        status = PAUSE;
                        break;
                    case PAUSE:
                        // 다시 시작하는 위치부터 폴리라인 시작
                        currentPolyline = new MapPolyline();
                        currentPolyline.setLineColor(polylineColer); // Polyline 컬러 지정
                        mapView.addPolyline(currentPolyline);

                        long reStart = SystemClock.elapsedRealtime();
                        baseTime += (reStart - pauseTime);
                        handler.sendEmptyMessage(0);
                        mPauseBtn.setText("일시정지");
                        status = RUN;
                }
            }
        });

        return view;
    }

    private String getTime() {
        //경과된 시간 체크

        long nowTime = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간?
        totalTime = nowTime - baseTime;

        long h = totalTime / 1000 / 360;
        long m = totalTime / 1000 / 60;
        long s = (totalTime / 1000) % 60;
        long ms = totalTime % 1000;

        String recTime = String.format("%02d:%02d", h, m);

        return recTime;
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {

            mTimeTextView.setText(getTime());

            //
            handler.sendEmptyMessage(0);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    */
/* 여기부터 카카오맵 *//*


    */
/*
     *  현재 위치 업데이트(setCurrentLocationEventListener)
     *//*

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(currentMapPoint, true);
        //전역변수로 현재 좌표 저장
        double longitude = mapPointGeo.latitude;
        double latitude = mapPointGeo.longitude;

        if (status == RUN) {
            if (s_lat == 0 && s_long == 0) {
                s_lat = latitude;
                s_long = longitude;
                bef_lat = latitude;
                bef_long = longitude;
            } else {
                */
/* 현재의 GPS 정보 저장*//*

                cur_lat = latitude;
                cur_long = longitude;

                */
/* 이전의 GPS 정보와 현재의 GPS 정보로 거리를 구한다.*//*

                calDistance = new CalDistance(bef_lat, bef_long, cur_lat, cur_long); // 거리계산하는 클래스 호출
                double dist = calDistance.getDistance();
                dist = (int) (dist * 100) / 100.0; // 소수점 둘째 자리 계산
                sum_dist += dist;

                */
/* 이전의 GPS 정보를 현재의 GPS 정보로 변환한다. *//*

                bef_lat = cur_lat;
                bef_long = cur_long;
            }
            // 총 거리
            totalDistance = sum_dist;

            // 폴리라인
            mapView.removePolyline(currentPolyline);
            currentPolyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            mapView.addPolyline(currentPolyline);
        }
        //트래킹 모드가 아닌 단순 현재위치 업데이트일 경우, 한번만 위치 업데이트하고 트래킹을 중단시키기 위한 로직
//        if (!isTrackingMode) {
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
//        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}*/
