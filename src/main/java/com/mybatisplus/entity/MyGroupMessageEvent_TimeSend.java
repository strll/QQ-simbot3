package com.mybatisplus.entity;

import love.forte.simbot.event.GroupMessageEvent;

public class MyGroupMessageEvent_TimeSend {
    private GroupMessageEvent groupMessageEvent;

    public MyGroupMessageEvent_TimeSend () {
    }

    public GroupMessageEvent getGroupMessageEvent() {
        return groupMessageEvent;
    }

    public void setGroupMessageEvent(GroupMessageEvent groupMessageEvent) {
        this.groupMessageEvent = groupMessageEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyGroupMessageEvent_TimeSend  that = (MyGroupMessageEvent_TimeSend ) o;

        return groupMessageEvent != null ? groupMessageEvent.getGroup().getId().toString().equals(that.groupMessageEvent.getGroup().getId().toString()) : that.groupMessageEvent.getGroup().getId().toString() == null;
    }

    @Override
    public int hashCode() {
        return groupMessageEvent != null ?  Integer.parseInt(groupMessageEvent.getGroup().getId().toString())  : 0;
    }

    public MyGroupMessageEvent_TimeSend (GroupMessageEvent groupMessageEvent) {
        this.groupMessageEvent = groupMessageEvent;
    }
}
