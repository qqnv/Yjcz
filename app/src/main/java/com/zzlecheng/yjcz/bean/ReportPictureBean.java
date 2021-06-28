package com.zzlecheng.yjcz.bean;

import java.io.Serializable;

/**
 * @类名: ReportPictureBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/9/11 下午2:42
 * @版本: 1.0.0
 */
public class ReportPictureBean implements Serializable{

    /**
     * dx : 0.01
     * id : 844529f25b274c739fc412d8aa1c0c59
     * lb :
     * lj : D://upload//2018//09//11//20180911112346516.jpg
     * lx : .jpg
     * pid : 817132662562538820
     * scsj : 2018-09-11 11:23:46
     * wjmc : 20180911112346516.jpg
     * ysmc : 0002.jpg
     */

    private String dx;
    private String id;
    private String lb;
    private String lj;
    private String lx;
    private String pid;
    private String scsj;
    private String wjmc;
    private String ysmc;

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getLj() {
        return lj;
    }

    public void setLj(String lj) {
        this.lj = lj;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getScsj() {
        return scsj;
    }

    public void setScsj(String scsj) {
        this.scsj = scsj;
    }

    public String getWjmc() {
        return wjmc;
    }

    public void setWjmc(String wjmc) {
        this.wjmc = wjmc;
    }

    public String getYsmc() {
        return ysmc;
    }

    public void setYsmc(String ysmc) {
        this.ysmc = ysmc;
    }

    @Override
    public String toString() {
        return "ReportPictureBean{" +
                "dx='" + dx + '\'' +
                ", id='" + id + '\'' +
                ", lb='" + lb + '\'' +
                ", lj='" + lj + '\'' +
                ", lx='" + lx + '\'' +
                ", pid='" + pid + '\'' +
                ", scsj='" + scsj + '\'' +
                ", wjmc='" + wjmc + '\'' +
                ", ysmc='" + ysmc + '\'' +
                '}';
    }
}
