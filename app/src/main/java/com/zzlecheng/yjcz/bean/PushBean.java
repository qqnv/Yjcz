package com.zzlecheng.yjcz.bean;

/**
 * @类名: PushBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/4 11:46 AM
 * @版本: 1.0.0
 */
public class PushBean {

    /**
     * dates : 2019-01-04 11:39:22
     * extra : {"lcid":"0fe7564018174a5493707d71879d3990","lclsid":"d96bd0b62e204c4bbfd1bbaa69f73306","title":"应急预案:拥堵预案","content":"预案执行结束","lcmc":"拥堵预案"}
     * id : 7ba42d40709247a78c8391e9decdb0a2
     * message : 预案执行结束
     * sfyd : 0
     * title : 应急预案:拥堵预案
     * userid : c038ae1c4d3d4fbf9b083091f4e632a3
     */

    private String dates;
    private ExtraBean extra;
    private String id;
    private String message;
    private String sfyd;
    private String title;
    private String userid;

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "PushBean{" +
                "dates='" + dates + '\'' +
                ", extra=" + extra +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sfyd='" + sfyd + '\'' +
                ", title='" + title + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }

    public static class ExtraBean {
        /**
         * lcid : 0fe7564018174a5493707d71879d3990
         * lclsid : d96bd0b62e204c4bbfd1bbaa69f73306
         * title : 应急预案:拥堵预案
         * content : 预案执行结束
         * lcmc : 拥堵预案
         */

        private String lcid;
        private String lclsid;
        private String title;
        private String content;
        private String lcmc;

        public String getLcid() {
            return lcid;
        }

        public void setLcid(String lcid) {
            this.lcid = lcid;
        }

        public String getLclsid() {
            return lclsid;
        }

        public void setLclsid(String lclsid) {
            this.lclsid = lclsid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLcmc() {
            return lcmc;
        }

        public void setLcmc(String lcmc) {
            this.lcmc = lcmc;
        }

        @Override
        public String toString() {
            return "ExtraBean{" +
                    "lcid='" + lcid + '\'' +
                    ", lclsid='" + lclsid + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", lcmc='" + lcmc + '\'' +
                    '}';
        }
    }
}
