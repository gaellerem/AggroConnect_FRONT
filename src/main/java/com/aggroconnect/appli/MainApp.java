package com.aggroconnect.appli;

import com.aggroconnect.appli.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class MainApp extends Application {
    @Getter
    private static MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/aggroconnect/appli/fxml/MainWindow.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();

        Scene scene = new Scene(root);
        stage.setTitle("AggroConnect");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}