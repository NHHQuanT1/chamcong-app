package com.example.cnpm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkSchedule1 implements Initializable {
    @FXML
    private TextField employeeNameField;

    @FXML
    private ListView<String> workScheduleListView;

    @FXML
    public void backButtonClicked(ActionEvent actionEvent) {
    }

    private Connection connection;

    @FXML
    private void searchButtonClicked() {
        String employeeName = employeeNameField.getText();
        List<String> workSchedules = getWorkSchedulesForEmployee(employeeName);
        workScheduleListView.getItems().setAll(workSchedules);
    }

    private List<String> getWorkSchedulesForEmployee(String employeeName) {
        List<String> schedules = new ArrayList<>();
        try {
            String databaseName = "database.db";
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);

            String sql = "SELECT WorkDate, Shift FROM WorkSchedule JOIN Users ON WorkSchedule.UserID = Users.UserID WHERE Users.Name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employeeName);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String workDate = resultSet.getString("WorkDate");
                String shift = resultSet.getString("Shift");
                schedules.add("WorkDate: " + workDate + ", Shift: " + shift);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    // ...

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize logic
    }

}

