package com.zzlecheng.yjcz.eventbus;

/**
 * @类名: ClusterNameBean
 * @描述: 显示选定组的成员
 * @作者: huangchao
 * @时间: 2019/1/29 3:09 PM
 * @版本: 1.0.0
 */
public class ClusterNameBean {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClusterNameBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
