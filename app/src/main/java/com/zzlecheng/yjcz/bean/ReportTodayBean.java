package com.zzlecheng.yjcz.bean;

import java.io.Serializable;

/**
 * @类名: ReportTodayBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/9/3 上午11:40
 * @版本: 1.0.0
 */
public class ReportTodayBean implements Serializable {

    //部门
    private String zzjgmc;
    //报班人
    private String username;
    //报班日期
    private String dates;
    //报班时间
    private String times;
    //班次
    private String types;

    public String getZzjgmc() {
        return zzjgmc;
    }

    public void setZzjgmc(String zzjgmc) {
        this.zzjgmc = zzjgmc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "ReportTodayBean{" +
                "zzjgmc='" + zzjgmc + '\'' +
                ", username='" + username + '\'' +
                ", dates='" + dates + '\'' +
                ", times='" + times + '\'' +
                ", types='" + types + '\'' +
                '}';
    }
}
