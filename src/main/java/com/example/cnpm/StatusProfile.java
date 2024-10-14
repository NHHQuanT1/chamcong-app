package com.example.cnpm;

import com.example.cnpm.DatabaseClass.RequestChangeSchedule;
import com.example.cnpm.DatabaseClass.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatusProfile implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private User user;
    private Stage stage;
    @FXML
    private Label doiLichLabel;

    @FXML
    private Label lateLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nghiLabel;

    @FXML
    private Label nghiPhepLabel;

    @FXML
    private Label tangCaLabel;
    @FXML
    private Pane taskBarPane;

    public void setUser(User user, Stage stage) {
        this.user = user;
        this.stage = stage;

        nameLabel.setText(user.getName());

        int amountNghi = 0;
        int amountNghiPhep = 0;
        int amountDoiLich = 0;
        int amountTangCa = 0;
        int amountDiMuon = 0;
        System.out.println(user.getId());
        List<RequestChangeSchedule> allRequests = DataBaseConnector.INSTANCE.getLeaveRequestsForUserByUserId(user.getId());
//        db.disconnect();
        for (RequestChangeSchedule request : allRequests) {
            System.out.println("RequestID: " + request.getRequestID() +
                    ", UserID: " + request.getUserID() +
                    // In ra danh sách các LeaveRequest của tất cả người dùng
                    ", RequestType: " + request.getRequestType() +
                    ", RequestDate: " + request.getRequestDate() +
                    ", Status: " + request.getStatus() +
                    ", UserName: " + request.getUserName());
            if (request.getStatus().equals("Accepted")) {
                if (request.getRequestType().equals("Nghỉ")) {
                    amountNghi++;
                }
                if (request.getRequestType().equals("Nghỉ phép")) {
                    amountNghiPhep++;
                }
                if (request.getRequestType().equals("Đổi lịch")) {
                    amountDoiLich++;
                }
                if (request.getRequestType().equals("Tăng ca")) {
                    amountTangCa++;
                }
                if (request.getRequestType().equals("Muộn")) {
                    amountDiMuon++;
                }

            }
        }
        nghiLabel.setText(String.valueOf(amountNghi));
        nghiPhepLabel.setText(String.valueOf(amountNghiPhep));
        doiLichLabel.setText(String.valueOf(amountDoiLich));
        tangCaLabel.setText(String.valueOf(amountTangCa));
        lateLabel.setText(String.valueOf(amountDiMuon));
    }

    @FXML
    public void closeStage() {
        stage.close();
    }

    @FXML
    public void minimizeStage() {
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.taskBarPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Sự kiện khi chuột được kéo (drag)
        this.taskBarPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
