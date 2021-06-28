package com.zzlecheng.yjcz.bean;

import java.util.List;

/**
 * @类名: ProcedureDealBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/12 5:38 PM
 * @版本: 1.0.0
 */
public class ProcedureDealBean {

    /**
     * lcxq : {"id":"11","lclsid":"1","jdid":"f2fb4f5771404825957d353dae3acd59","czms":"操作描述","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"操作时间","czjg":"操作结果","jdname":"节点1-5","czrmc":"段长","sj":"11111111","czlx":"","czlxmc":""}
     * fj : []
     * pl : [{"plxq":{"id":"12","lclsid":"11","jdlsid":"f2fb4f5771404825957d353dae3acd59","czms":"评论描述1231","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"2012-12-12","czrmc":"段长"},"plfj":[{"id":"0dacef81a1c2489ab8373e89b342d31f","ysmc":"测试账号.txt","wjmc":"20181212135939028.txt","scsj":"2018-12-12 13:59:39","lj":"http://192.168.100.184:8080/yjcz/upload/download?imagePath=D://upload//2018//12//12//20181129100440984.png","lx":".png","pid":"12","lb":"","dx":""},{"id":"9b280674e52c4bbb832f48ca9e64d999","ysmc":"测试账号.txt","wjmc":"20181212140752834.txt","scsj":"2018-12-12 14:07:52","lj":"http://192.168.100.184:8080/yjcz/upload/download?imagePath=D://upload//2018//12//12//20181129100644004.jpg","lx":".jpg","pid":"12","lb":"","dx":""}]},{"plxq":{"id":"14","lclsid":"11","jdlsid":"f2fb4f5771404825957d353dae3acd59","czms":"评论描述1231","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"2012-12-12","czrmc":"段长"},"plfj":[]},{"plxq":{"id":"13","lclsid":"11","jdlsid":"f2fb4f5771404825957d353dae3acd59","czms":"评论描述1231","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"2012-12-12","czrmc":"段长"},"plfj":[]}]
     */

    private LcxqBean lcxq;
    private List<?> fj;
    private List<PlBean> pl;

    public LcxqBean getLcxq() {
        return lcxq;
    }

    public void setLcxq(LcxqBean lcxq) {
        this.lcxq = lcxq;
    }

    public List<?> getFj() {
        return fj;
    }

    public void setFj(List<?> fj) {
        this.fj = fj;
    }

    public List<PlBean> getPl() {
        return pl;
    }

    public void setPl(List<PlBean> pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return "ProcedureDealBean{" +
                "lcxq=" + lcxq +
                ", fj=" + fj +
                ", pl=" + pl +
                '}';
    }

    public static class LcxqBean {
        /**
         * id : 11
         * lclsid : 1
         * jdid : f2fb4f5771404825957d353dae3acd59
         * czms : 操作描述
         * czr : e990f8a6609b417f88a5eb0ede2264cb
         * czsj : 操作时间
         * czjg : 操作结果
         * jdname : 节点1-5
         * czrmc : 段长
         * sj : 11111111
         * czlx :
         * czlxmc :
         */

        private String id;//节点历史ID
        private String lclsid;//流程历史ID
        private String jdid;
        private String czms;
        private String czr;
        private String czsj;
        private String czjg;
        private String jdname;
        private String czrmc;
        private String sj;
        private String czlx;
        private String czlxmc;

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

        public String getJdid() {
            return jdid;
        }

