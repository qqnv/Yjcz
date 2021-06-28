package com.zzlecheng.yjcz.eventbus;

/**
 * @类名: PushEventBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/10 2:18 PM
 * @版本: 1.0.0
 */
public class PushEventBean {

    private String pushName;

    public PushEventBean(String pushName) {
        this.pushName = pushName;
    }


    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    @Override
    public String toString() {
        return "PushEventBean{" +
                ", pushName='" + pushName + '\'' +
                '}';
    }
}
