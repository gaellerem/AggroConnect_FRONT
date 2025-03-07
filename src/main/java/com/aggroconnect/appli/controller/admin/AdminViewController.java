package com.aggroconnect.appli.controller.admin;

import com.aggroconnect.appli.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class AdminViewController {

    @FXML
    private StackPane contentArea;
    @FXML
    private ToggleGroup menuGroup;

    @FXML
    private void initialize() {
        menuGroup.selectToggle(menuGroup.getToggles().getFirst());
        menuGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton selectedButton = (ToggleButton) menuGroup.getSelectedToggle();
                String text = selectedButton.getText();

                switch (text) {
                    case "Employés":
                        setContent("/com/aggroconnect/appli/fxml/admin/EmployeeList.fxml");
                        break;
                    case "Sites":
                        setContent("/com/aggroconnect/appli/fxml/admin/SiteList.fxml");
                        break;
                    case "Départements":
                        setContent("/com/aggroconnect/appli/fxml/admin/DepartmentList.fxml");
                        break;
                }
            }
        });
    }

    private void setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }
}