        public void setJdid(String jdid) {
            this.jdid = jdid;
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

        public String getCzjg() {
            return czjg;
        }

        public void setCzjg(String czjg) {
            this.czjg = czjg;
        }

        public String getJdname() {
            return jdname;
        }

        public void setJdname(String jdname) {
            this.jdname = jdname;
        }

        public String getCzrmc() {
            return czrmc;
        }

        public void setCzrmc(String czrmc) {
            this.czrmc = czrmc;
        }

        public String getSj() {
            return sj;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public String getCzlx() {
            return czlx;
        }

        public void setCzlx(String czlx) {
            this.czlx = czlx;
        }

        public String getCzlxmc() {
            return czlxmc;
        }

        public void setCzlxmc(String czlxmc) {
            this.czlxmc = czlxmc;
        }

        @Override
        public String toString() {
            return "LcxqBean{" +
                    "id='" + id + '\'' +
                    ", lclsid='" + lclsid + '\'' +
                    ", jdid='" + jdid + '\'' +
                    ", czms='" + czms + '\'' +
                    ", czr='" + czr + '\'' +
                    ", czsj='" + czsj + '\'' +
                    ", czjg='" + czjg + '\'' +
                    ", jdname='" + jdname + '\'' +
                    ", czrmc='" + czrmc + '\'' +
                    ", sj='" + sj + '\'' +
                    ", czlx='" + czlx + '\'' +
                    ", czlxmc='" + czlxmc + '\'' +
                    '}';
        }
    }

    public static class PlBean {
        /**
         * plxq : {"id":"12","lclsid":"11","jdlsid":"f2fb4f5771404825957d353dae3acd59","czms":"评论描述1231","czr":"e990f8a6609b417f88a5eb0ede2264cb","czsj":"2012-12-12","czrmc":"段长"}
         * plfj : [{"id":"0dacef81a1c2489ab8373e89b342d31f","ysmc":"测试账号.txt","wjmc":"20181212135939028.txt","scsj":"2018-12-12 13:59:39","lj":"http://192.168.100.184:8080/yjcz/upload/download?imagePath=D://upload//2018//12//12//20181129100440984.png","lx":".png","pid":"12","lb":"","dx":""},{"id":"9b280674e52c4bbb832f48ca9e64d999","ysmc":"测试账号.txt","wjmc":"20181212140752834.txt","scsj":"2018-12-12 14:07:52","lj":"http://192.168.100.184:8080/yjcz/upload/download?imagePath=D://upload//2018//12//12//20181129100644004.jpg","lx":".jpg","pid":"12","lb":"","dx":""}]
         */

        private PlxqBean plxq;
        private List<PlfjBean> plfj;

        public PlxqBean getPlxq() {
            return plxq;
        }

        public void setPlxq(PlxqBean plxq) {
            this.plxq = plxq;
        }

        public List<PlfjBean> getPlfj() {
            return plfj;
        }

        public void setPlfj(List<PlfjBean> plfj) {
            this.plfj = plfj;
        }

        @Override
        public String toString() {
            return "PlBean{" +
                    "plxq=" + plxq +
                    ", plfj=" + plfj +
                    '}';
        }

        public static class PlxqBean {
            /**
             * id : 12
             * lclsid : 11
             * jdlsid : f2fb4f5771404825957d353dae3acd59
             * czms : 评论描述1231
             * czr : e990f8a6609b417f88a5eb0ede2264cb
             * czsj : 2012-12-12
             * czrmc : 段长
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
                return "PlxqBean{" +
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

        public static class PlfjBean {
            /**
             * id : 0dacef81a1c2489ab8373e89b342d31f
             * ysmc : 测试账号.txt
             * wjmc : 20181212135939028.txt
             * scsj : 2018-12-12 13:59:39
             * lj : http://192.168.100.184:8080/yjcz/upload/download?imagePath=D://upload//2018//12//12//20181129100440984.png
             * lx : .png
             * pid : 12
             * lb :
             * dx :
             */

            private String id;
            private String ysmc;
            private String wjmc;
            private String scsj;
            private String lj;
            private String lx;
            private String pid;
            private String lb;
            private String dx;
            private String types;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getYsmc() {
                return ysmc;
            }

            public void setYsmc(String ysmc) {
                this.ysmc = ysmc;
            }

            public String getWjmc() {
                return wjmc;
            }

            public void setWjmc(String wjmc) {
                this.wjmc = wjmc;
            }

            public String getScsj() {
                return scsj;
            }

            public void setScsj(String scsj) {
                this.scsj = scsj;
            }

            public String getLj() {
                return lj;
            }

            public void setLj(String lj) {
                this.lj = lj;
            }

            public String getLx() {
                return lx;
            }

            public void setLx(String lx) {
                this.lx = lx;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getLb() {
                return lb;
            }

            public void setLb(String lb) {
                this.lb = lb;
            }

            public String getDx() {
                return dx;
            }

            public void setDx(String dx) {
                this.dx = dx;
            }

            public String getTypes() {
                return types;
            }

            public void setTypes(String types) {
                this.types = types;
            }

            @Override
            public String toString() {
                return "PlfjBean{" +
                        "id='" + id + '\'' +
                        ", ysmc='" + ysmc + '\'' +
                        ", wjmc='" + wjmc + '\'' +
                        ", scsj='" + scsj + '\'' +
                        ", lj='" + lj + '\'' +
                        ", lx='" + lx + '\'' +
                        ", pid='" + pid + '\'' +
                        ", lb='" + lb + '\'' +
                        ", dx='" + dx + '\'' +
                        ", types='" + types + '\'' +
                        '}';
            }
        }
    }
}
