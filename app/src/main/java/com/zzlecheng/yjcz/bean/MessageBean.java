package com.zzlecheng.yjcz.bean;

/**
 * @类名: MessageBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/27 5:22 PM
 * @版本: 1.0.0
 */
public class MessageBean {

    /**
     * id : 2df9f63af08a4bee940494498c475155
     * message : 预案执行结束
     * url :
     * mod :
     * userid : e990f8a6609b417f88a5eb0ede2264cb
     * dates : 2019-01-04 15:02:01
     * sfyd : 0
     * title : 应急预案:除雪预案
     * extraStr : {"lcid":"6852971574694259857fecaf4a5976ad","lclsid":"9f07555315e74712864ec152b590fc5b","title":"应急预案:除雪预案","content":"预案执行结束","lcmc":"除雪预案"}
     * extra :
     * channelid :
     */

    private String id;
    private String message;
    private String url;
    private String mod;
    private String userid;
    private String dates;
    private String sfyd;
    private String title;
    private String extraStr;
    private String extra;
    private String channelid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getSfyd() {
        return sfyd;
    }

    public void setSfyd(String sfyd) {
        this.sfyd = sfyd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtraStr() {
        return extraStr;
    }

    public void setExtraStr(String extraStr) {
        this.extraStr = extraStr;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", mod='" + mod + '\'' +
                ", userid='" + userid + '\'' +
                ", dates='" + dates + '\'' +
                ", sfyd='" + sfyd + '\'' +
                ", title='" + title + '\'' +
                ", extraStr='" + extraStr + '\'' +
                ", extra='" + extra + '\'' +
                ", channelid='" + channelid + '\'' +
                '}';
    }
}
