package com.example.cnpm.DatabaseClass;
import java.util.Date;

public class RequestChangeSchedule {
    private String requestID;
    private String userID;
    private String requestType;
    private Date requestDate;
    private String status;
    private String userName;

    // Constructors, getters, and setters
    // ...

    // Ví dụ về constructor
    public RequestChangeSchedule(String requestID, String userID, String requestType, Date requestDate, String status, String userName) {
        this.requestID = requestID;
        this.userID = userID;
        this.requestType = requestType;
        this.requestDate = requestDate;
        this.status = status;
        this.userName = userName;
    }
    public String getRequestID() {
        return this.requestID;
    }
    public String getUserID() {
        return this.userID;
    }
    public String getRequestType() {
        return this.requestType;
    }
    public Date getRequestDate() {
        return this.requestDate;
    }
    public String getStatus() {
        return this.status;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

}