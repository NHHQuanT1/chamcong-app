package com.example.cnpm;

import com.example.cnpm.DatabaseClass.RequestChangeSchedule;
import com.example.cnpm.DatabaseClass.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ChangeScheduleAdminController implements Initializable {
    private User user;
    private Stage stage;
    @FXML
    private VBox vboxAddRequests;

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(date);
    }

    public void setUser(User user, Stage stage) throws IOException {
        this.user = user;
        this.stage = stage;
        List<RequestChangeSchedule> allRequests = DataBaseConnector.INSTANCE.getLeaveRequestsForUser();
        for (RequestChangeSchedule request : allRequests) {
            System.out.println("RequestID: " + request.getRequestID() +
                    ", UserID: " + request.getUserID() +
                    // In ra danh sách các LeaveRequest của tất cả người dùng
                    ", RequestType: " + request.getRequestType() +
                    ", RequestDate: " + request.getRequestDate() +
                    ", Status: " + request.getStatus() +
                    ", UserName: " + request.getUserName());
            if (request.getStatus().equals("Pending")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ApproveBar.fxml"));
                AnchorPane boxFind = loader.load();
                ControlApproveBar controller = loader.getController();
                controller.setRequestsData(request);
                Label nameLabel = (Label) boxFind.lookup("#contentLabel");
                Date requestDate = request.getRequestDate(); // Lấy ngày từ đối tượng RequestChangeSchedule
                String formattedDate = formatDate(requestDate); // Chuyển đổi định dạng ngày
                nameLabel.setText(request.getUserName() + " - " + request.getRequestType() + " - " + formattedDate);
                vboxAddRequests.getChildren().add(boxFind);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
