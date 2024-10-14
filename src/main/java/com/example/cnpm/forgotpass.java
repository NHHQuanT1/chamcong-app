package com.example.cnpm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class forgotpass implements Initializable {
    @FXML
    Pane taskBarPane;
    private double xOffset;
    private double yOffset;
    @FXML
    private Label done;

    @FXML
    private TextField emailinfor;

    @FXML
    private Button sendrequest;

    @FXML
    public void sendrequest(ActionEvent event) throws IOException {
        checkrequest();
    }

    private void checkrequest() throws IOException {
        if (emailinfor.getText().isEmpty() || emailinfor.getText().equals("email")) {
            done.setText("Vui lòng nhập đủ dữ liệu");
        } else {
            done.setText("Yêu cầu đã được gửi");
        }
    }

    @FXML
    void backlogin(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeScene("login.fxml");
    }

    @FXML
    void closeStage() {
        Stage stage = (Stage) done.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeStage() {
        Stage stage = (Stage) done.getScene().getWindow();
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
