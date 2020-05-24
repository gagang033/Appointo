package com.lenovo.example.appointo;

public class Appointment {

    private String id;
    private String time,date;
    private String free;
    private String status,with;

    public Appointment(){

    }

    public Appointment(String id,String date, String time, String free, String status, String with) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.free = free;
        this.status = status;
        this.with = with;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFree() {
        return free;
    }

    public String getStatus() {
        return status;
    }

    public String getWith() {
        return with;
    }
}
