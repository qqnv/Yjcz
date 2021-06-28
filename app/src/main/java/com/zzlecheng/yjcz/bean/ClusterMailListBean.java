package com.zzlecheng.yjcz.bean;

import java.util.List;

/**
 * @类名: ClusterMailListBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/22 10:39 AM
 * @版本: 1.0.0
 */
public class ClusterMailListBean {


    /**
     * id : 49491ccb28ca48a2a11b52fea2275406
     * label : 林培培
     * label2 : 林培培
     * pid : 12bb6177244144c48584fc81a1e4e873
     * code :
     * type :
     * azkjid :
     * state : closed
     * checked : false
     * children : []
     * sfParent : false
     * sfChildren : false
     */

    private String id;
    private String label;
    private String label2;
    private String pid;
    private String code;
    private String type;
    private String azkjid;
    private String state;
    private boolean checked;
    private boolean sfParent;
    private boolean sfChildren;
    private List<?> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAzkjid() {
        return azkjid;
    }

    public void setAzkjid(String azkjid) {
        this.azkjid = azkjid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSfParent() {
        return sfParent;
    }

    public void setSfParent(boolean sfParent) {
        this.sfParent = sfParent;
    }

    public boolean isSfChildren() {
        return sfChildren;
    }

    public void setSfChildren(boolean sfChildren) {
        this.sfChildren = sfChildren;
    }

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ClusterMailListBean{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", label2='" + label2 + '\'' +
                ", pid='" + pid + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", azkjid='" + azkjid + '\'' +
                ", state='" + state + '\'' +
                ", checked=" + checked +
                ", sfParent=" + sfParent +
                ", sfChildren=" + sfChildren +
                ", children=" + children +
                '}';
    }
}
