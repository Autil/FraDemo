package com.ll.frademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.ll.frademo.R;
import com.ll.frademo.db.RideDataService;
import com.ll.frademo.entity.Locate;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends Activity implements AMap.OnMapLoadedListener{
    List<LatLng> list = new ArrayList<LatLng>();

    private LatLng latLng ;
    private AMap aMap;
    private MapView mapView;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        mapView = findViewById(R.id.map_show);
        mapView.onCreate(savedInstanceState);

        init();
        //设置定位监听器
        aMap.setOnMapLoadedListener(this);



        Intent intent = getIntent();
        int position = (int) intent.getSerializableExtra("position");//位置信息


        //获取运动轨迹
        List<Locate> locates = new RideDataService(this).getObject(position);


        for(Locate locate : locates){
            latLng = new LatLng(locate.getLatitude(),locate.getLongitude());
            list.add(latLng);
        }

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }

    }

    private void setUpMap() {
        //显示比例尺
        mUiSettings.setScaleControlsEnabled(true);
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
    }

    @Override
    public void onMapLoaded() {
        if (list != null){
            aMap.addPolyline(new PolylineOptions().color(Color.rgb(0, 153, 255)).addAll(list));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(list.get(0)));
            aMap.addMarker(new MarkerOptions().position(list.get(0))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_start)));
            aMap.addMarker(new MarkerOptions().position(list.get(list.size()-1))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_stop)));

            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        }

    }
}
