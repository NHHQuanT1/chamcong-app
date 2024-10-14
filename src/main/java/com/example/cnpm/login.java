package com.example.cnpm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class login implements Initializable {
    @FXML
    Pane taskBarPane;
    private double xOffset;
    private double yOffset;
    @FXML
    private PasswordField pass;

    @FXML
    private TextField username;

    @FXML
    private Label wronglogin;

    @FXML
    void forgotpass(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeScene("forgotpass.fxml");
    }

    @FXML
    void backToStartView(ActionEvent event) throws IOException {
        HelloApplication change = new HelloApplication();
        change.changeScene("start-view.fxml");
    }

    @FXML
    public void loginButton(ActionEvent event) throws IOException {
        checklogin();
    }

    private void checklogin() throws IOException {
        HelloApplication m = new HelloApplication();

        if (username.getText().isEmpty() || pass.getText().isEmpty()) {
            wronglogin.setText("Vui lòng nhập đủ dữ liệu");
        } else {
            // Sử dụng thử phương thức kiemTraDangNhap từ DataBaseConnector

            // Gọi phương thức kiemTraDangNhap
            String userID = DataBaseConnector.INSTANCE.kiemTraDangNhap(username.getText(), pass.getText());

            if (userID != null) {
                // Kiểm tra RoleID
                int roleID = DataBaseConnector.INSTANCE.getRoleID(userID);
                // Thực hiện các xử lý tiếp theo dựa trên RoleID
                if (roleID == 1) {
                    m.changeSceneToHomeAdmin("homeadmin.fxml", userID);
                } else {
                    m.changeSceneToHomeuser("UserProfile.fxml", userID);
                }
            } else {
                wronglogin.setText("Tên đăng nhập hoặc mật khẩu không đúng");
            }
        }
    }

    @FXML
    void closeStage() {
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimizeStage() {
        Stage stage = (Stage) username.getScene().getWindow();
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