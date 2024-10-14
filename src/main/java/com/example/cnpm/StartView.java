package com.example.cnpm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartView implements Initializable {
    @FXML
    Pane taskBarPane;
    private double xOffset;
    private double yOffset;
    @FXML
    private Text label;

    private
    @FXML void onCheckinClick(MouseEvent event) throws IOException {
        Runtime.getRuntime()
                .exec(String.format("cmd.exe /c python %s", "D:\\Work\\Project\\CNPM_ChamCong\\src\\main\\java\\com\\example\\cnpm\\main_video.py"));
    }

    public void onLoginClick(MouseEvent event) throws IOException {
        com.example.cnpm.HelloApplication tmp = new com.example.cnpm.HelloApplication();
        tmp.changeScene("login.fxml");
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
