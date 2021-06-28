package com.zzlecheng.yjcz.eventbus;

import java.util.List;

/**
 * @类名: EventBusBean
 * @描述:
 * @作者: huangchao
 * @时间: 2019/1/18 11:13 AM
 * @版本: 1.0.0
 */
public class EventBusBean {

    private List<String> talkGroupId;
    private List<String> talkGroupName;
    private List<List<String>> djzcy;
    private String currentSpeaker;

    public EventBusBean(List<String> talkGroupId, List<String> talkGroupName, List<List<String>> djzcy, String currentSpeaker) {
        this.talkGroupId = talkGroupId;
        this.talkGroupName = talkGroupName;
        this.djzcy = djzcy;
        this.currentSpeaker = currentSpeaker;
    }

    public List<String> getTalkGroupId() {
        return talkGroupId;
    }

    public void setTalkGroupId(List<String> talkGroupId) {
        this.talkGroupId = talkGroupId;
    }

    public List<String> getTalkGroupName() {
        return talkGroupName;
    }

    public void setTalkGroupName(List<String> talkGroupName) {
        this.talkGroupName = talkGroupName;
    }

    public List<List<String>> getDjzcy() {
        return djzcy;
    }

    public void setDjzcy(List<List<String>> djzcy) {
        this.djzcy = djzcy;
    }

    public String getCurrentSpeaker() {
        return currentSpeaker;
    }

    public void setCurrentSpeaker(String currentSpeaker) {
        this.currentSpeaker = currentSpeaker;
    }

    @Override
    public String toString() {
        return "EventBusBean{" +
                "talkGroupId=" + talkGroupId +
                ", talkGroupName=" + talkGroupName +
                ", djzcy=" + djzcy +
                ", currentSpeaker='" + currentSpeaker + '\'' +
                '}';
    }
}
