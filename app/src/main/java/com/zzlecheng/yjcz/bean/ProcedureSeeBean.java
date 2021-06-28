package com.zzlecheng.yjcz.bean;

import java.util.List;

/**
 * @类名: ProcedureSeeBean
 * @描述: 查看节点的操作
 * @作者: huangchao
 * @时间: 2018/12/24 2:56 PM
 * @版本: 1.0.0
 */
public class ProcedureSeeBean {

    /**
     * jdxq : {"id":"1380e2507d4c4ceebbbc3c823d6d0295","lcid":"6852971574694259857fecaf4a5976ad","nodename":"起始位置","nodedescribe":"开始","nodex":"0","nodey":"3","nodesupe":"","nodenext":"","nodetype":"node_type_start","styledefault":"","stylesucceed":"","stylenext":"","userid":"cc5d179210254b62a2004c1013ce9a32","over":"","pe1":"","pe2":"队长","duty":"","jdzt":"","lclsid":"","username":"","czms":"","nodetypename":"开始","czsj":"2018-12-19 11:16:57","isSubmit":"false","czqx":"","jdlsid":"212cb647103e4813976c9b277b800c48"}
     * pl : [{"id":"9821u48i23","lclsid":"a11f5a2441d44dbba5024fd72797862c","jdlsid":"212cb647103e4813976c9b277b800c48","czms":"啊哈","czr":"","czsj":"2018-12-20 17:04:06","czrmc":""},{"id":"V2b82G8Ho1","lclsid":"a11f5a2441d44dbba5024fd72797862c","jdlsid":"212cb647103e4813976c9b277b800c48","czms":"设置","czr":"","czsj":"2018-12-20 17:02:03","czrmc":""},{"id":"97faf964e94e47aab21def15ea5c4233","lclsid":"a11f5a2441d44dbba5024fd72797862c","jdlsid":"212cb647103e4813976c9b277b800c48","czms":"明下午","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"2018-12-20 16:21:02","czrmc":"段长"}]
     */

    private JdxqBean jdxq;
    private List<PlBean> pl;

    public JdxqBean getJdxq() {
        return jdxq;
    }

    public void setJdxq(JdxqBean jdxq) {
        this.jdxq = jdxq;
    }

    public List<PlBean> getPl() {
        return pl;
    }

    public void setPl(List<PlBean> pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return "ProcedureSeeBean{" +
                "jdxq=" + jdxq +
                ", pl=" + pl +
                '}';
    }

    public static class JdxqBean {
        /**
         * id : 1380e2507d4c4ceebbbc3c823d6d0295
         * lcid : 6852971574694259857fecaf4a5976ad
         * nodename : 起始位置
         * nodedescribe : 开始
         * nodex : 0
         * nodey : 3
         * nodesupe :
         * nodenext :
         * nodetype : node_type_start
         * styledefault :
         * stylesucceed :
         * stylenext :
         * userid : cc5d179210254b62a2004c1013ce9a32
         * over :
         * pe1 :
         * pe2 : 队长
         * duty :
         * jdzt :
         * lclsid :
         * username :
         * czms :
         * nodetypename : 开始
         * czsj : 2018-12-19 11:16:57
         * isSubmit : false
         * czqx :
         * jdlsid : 212cb647103e4813976c9b277b800c48
         */

        private String id;
        private String lcid;
        private String nodename;
        private String nodedescribe;
        private String nodex;
        private String nodey;
        private String nodesupe;
        private String nodenext;
        private String nodetype;
        private String styledefault;
        private String stylesucceed;
        private String stylenext;
        private String userid;
        private String over;
        private String pe1;
        private String pe2;
        private String duty;
        private String jdzt;
        private String lclsid;
        private String username;
        private String czms;
        private String nodetypename;
        private String czsj;
        private String isSubmit;
        private String czqx;
        private String jdlsid;

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

        public String getNodename() {
            return nodename;
        }

        public void setNodename(String nodename) {
            this.nodename = nodename;
        }

        public String getNodedescribe() {
            return nodedescribe;
        }

        public void setNodedescribe(String nodedescribe) {
            this.nodedescribe = nodedescribe;
        }

        public String getNodex() {
            return nodex;
        }

        public void setNodex(String nodex) {
            this.nodex = nodex;
        }

        public String getNodey() {
            return nodey;
        }

        public void setNodey(String nodey) {
            this.nodey = nodey;
        }

        public String getNodesupe() {
            return nodesupe;
        }

        public void setNodesupe(String nodesupe) {
            this.nodesupe = nodesupe;
        }

        public String getNodenext() {
            return nodenext;
        }

        public void setNodenext(String nodenext) {
            this.nodenext = nodenext;
        }

        public String getNodetype() {
            return nodetype;
        }

