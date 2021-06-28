package com.zzlecheng.yjcz.bean;

/**
 * @类名: GjdBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/24 3:06 PM
 * @版本: 1.0.0
 */
public class GjdBean {

    /**
     * id : 918b7e590bf64b12b51efd14af6be73c
     * lcid : 6852971574694259857fecaf4a5976ad
     * content : 1.给所有参与人发送通知
     2.人员操作权限有控制
     3.注意流程图标的变化
     4.注意操作详情及评论的显示
     5.待处理人接受通知
     6.操作类型
     * whsj : 2018-12-14 13:48:14
     * isnew : 1
     * version :
     */

    private String id;
    private String lcid;
    private String content;
    private String whsj;
    private String isnew;
    private String version;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWhsj() {
        return whsj;
    }

    public void setWhsj(String whsj) {
        this.whsj = whsj;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GjdBean{" +
                "id='" + id + '\'' +
                ", lcid='" + lcid + '\'' +
                ", content='" + content + '\'' +
                ", whsj='" + whsj + '\'' +
                ", isnew='" + isnew + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
