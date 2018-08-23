package com.biketomotor.kqj.object;

public class Activity {

    private int    image;
    private String name;
    private String id;
    private String creater;
    private String info;

//    public Activity (String name, int image, String organizer, String info) {
//        this.image = image;
//        this.name = name;
//        this.organizer = organizer;
//        this.info = info;
//    }

    public Activity(int image, String name, String id) {
        this.image = image;
        this.name = name;
        this.id = id;
        this.creater = "none";
        this.info = "This is " + name;
    }

    public Activity(int image, String name, String id, String creater) {
        this.image = image;
        this.name = name;
        this.id = id;
        this.creater = creater;
        this.info = "This is " + name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setCreater(String account) {
        this.creater = account;
    }
    public String getCreater() {
        return creater;
    }

    public String getInfo() {
        return info;
    }
}