        public void setNodetype(String nodetype) {
            this.nodetype = nodetype;
        }

        public String getStyledefault() {
            return styledefault;
        }

        public void setStyledefault(String styledefault) {
            this.styledefault = styledefault;
        }

        public String getStylesucceed() {
            return stylesucceed;
        }

        public void setStylesucceed(String stylesucceed) {
            this.stylesucceed = stylesucceed;
        }

        public String getStylenext() {
            return stylenext;
        }

        public void setStylenext(String stylenext) {
            this.stylenext = stylenext;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getOver() {
            return over;
        }

        public void setOver(String over) {
            this.over = over;
        }

        public String getPe1() {
            return pe1;
        }

        public void setPe1(String pe1) {
            this.pe1 = pe1;
        }

        public String getPe2() {
            return pe2;
        }

        public void setPe2(String pe2) {
            this.pe2 = pe2;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getJdzt() {
            return jdzt;
        }

        public void setJdzt(String jdzt) {
            this.jdzt = jdzt;
        }

        public String getLclsid() {
            return lclsid;
        }

        public void setLclsid(String lclsid) {
            this.lclsid = lclsid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCzms() {
            return czms;
        }

        public void setCzms(String czms) {
            this.czms = czms;
        }

        public String getNodetypename() {
            return nodetypename;
        }

        public void setNodetypename(String nodetypename) {
            this.nodetypename = nodetypename;
        }

        public String getCzsj() {
            return czsj;
        }

        public void setCzsj(String czsj) {
            this.czsj = czsj;
        }

        public String getIsSubmit() {
            return isSubmit;
        }

        public void setIsSubmit(String isSubmit) {
            this.isSubmit = isSubmit;
        }

        public String getCzqx() {
            return czqx;
        }

        public void setCzqx(String czqx) {
            this.czqx = czqx;
        }

        public String getJdlsid() {
            return jdlsid;
        }

        public void setJdlsid(String jdlsid) {
            this.jdlsid = jdlsid;
        }

        @Override
        public String toString() {
            return "JdxqBean{" +
                    "id='" + id + '\'' +
                    ", lcid='" + lcid + '\'' +
                    ", nodename='" + nodename + '\'' +
                    ", nodedescribe='" + nodedescribe + '\'' +
                    ", nodex='" + nodex + '\'' +
                    ", nodey='" + nodey + '\'' +
                    ", nodesupe='" + nodesupe + '\'' +
                    ", nodenext='" + nodenext + '\'' +
                    ", nodetype='" + nodetype + '\'' +
                    ", styledefault='" + styledefault + '\'' +
                    ", stylesucceed='" + stylesucceed + '\'' +
                    ", stylenext='" + stylenext + '\'' +
                    ", userid='" + userid + '\'' +
                    ", over='" + over + '\'' +
                    ", pe1='" + pe1 + '\'' +
                    ", pe2='" + pe2 + '\'' +
                    ", duty='" + duty + '\'' +
                    ", jdzt='" + jdzt + '\'' +
                    ", lclsid='" + lclsid + '\'' +
                    ", username='" + username + '\'' +
                    ", czms='" + czms + '\'' +
                    ", nodetypename='" + nodetypename + '\'' +
                    ", czsj='" + czsj + '\'' +
                    ", isSubmit='" + isSubmit + '\'' +
                    ", czqx='" + czqx + '\'' +
                    ", jdlsid='" + jdlsid + '\'' +
                    '}';
        }
    }

    public static class PlBean {
        /**
         * id : 9821u48i23
         * lclsid : a11f5a2441d44dbba5024fd72797862c
         * jdlsid : 212cb647103e4813976c9b277b800c48
         * czms : 啊哈
         * czr :
         * czsj : 2018-12-20 17:04:06
         * czrmc :
         */

        private String id;
        private String lclsid;
        private String jdlsid;
        private String czms;
        private String czr;
        private String czsj;
        private String czrmc;

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

        public String getJdlsid() {
            return jdlsid;
        }

        public void setJdlsid(String jdlsid) {
            this.jdlsid = jdlsid;
        }

        public String getCzms() {
            return czms;
        }

        public void setCzms(String czms) {
            this.czms = czms;
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

        public String getCzrmc() {
            return czrmc;
        }

        public void setCzrmc(String czrmc) {
            this.czrmc = czrmc;
        }

        @Override
        public String toString() {
            return "PlBean{" +
                    "id='" + id + '\'' +
                    ", lclsid='" + lclsid + '\'' +
                    ", jdlsid='" + jdlsid + '\'' +
                    ", czms='" + czms + '\'' +
                    ", czr='" + czr + '\'' +
                    ", czsj='" + czsj + '\'' +
                    ", czrmc='" + czrmc + '\'' +
                    '}';
        }
    }
}
