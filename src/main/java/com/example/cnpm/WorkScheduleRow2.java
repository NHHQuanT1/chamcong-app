package com.example.cnpm;
import javafx.beans.property.SimpleStringProperty;
public class WorkScheduleRow2 {
    private final SimpleStringProperty rank;
    private final SimpleStringProperty userID;
    private final SimpleStringProperty employeeName;
    private final SimpleStringProperty status;

    public WorkScheduleRow2(String rank, String userID, String employeeName,String status) {
        this.rank = new SimpleStringProperty(rank);
        this.userID = new SimpleStringProperty(userID);
        this.employeeName = new SimpleStringProperty(employeeName);
        this.status = new SimpleStringProperty(status);
    }

    public String getrank() {
        return rank.get();
    }

    public String getuserID() {
        return userID.get();
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public String getStatus() { return status.get();
    }

}
