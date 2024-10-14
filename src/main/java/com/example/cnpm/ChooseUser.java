package com.example.cnpm;

import com.example.cnpm.DatabaseClass.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseUser implements Initializable {
    private double xOffset;
    private double yOffset;
    private String userID;
    @FXML
    private Pane taskBarPane;
    @FXML
    private TextField textField;
    @FXML
    private Label invalidId;

    @FXML
    private void viewProfileStatus_adminView() {
        String staffId = textField.getText();
        int check = DataBaseConnector.INSTANCE.checkUserId(staffId);
        System.out.println("Check = " + check);

        if (check == 0) {
            invalidId.setText("ID nhân viên không tồn tại");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusProfile.fxml"));
            try {
                User user = DataBaseConnector.INSTANCE.getUserProfileFromId(staffId);

                AnchorPane root = loader.load();
                StatusProfile controller = loader.getController();

                // Create a new stage
                Stage newStage = new Stage();
                controller.setUser(user, newStage);
                newStage.setTitle("Xem trạng thái nhân viên");
                newStage.initStyle(StageStyle.UNDECORATED);

                // Set the loaded content onto the new stage
                Scene scene = new Scene(root);
                newStage.setScene(scene);

                // Show the new stage
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @FXML
    public void backAdmin(ActionEvent actionEvent) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeSceneToHomeAdmin("homeadmin.fxml", userID);
    }

    @FXML
    void closeStage() {
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeStage() {
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
