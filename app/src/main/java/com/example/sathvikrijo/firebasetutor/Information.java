package com.example.sathvikrijo.firebasetutor;

public class Information {
    public String msg;
    public String name;
    public String id;

    public Information() {

    }

    public Information(String msg, String name, String id) {
        this.msg = msg;
        this.name = name;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setName(String name) {
        this.name = name;
    }
}
