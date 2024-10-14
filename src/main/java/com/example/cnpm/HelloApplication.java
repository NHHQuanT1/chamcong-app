package com.example.cnpm;

import com.example.cnpm.DatabaseClass.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stg;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataBaseConnector.init();
        stg = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("start-view.fxml"));
        primaryStage.setTitle("Hệ thống quản lý chấm công");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public void changeSceneToHomeAdmin(String fxml, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent parent = loader.load();

        // Truyền UserID vào Homeadmin controller
        Homeadmin homeadminController = loader.getController();
        homeadminController.setUserID(userID);

        Scene scene = new Scene(parent);
        stg.setScene(scene);
        stg.show();
    }

    public void changeSceneToHomeuser(String fxml, String userID) throws IOException {
        // Truyền UserID vào Homeadmin controller
        User user = DataBaseConnector.INSTANCE.getUserProfileFromId(userID);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        Parent root = fxmlLoader.load();
        // Create a new stage
        UserProfile controller = fxmlLoader.getController();
        System.out.println(user.getName());
        controller.setUser(user, stg);
        stg.setTitle("Hệ thống quản lý chấm công");
        stg.setScene(new Scene(root, 600, 400));
        stg.show();
    }

    public void changeSceneToChooseUser(String fxml, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent parent = loader.load();

        // Truyền UserID vào controller
        ChooseUser controller = loader.getController();
        controller.setUserID(userID);

        Scene scene = new Scene(parent);
        stg.setScene(scene);
        stg.show();
    }

    public void changeSceneToWorkSchedule2(String fxml, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent parent = loader.load();

        // Truyền UserID vào Homeadmin controller
        WorkSchedule2 homeadminController = loader.getController();
        homeadminController.setUserID(userID);

        Scene scene = new Scene(parent);
        stg.setScene(scene);
        stg.show();
    }

    public void changeSceneToPersonalRanking(String fxml, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent parent = loader.load();

        // Truyền UserID vào Homeadmin controller
        PersonalRanking homeadminController = loader.getController();
        homeadminController.setUserID(userID);

        Scene scene = new Scene(parent);
        stg.setScene(scene);
        stg.show();
    }

    public void changeSceneToPersonalRanking2(String fxml, String userID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent parent = loader.load();

        // Truyền UserID vào Homeadmin controller
        PersonalRanking2 homeadminController = loader.getController();
        homeadminController.setUserID(userID);

        Scene scene = new Scene(parent);
        stg.setScene(scene);
        stg.show();
    }
}