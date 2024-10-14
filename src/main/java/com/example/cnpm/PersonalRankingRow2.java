package com.example.cnpm;

import javafx.beans.property.SimpleStringProperty;

public class PersonalRankingRow2 {
    private final SimpleStringProperty rank;
    private final SimpleStringProperty DepartmentID;
    private final SimpleStringProperty DepartmentName;

    public PersonalRankingRow2(String rank, String DepartmentID, String DepartmentName) {
        this.rank = new SimpleStringProperty(rank);
        this.DepartmentID = new SimpleStringProperty(DepartmentID);
        this.DepartmentName = new SimpleStringProperty(DepartmentName);
    }

    public String getrank() {
        return rank.get();
    }

    public String getDepartmentID() {
        return DepartmentID.get();
    }

    public String getDepartmentName() {
        return DepartmentName.get();
    }
}
