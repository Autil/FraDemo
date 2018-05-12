package com.ll.frademo.entity;

import java.io.Serializable;

/**
 * Created by l4396 on 2018/4/11.
 */

public class Locate implements Serializable {
    private static final long serialVersionUID = 1L;

    private double latitude;
    private double longitude;
    private float speed;
    public Locate(double latitude,double longitude,float speed){
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public double getLatitude(){
        return latitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public float getSpeed(){
        return speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
}
