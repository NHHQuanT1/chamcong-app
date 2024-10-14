package com.example.cnpm;

import com.example.cnpm.DatabaseClass.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ChangeScheduleStaffController implements Initializable {
    private double xOffset;
    private double yOffset;
    private User user;
    private Stage stage;
    @FXML
    private ChoiceBox requestCb;
    @FXML
    private DatePicker dateMove;
    @FXML
    private Pane taskBarPane;

    public void setUser(User user, Stage stage) {
        this.user = user;
        this.stage = stage;
        requestCb.getItems().addAll("Nghỉ", "Muộn", "Nghỉ phép", "Tăng ca", "Đổi lịch");
    }

    @FXML
    void setRequestTypeTextField() {
        String requestType = (String) requestCb.getValue();
        LocalDate date = dateMove.getValue();
        DataBaseConnector.INSTANCE.sentRequestFromIdAndTime(user.getId(), requestType, Date.valueOf(date));
        stage.close();
    }

    @FXML
    void setCancel() {
        stage.close();
    }

    @FXML
    void closeStage() {
        stage.close();
    }

    @FXML
    void minimizeStage() {
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
