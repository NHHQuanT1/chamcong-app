package com.example.cnpm;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WorkSchedule2 implements Initializable {
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> shiftComboBox;
    @FXML
    private Label label;
    @FXML
    private Pane taskBarPane;
    private double xOffset;
    private double yOffset;
    private String userID; // Thêm trường dữ liệu để lưu UserID
    @FXML
    private TableView<WorkScheduleRow2> workScheduleTableView;
    @FXML
    private TableColumn<WorkScheduleRow2, String> rankColumn;
    @FXML
    private TableColumn<WorkScheduleRow2, String> userIDColumn;
    @FXML
    private TableColumn<WorkScheduleRow2, String> employeeNameColumn;
    @FXML
    private TableColumn<WorkScheduleRow2, String> statusColumn;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @FXML
    public void backButtonClicked(ActionEvent actionEvent) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToHomeAdmin("homeadmin.fxml", userID);
    }

    @FXML
    private void searchButtonClicked() {
        LocalDate selectedDate = datePicker.getValue();
        String formattedDate = selectedDate.format(DateTimeFormatter.ISO_DATE);
        String selectedShift = shiftComboBox.getValue(); // Lấy giá trị từ ComboBox

        // Kiểm tra xem ngày và ca làm việc đã được chọn chưa
        if (selectedDate != null && selectedShift != null) {
            ObservableList<WorkScheduleRow2> workSchedules = getWorkSchedulesForDateAndShift(formattedDate, selectedShift);
            workScheduleTableView.setItems(workSchedules);
        }
    }


    private ObservableList<WorkScheduleRow2> getWorkSchedulesForDateAndShift(String selectedDate, String selectedShift) {
        ObservableList<WorkScheduleRow2> schedules = FXCollections.observableArrayList();
        try {
            String sql = "SELECT WorkSchedule.WorkDate, WorkSchedule.Shift, Users.UserID, Users.Name, Attendance.CheckInTime " +
                    "FROM WorkSchedule " +
                    "JOIN Users ON WorkSchedule.UserID = Users.UserID " +
                    "LEFT JOIN Attendance ON WorkSchedule.UserID = Attendance.UserID AND WorkSchedule.WorkDate = Attendance.WorkDate " +
                    "WHERE WorkSchedule.WorkDate = ? AND WorkSchedule.Shift = ?";

            PreparedStatement statement = DataBaseConnector.INSTANCE.getConnection().prepareStatement(sql);
            statement.setString(1, selectedDate);
            statement.setString(2, selectedShift);

            ResultSet resultSet = statement.executeQuery();

            int rank = 1;
            while (resultSet.next()) {
                String workDate = resultSet.getString("WorkDate");
                String shift = resultSet.getString("Shift");
                String userId = resultSet.getString("UserID");
                String userName = resultSet.getString("Name");
                String checkInTime = resultSet.getString("CheckInTime");

                // Kiểm tra xem có dữ liệu check-in không
                String status = checkInTime == null ? "Nghỉ" : "Đúng giờ"; // Mặc định là "Đúng giờ" nếu có check-in

                // Kiểm tra thời gian check-in để xác định muộn
                if (checkInTime != null && !checkInTime.isEmpty()) {
                    if (selectedShift.equals("Sáng")) {
                        String checkInHour = checkInTime.split(" ")[1]; // Lấy giờ từ thời gian check-in
                        int hour = Integer.parseInt(checkInHour.split(":")[0]);
                        int minute = Integer.parseInt(checkInHour.split(":")[1]);

                        if (hour > 8 || (hour == 8 && minute > 0)) {
                            status = "Muộn ca sáng";
                        }
                    } else if (selectedShift.equals("Chiều")) {
                        String checkInHour = checkInTime.split(" ")[1]; // Lấy giờ từ thời gian check-in
                        int hour = Integer.parseInt(checkInHour.split(":")[0]);
                        int minute = Integer.parseInt(checkInHour.split(":")[1]);

                        if (hour > 13 || (hour == 13 && minute > 30)) {
                            status = "Muộn ca chiều";
                        }
                    }
                }

                schedules.add(new WorkScheduleRow2(String.valueOf(rank), userId, userName, status));
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> shiftOptions = FXCollections.observableArrayList("Sáng", "Chiều");
        shiftComboBox.setItems(shiftOptions);

        rankColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getrank()));
        userIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getuserID()));
        employeeNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployeeName()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        // Initialize table view with empty data or perform other initialization tasks
        workScheduleTableView.setItems(FXCollections.observableArrayList());

        taskBarPane.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        taskBarPane.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    @FXML
    void closeStage() {
        Stage stage = (Stage) label.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeStage() {
        Stage stage = (Stage) label.getScene().getWindow();
        stage.setIconified(true);
    }
}
