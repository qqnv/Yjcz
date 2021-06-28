package com.zzlecheng.yjcz.bean;

/**
 * @类名: LcBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/12 10:45 AM
 * @版本: 1.0.0
 */
public class LcBean {
    private String content;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private String qx;
    private String nodetype;
    private String lclsid;
    private String jdid;
    private String jdname;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public String getQx() {
        return qx;
    }

    public void setQx(String qx) {
        this.qx = qx;
    }

    public String getNodetype() {
        return nodetype;
    }

    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    public String getLclsid() {
        return lclsid;
    }

    public void setLclsid(String lclsid) {
        this.lclsid = lclsid;
    }

    public String getJdid() {
        return jdid;
    }

    public void setJdid(String jdid) {
        this.jdid = jdid;
    }

    public String getJdname() {
        return jdname;
    }

    public void setJdname(String jdname) {
        this.jdname = jdname;
    }

    @Override
    public String toString() {
        return "LcBean{" +
                "content='" + content + '\'' +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", qx='" + qx + '\'' +
                ", nodetype='" + nodetype + '\'' +
                ", lclsid='" + lclsid + '\'' +
                ", jdid='" + jdid + '\'' +
                ", jdname='" + jdname + '\'' +
                '}';
    }
}
