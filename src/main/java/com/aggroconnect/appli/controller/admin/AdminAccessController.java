package com.aggroconnect.appli.controller.admin;

import com.aggroconnect.appli.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

public class AdminAccessController {

    @FXML
    private PasswordField passwordField;

    private static final String PASSWORD = "admin123";

    @FXML
    private void initialize() {
        // si la touche enter est pressée dans password field
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleValidatePassword();
            }
        });
    }
    @FXML
    private void handleValidatePassword() {
        String enteredPassword = passwordField.getText();
        if (PASSWORD.equals(enteredPassword)) {
            MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/AdminView.fxml", null);
        } else {
            showError("Mot de passe incorrect.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void handleBack() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }
}
