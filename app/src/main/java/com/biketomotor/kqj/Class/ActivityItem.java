package com.biketomotor.kqj.Class;

import android.support.annotation.NonNull;

public class ActivityItem implements Comparable<ActivityItem> {

    private String name; // 活动名称
    private String id; // 活动ID
    private String creater; // 活动创建者
    private String info; // 活动详情
    private String startTime; // 活动开始时间
    private String endTime; // 活动签到截止时间

    public ActivityItem(
            String name,
            String id,
            String creater,
            String startTime,
            String endTime) {
        this.name = name;
        this.id = id;
        this.creater = creater;
        this.info = "This is " + this.name + " created by " + this.creater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ActivityItem(String name,
                        String id,
                        String creater,
                        String info,
                        String startTime,
                        String endTime) {
        this.name = name;
        this.id = id;
        this.creater = creater;
        this.info = info;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int compareTo(@NonNull ActivityItem activityItem) {
        return Long.valueOf(this.getStartTime()).compareTo(Long.valueOf(activityItem.getStartTime()));
    }
}
