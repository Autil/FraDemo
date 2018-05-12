package com.ll.frademo.event;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l4396 on 2018/4/11.
 */

public class MyEvent {
    String module;//运动模式
    private float speed;//GPS实时速度
    private float distance;//路程
    private float ey;//耗能
    List<LatLng> list = new ArrayList<LatLng>();//保存的经纬度

    public String getModule(){
        return module;
    }
    public void setModule(String module){
        this.module = module;
    }

    public float getSpeed(){
        return speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }

    public float getDistance(){
        return distance;
    }
    public void setDistance(float distance){
        this.distance = distance;
    }

    public float getEy(){
        return ey;
    }
    public void setEy(float ey){
        this.ey = ey;
    }

    public List<LatLng> getList(){
        return list;
    }
    public void setList(List<LatLng> list){
        this.list = list;
    }
}
