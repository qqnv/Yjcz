package com.zzlecheng.yjcz.bean;

import java.util.List;

/**
 * @类名: PopNewBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/2 4:21 PM
 * @版本: 1.0.0
 */
public class PopNewBean {
    private String bm;
    private List<RyBean> ryBeanList;

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public List<RyBean> getRyBeanList() {
        return ryBeanList;
    }

    public void setRyBeanList(List<RyBean> ryBeanList) {
        this.ryBeanList = ryBeanList;
    }

    @Override
    public String toString() {
        return "PopNewBean{" +
                "bm='" + bm + '\'' +
                ", ryBeanList=" + ryBeanList +
                '}';
    }

    public static class RyBean {

        private String ry;

        public String getRy() {
            return ry;
        }

        public void setRy(String ry) {
            this.ry = ry;
        }

        @Override
        public String toString() {
            return "RyBean{" +
                    "ry='" + ry + '\'' +
                    '}';
        }
    }
}
