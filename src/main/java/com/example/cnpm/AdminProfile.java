package com.example.cnpm;

import com.example.cnpm.DatabaseClass.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminProfile implements Initializable {
    public User user;
    @FXML
    private TextField roomTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField mailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField sexTextField;
    @FXML
    private Button chinhSuaBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Circle avaPlace;
    private boolean isEditing = false;

    // Hàm xử lý khi nút "Chỉnh sửa" được nhấn
    @FXML
    private void onChinhSuaClicked() {
        isEditing = !isEditing;
        setFieldsEditable(isEditing);
    }

    // Hàm xử lý khi nút "Lưu" được nhấn
    @FXML
    private void onSaveClicked() {
        // Lưu thông tin vào database
        isEditing = false;
        setFieldsEditable(false);
        User user = new User(this.user.getId(), nameTextField.getText(), mailTextField.getText(), roomTextField.getText(), phoneTextField.getText(), positionTextField.getText());
        DataBaseConnector.INSTANCE.updateProfile(user);
    }

    // Hàm để thiết lập tính chất chỉnh sửa cho các TextField
    public void setUser(User user) {
        this.user = user;
        nameTextField.setText(user.getName());
        mailTextField.setText(user.getEmail());
        phoneTextField.setText(user.getPhone());
        roomTextField.setText(user.getDepartment());
        positionTextField.setText(user.getRole());
    }

    private void setFieldsEditable(boolean editable) {
        nameTextField.setEditable(editable);
        mailTextField.setEditable(editable);
        phoneTextField.setEditable(editable);

        saveBtn.setDisable(!editable);
        chinhSuaBtn.setDisable(editable);
    }

    @FXML
    private void changeAvatar() {
        //Chọn ảnh từ thư viện
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ảnh Files", "*.jpg", "*.png")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);

            // Áp dụng hình ảnh vào avaPlace (giả sử avaPlace là một đối tượng hình chữ nhật hoặc hình tròn)
            avaPlace.setFill(new javafx.scene.paint.ImagePattern(image));
        }
    }

    @FXML
    private void viewScheduleStaff() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarView.fxml"));
        try {
            AnchorPane root = loader.load();
            CalendarView controller = loader.getController();
            controller.setUser(user);
            // Create a new stage
            Stage newStage = new Stage();
            newStage.setTitle("Lịch làm việc");
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

    @FXML
    private void listRequestChangeSchedule() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewRequestLeaveStaff.fxml"));
        try {
            AnchorPane root = loader.load();
            ChangeScheduleAdminController controller = loader.getController();
            Stage newStage = new Stage();
            controller.setUser(user, newStage);
            // Create a new stage

            newStage.setTitle("Đăng ký thay đổi lịch làm việc");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo trạng thái ban đầu của các TextField
        setFieldsEditable(false);
        roomTextField.setEditable(false);
        positionTextField.setEditable(false);
        avaPlace.setStroke(Color.SEAGREEN);
    }
}
