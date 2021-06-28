package com.zzlecheng.yjcz.bean;

import java.io.Serializable;

/**
 * @类名: VersionUpdateBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/8/31 下午3:41
 * @版本: 1.0.0
 */
public class VersionUpdateBean implements Serializable {

    private String coerce;//是否强制更新
    private String content;//更新的内容
    private String datetime;//添加时间
    private String dx;//安装包大小
    private String id;//ID
    private String newest;//是否最新
    private String numbers;//最新版本号
    private String path;//下载路径
    private String types;//app类型（Android/IOS）
    private String wjid;//文件ID
    private String wjmc;//文件名称

    public String getCoerce() {
        return coerce;
    }

    public void setCoerce(String coerce) {
        this.coerce = coerce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

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

    public String getNewest() {
        return newest;
    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getWjid() {
        return wjid;
    }

    public void setWjid(String wjid) {
        this.wjid = wjid;
    }

    public String getWjmc() {
        return wjmc;
    }

    public void setWjmc(String wjmc) {
        this.wjmc = wjmc;
    }

    @Override
    public String toString() {
        return "VersionUpdateBean{" +
                "coerce='" + coerce + '\'' +
                ", content='" + content + '\'' +
                ", datetime='" + datetime + '\'' +
                ", dx='" + dx + '\'' +
                ", id='" + id + '\'' +
                ", newest='" + newest + '\'' +
                ", numbers='" + numbers + '\'' +
                ", path='" + path + '\'' +
                ", types='" + types + '\'' +
                ", wjid='" + wjid + '\'' +
                ", wjmc='" + wjmc + '\'' +
                '}';
    }

}
