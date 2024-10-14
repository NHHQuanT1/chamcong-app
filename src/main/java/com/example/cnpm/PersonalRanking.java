package com.example.cnpm;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PersonalRanking implements Initializable {
    @FXML
    private TableView<PersonalRankingRow> personalRankingTableView;
    @FXML
    private TableColumn<PersonalRankingRow, String> rankColumn;
    @FXML
    private TableColumn<PersonalRankingRow, String> userIDColumn;
    @FXML
    private TableColumn<PersonalRankingRow, String> nameColumn;
    @FXML
    private Label label;
    @FXML
    private Pane taskBarPane;
    private Stage stage;
    private double xOffset;
    private double yOffset;
    private String userID; // Thêm trường dữ liệu để lưu UserID

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @FXML
    public void backButtonClicked(ActionEvent actionEvent) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToHomeAdmin("homeadmin.fxml", userID);
    }

    @FXML
    public void initialize() {
        // Gọi phương thức để thực hiện việc xếp hạng
        rankEmployees();
    }

    @FXML
    private void handleSortButton(ActionEvent event) {
        // Gọi phương thức để thực hiện việc xếp hạng khi nút sắp xếp được nhấn
        rankEmployees();
    }

    private void rankEmployees() {
        try {
            String query = "SELECT u.UserID, u.Name, " +
                    "(SELECT COUNT(*) FROM Attendance WHERE UserID = u.UserID AND Late = true) AS late_count, " +
                    "(SELECT COUNT(*) FROM LeaveRequest WHERE UserID = u.UserID AND RequestType = 'Nghỉ phép' AND Status = 'Confirm') AS leave_count, " +
                    "(SELECT COUNT(*) FROM LeaveRequest WHERE UserID = u.UserID AND RequestType = 'Tăng ca' AND Status = 'Confirm') AS overtime_count " +
                    "FROM Users u";

            PreparedStatement statement = DataBaseConnector.INSTANCE.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<PersonalRankingRow> rankings = FXCollections.observableArrayList();

            int rank = 1;
            while (resultSet.next()) {
                String userId = resultSet.getString("UserID");
                String name = resultSet.getString("Name");
                int lateCount = resultSet.getInt("late_count");
                int leaveCount = resultSet.getInt("leave_count");
                int overtimeCount = resultSet.getInt("overtime_count");

                // Calculate ranking based on your criteria
                int totalPoints = -lateCount * 2 - leaveCount * 3 + overtimeCount * 5;

                // Create PersonalRankingRow object and add to the list
                rankings.add(new PersonalRankingRow(String.valueOf(rank), userId, name));
                rank++;
            }

            // Clear previous items in the TableView
            personalRankingTableView.getItems().clear();

            // Set the items in TableView
            personalRankingTableView.setItems(rankings);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rankColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getrank()));
        userIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getuserID()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getname()));

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