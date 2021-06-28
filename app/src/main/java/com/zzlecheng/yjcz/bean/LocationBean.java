package com.zzlecheng.yjcz.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * @类名: LocationBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/9/7 上午9:21
 * @版本: 1.0.0
 */
public class LocationBean implements IPickerViewData {

    /**
     * id : 89d53aebc71c46b0b4d854790df0c72f
     * pid :
     * px : 1
     * sfyx : 1
     * sfyxxg : 1
     * sjdm : event_eventarea
     * sjdmmc : 事件区域
     * zdcj : 2
     * zddm : 89d53aebc71c46b0b4d854790df0c72f
     * zdmc : 大厅
     */

    private String id;
    private String px;
    private String sfyx;
    private String sfyxxg;
    private String sjdm;
    private String sjdmmc;
    private String zdcj;
    private String zddm;
    private String zdmc;

    public LocationBean(String id, String px, String sfyx, String sfyxxg, String sjdm,
                        String sjdmmc, String zdcj, String zddm, String zdmc) {
        this.id = id;
        this.px = px;
        this.sfyx = sfyx;
        this.sfyxxg = sfyxxg;
        this.sjdm = sjdm;
        this.sjdmmc = sjdmmc;
        this.zdcj = zdcj;
        this.zddm = zddm;
        this.zdmc = zdmc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }

    public String getSfyx() {
        return sfyx;
    }

    public void setSfyx(String sfyx) {
        this.sfyx = sfyx;
    }

    public String getSfyxxg() {
        return sfyxxg;
    }

    public void setSfyxxg(String sfyxxg) {
        this.sfyxxg = sfyxxg;
    }

    public String getSjdm() {
        return sjdm;
    }

    public void setSjdm(String sjdm) {
        this.sjdm = sjdm;
    }

    public String getSjdmmc() {
        return sjdmmc;
    }

    public void setSjdmmc(String sjdmmc) {
        this.sjdmmc = sjdmmc;
    }

    public String getZdcj() {
        return zdcj;
    }

    public void setZdcj(String zdcj) {
        this.zdcj = zdcj;
    }

    public String getZddm() {
        return zddm;
    }

    public void setZddm(String zddm) {
        this.zddm = zddm;
    }

    public String getZdmc() {
        return zdmc;
    }

    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "id='" + id + '\'' +
                ", px='" + px + '\'' +
                ", sfyx='" + sfyx + '\'' +
                ", sfyxxg='" + sfyxxg + '\'' +
                ", sjdm='" + sjdm + '\'' +
                ", sjdmmc='" + sjdmmc + '\'' +
                ", zdcj='" + zdcj + '\'' +
                ", zddm='" + zddm + '\'' +
                ", zdmc='" + zdmc + '\'' +
                '}';
    }

    @Override
    public String getPickerViewText() {
        return zdmc;
    }
}
