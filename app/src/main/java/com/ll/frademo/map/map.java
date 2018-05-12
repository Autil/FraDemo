package com.ll.frademo.map;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.ll.frademo.R;

import java.util.List;

public class map extends AppCompatActivity implements LocationSource,AMapLocationListener,AMap.OnMapLoadedListener {

    List<LatLng> list;
    LatLng lastlatLng;
    LatLng newlatLng;


    private MapView mapView = null;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;
    private UiSettings mUiSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        init();
        aMap.setOnMapLoadedListener(this);
        Intent intent = getIntent();
        list = (List<LatLng>) intent.getSerializableExtra("list");
        if(list != null && list.size() < 2){
            list = null;
        }
        if (list != null){
            lastlatLng = list.get(list.size()-1);
        }
        ToggleButton tb = findViewById(R.id.tb);
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                }
                else {
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
            }
        });
    }

    private void init(){
        if (aMap == null) {
            aMap = mapView.getMap();

            mUiSetting = aMap.getUiSettings();
            mUiSetting.setScaleControlsEnabled(true);

            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
            myLocationStyle.strokeColor(Color.argb(0,0,0,0));
            myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
            aMap.setMyLocationStyle(myLocationStyle);

            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onMapLoaded() {
        if (list != null){
            if (list != null){
                aMap.addPolyline(new PolylineOptions().color(Color.rgb(0,153,255)).addAll(list));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(list.get(0)));
               // aMap.addMarker(new MarkerOptions().position(list.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_start)));
            }else {
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(119.217926,26.036532)));
            }
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        }

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null){
            if (amapLocation != null && amapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(amapLocation);
                System.out.println("这是小蓝点");
                newlatLng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());

                if (lastlatLng != null){
                    aMap.addPolyline(new PolylineOptions().color(Color.rgb(0,153,255)).add(lastlatLng,newlatLng));
                }
                lastlatLng = newlatLng;

            }else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null){
            mLocationClient = new AMapLocationClient(this);
            mLocationClientOption = new AMapLocationClientOption();

            mLocationClient.setLocationListener(this);

            mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationClientOption);

            mLocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null){
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;

    }




}

