package com.example.cnpm;

import javafx.beans.property.SimpleStringProperty;

public class PersonalRankingRow {
    private final SimpleStringProperty rank;
    private final SimpleStringProperty userID;
    private final SimpleStringProperty name;

    public PersonalRankingRow(String rank, String userID, String name) {
        this.rank = new SimpleStringProperty(rank);
        this.userID = new SimpleStringProperty(userID);
        this.name = new SimpleStringProperty(name);
    }

    public String getrank() {
        return rank.get();
    }

    public String getuserID() {
        return userID.get();
    }

    public String getname() {
        return name.get();
    }
}
