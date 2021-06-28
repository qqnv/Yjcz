package com.zzlecheng.yjcz.bean;

/**
 * @类名: PopBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/2 3:56 PM
 * @版本: 1.0.0
 */
public class PopBean {

    /**
     * dept : 领导班子
     * users : 领导,段长
     */

    private String dept;
    private String users;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "PopBean{" +
                "dept='" + dept + '\'' +
                ", users='" + users + '\'' +
                '}';
    }
}
