package com.ll.frademo.bean;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private Boolean sex;
    private Integer age;
    private Float mheight;
    private Float mweight;
    private Integer mdistance;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getMheight() {
        return mheight;
    }

    public void setMheight(Float mheight) {
        this.mheight = mheight;
    }

    public Float getMweight() {
        return mweight;
    }

    public void setMweight(Float mweight) {
        this.mweight = mweight;
    }

    public Integer getMdistance() {
        return mdistance;
    }

    public void setMdistance(Integer mdistance) {
        this.mdistance = mdistance;
    }
}
