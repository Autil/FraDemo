package com.ll.frademo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by l4396 on 2018/4/11.
 */

public class SportData implements Serializable {
    private static final long serialVersionUID = 13L;

    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private float useTime;//用时
    private float distance;//运动距离
    private String module;//运动模式
    private float ey;

    private int num;//序号
    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
    }

    public SportData(Date startTime,Date endTime,float useTime,float distance,String module){
        this.startTime = startTime;
        this.endTime = endTime;
        this.useTime = useTime;
        this.distance = distance;
        this.module = module;
    }

    public Date getStartTime(){
        return startTime;
    }
    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }

    public Date getEndTime(){
        return endTime;
    }
    public  void setEndTime(Date endTime){
        this.endTime = endTime;
    }

    public float getUseTime(){
        return useTime;
    }
    public void setUseTime(float useTime){
        this.useTime = useTime;
    }

    public float getDistance(){
        return distance;
    }
    public void setDistance(float distance){
        this.distance = distance;
    }

    public String getModule(){
        return module;
    }
    public void setModule(String module){
        this.module = module;
    }

    public float getEy() {
        return ey;
    }

    public void setEy(float ey) {
        this.ey = ey;
    }
}
