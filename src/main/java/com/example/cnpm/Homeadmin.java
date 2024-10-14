package com.example.cnpm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Homeadmin implements Initializable {
    @FXML
    private Button viewingUserStatus;

    @FXML
    private Button currentlist;

    @FXML
    private Button rankingall;

    @FXML
    private Button rankingalldepartment;

    @FXML
    private Label setname;

    @FXML
    private Label setemail;

    @FXML
    private Label setphone;

    @FXML
    private Pane taskBarPane;
    private double xOffset;
    private double yOffset;

    private String userID; // Thêm trường dữ liệu để lưu UserID

    // Hiểm thị thông tin admin
    public void setUserID(String userID) {
        this.userID = userID;
        // Thực hiện truy vấn để lấy Name từ database dựa trên userID

        // Gọi phương thức để lấy Name từ UserID
        String userName = DataBaseConnector.INSTANCE.getUserInfoByUserID(userID, 1);
        // Hiển thị Name lên Label setname
        setname.setText(userName + " ( Admin )");

        // Gọi phương thức để lấy Phone từ UserID
        String Phone = DataBaseConnector.INSTANCE.getUserInfoByUserID(userID, 2);
        // Hiển thị phone lên Label setphone
        setphone.setText(Phone);

        // Gọi phương thức để lấy email từ UserID
        String Email1 = DataBaseConnector.INSTANCE.getUserInfoByUserID(userID, 3);
        // Hiển thị email lên Label setemail
        setemail.setText(Email1);
    }

    @FXML
    void currentlist(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToWorkSchedule2("WorkSchedule2.fxml", userID);
    }

    @FXML
    void rankingall(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToPersonalRanking("PersonalRanking.fxml", userID);
    }

    @FXML
    void rankingalldepartment(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToPersonalRanking2("PersonalRanking2.fxml", userID);
    }

    @FXML
    void viewingUserStatus(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToChooseUser("ChooseUser.fxml", userID);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeScene("login.fxml");
    }

    @FXML
    void closeStage() {
        Stage stage = (Stage) setname.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeStage() {
        Stage stage = (Stage) setname.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

}
