package com.zzlecheng.yjcz.bean;

/**
 * @类名: EmergencyBean
 * @描述: 应急处置列表
 * @作者: huangchao
 * @时间: 2018/12/11 3:34 PM
 * @版本: 1.0.0
 */
public class EmergencyBean {

    /**
     * id : 8
     * lcid : e0623c17a8b048d58bba893ced784195
     * xgsj :
     * xxwz :
     * lczt : 0
     * czr : 77d610ba97f546e5b10296352282a7d3
     * czsj : 2018-12-06 12:00:00
     * remarks :
     * czrmc : 大司马
     * lcmc :
     * yaqymc :
     */

    private String id;//流程历史ID
    private String lcid;//流程ID
    private String xgsj;
    private String xxwz;
    private String lczt;
    private String czr;
    private String czsj;
    private String remarks;
    private String czrmc;
    private String lcmc;
    private String yaqymc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLcid() {
        return lcid;
    }

    public void setLcid(String lcid) {
        this.lcid = lcid;
    }

    public String getXgsj() {
        return xgsj;
    }

    public void setXgsj(String xgsj) {
        this.xgsj = xgsj;
    }

    public String getXxwz() {
        return xxwz;
    }

    public void setXxwz(String xxwz) {
        this.xxwz = xxwz;
    }

    public String getLczt() {
        return lczt;
    }

    public void setLczt(String lczt) {
        this.lczt = lczt;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr;
    }

    public String getCzsj() {
        return czsj;
    }

    public void setCzsj(String czsj) {
        this.czsj = czsj;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCzrmc() {
        return czrmc;
    }

    public void setCzrmc(String czrmc) {
        this.czrmc = czrmc;
    }

    public String getLcmc() {
        return lcmc;
    }

    public void setLcmc(String lcmc) {
        this.lcmc = lcmc;
    }

    public String getYaqymc() {
        return yaqymc;
    }

    public void setYaqymc(String yaqymc) {
        this.yaqymc = yaqymc;
    }

    @Override
    public String toString() {
        return "EmergencyBean{" +
                "id='" + id + '\'' +
                ", lcid='" + lcid + '\'' +
                ", xgsj='" + xgsj + '\'' +
                ", xxwz='" + xxwz + '\'' +
                ", lczt='" + lczt + '\'' +
                ", czr='" + czr + '\'' +
                ", czsj='" + czsj + '\'' +
                ", remarks='" + remarks + '\'' +
                ", czrmc='" + czrmc + '\'' +
                ", lcmc='" + lcmc + '\'' +
                ", yaqymc='" + yaqymc + '\'' +
                '}';
    }
}
