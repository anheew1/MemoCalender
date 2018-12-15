package com.kmu.myapplication;

public class EventData {
    private String name;
    private String memo;
    private String date;
    public EventData(String name,String date){
        this.name = name;
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }

    public String getName() {
        return name;
    }

}
