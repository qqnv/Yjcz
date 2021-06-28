package com.zzlecheng.yjcz.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @类名: ReportTypesBean
 * @描述:
 * @作者: huangchao
 * @时间: 2018/9/4 上午9:46
 * @版本: 1.0.0
 */
public class ReportTypesBean implements Serializable {


    private List<String> workZZJG;
    private List<WorkTypeBean> workType;
    private List<String> workUser;

    public List<String> getWorkZZJG() {
        return workZZJG;
    }

    public void setWorkZZJG(List<String> workZZJG) {
        this.workZZJG = workZZJG;
    }

    public List<WorkTypeBean> getWorkType() {
        return workType;
    }

    public void setWorkType(List<WorkTypeBean> workType) {
        this.workType = workType;
    }

    public List<String> getWorkUser() {
        return workUser;
    }

    public void setWorkUser(List<String> workUser) {
        this.workUser = workUser;
    }

    public static class WorkTypeBean {
        /**
         * begins : 08:00:00
         * coding : 1
         * ends : 24:00:00
         * late : 1
         * span : 1
         * types : 全天
         */

        private String begins;
        private String coding;
        private String ends;
        private String late;
        private String span;
        private String types;

        public String getBegins() {
            return begins;
        }

        public void setBegins(String begins) {
            this.begins = begins;
        }

        public String getCoding() {
            return coding;
        }

        public void setCoding(String coding) {
            this.coding = coding;
        }

        public String getEnds() {
            return ends;
        }

        public void setEnds(String ends) {
            this.ends = ends;
        }

        public String getLate() {
            return late;
        }

        public void setLate(String late) {
            this.late = late;
        }

        public String getSpan() {
            return span;
        }

        public void setSpan(String span) {
            this.span = span;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return "WorkTypeBean{" +
                    "begins='" + begins + '\'' +
                    ", coding='" + coding + '\'' +
                    ", ends='" + ends + '\'' +
                    ", late='" + late + '\'' +
                    ", span='" + span + '\'' +
                    ", types='" + types + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ReportTypesBean{" +
                "workZZJG=" + workZZJG +
                ", workType=" + workType +
                ", workUser=" + workUser +
                '}';
    }
}
