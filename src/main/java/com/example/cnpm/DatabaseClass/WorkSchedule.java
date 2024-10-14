package com.example.cnpm.DatabaseClass;

import java.util.Date;

public class WorkSchedule {
    private String id;
    private String userID;
    private Date date;
    private String shift;

    public WorkSchedule(String id, String userID, Date date, String shift) {
        this.id = id;
        this.userID = userID;
        this.date = date;
        this.shift = shift;
    }
    public String getId() {
        return this.id;
    }
    public String getUserID() {
        return this.userID;
    }
    public Date getDate() {
        return this.date;
    }
    public String getShift() {
        return this.shift;
    }
    public void setShift(String shift) {
        this.shift = shift;
    }

}
