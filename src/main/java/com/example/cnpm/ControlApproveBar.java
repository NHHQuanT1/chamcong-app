package com.example.cnpm;

import com.example.cnpm.DatabaseClass.RequestChangeSchedule;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControlApproveBar {
    private RequestChangeSchedule request;

    private VBox findVBoxParent(Node node) {
        Parent parent = node.getParent();
        while (parent != null) {
            if (parent instanceof VBox) {
                return (VBox) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    @FXML
    public void approve(MouseEvent mouseEvent) {
        DataBaseConnector.INSTANCE.changeStatusRequests(request.getRequestID(), "Accepted");
        Node sourceNode = (Node) mouseEvent.getSource();
        VBox vbox = findVBoxParent(sourceNode);
        System.out.println(vbox);
        if (vbox != null) {
            vbox.getChildren().remove(sourceNode);
        }
    }

    @FXML
    public void decline(MouseEvent mouseEvent) {
        DataBaseConnector.INSTANCE.changeStatusRequests(request.getRequestID(), "Declined");
        Node sourceNode = (Node) mouseEvent.getSource();
        VBox vbox = findVBoxParent(sourceNode);

        if (vbox != null) {
            vbox.getChildren().remove(sourceNode);
        }
    }

    public void setRequestsData(RequestChangeSchedule request) {
        this.request = request;
    }

}
