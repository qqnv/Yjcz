package com.zzlecheng.yjcz.bean;

/**
 * @类名: ChatRightContentBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/28 4:47 PM
 * @版本: 1.0.0
 */
public class ChatRightContentBean {
    /**
     * id : 8fd12fcd735346a499dad6c101384bdf
     * lclsid : 7cee282e13be462ab43345ee0d33a68b
     * userid : e990f8a6609b417f88a5eb0ede2264cb
     * username : 段长
     * dates : 2018-12-28 16:19:34
     * content : 阿本
     * soures : app
     * types : 4
     */

    private String id;
    private String lclsid;
    private String userid;
    private String username;
    private String dates;
    private String content;
    private String soures;
    private String types;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLclsid() {
        return lclsid;
    }

    public void setLclsid(String lclsid) {
        this.lclsid = lclsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSoures() {
        return soures;
    }

    public void setSoures(String soures) {
        this.soures = soures;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "ProcedureChatBean{" +
                "id='" + id + '\'' +
                ", lclsid='" + lclsid + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", dates='" + dates + '\'' +
                ", content='" + content + '\'' +
                ", soures='" + soures + '\'' +
                ", types='" + types + '\'' +
                '}';
    }
}
