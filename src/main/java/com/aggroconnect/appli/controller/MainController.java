package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;

import java.io.IOException;

public class MainController {

    @FXML
    BorderPane mainContainer;

    public void setContent(String fxmlPath, Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            Parent view = loader.load();
            if (employee != null) {
                EmployeeController controller = loader.getController();
                controller.setEmployee(employee);
            }
            mainContainer.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
