package com.biketomotor.kqj.Class;

public class ActivityItem {

    private String name; // 活动名称
    private String id; // 活动ID
    private String creater; // 活动创建者
    private String info; // 活动详情

    public ActivityItem(String name, String id, String creater) {
        this.name = name;
        this.id = id;
        this.creater = creater;
        this.info = "This is " + this.name + " created by " + this.creater;
    }

    public ActivityItem(String name, String id, String creater, String info) {
        this.name = name;
        this.id = id;
        this.creater = creater;
        this.info = info;
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

}
