package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

public class AdminAccessController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
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
            MainApp.getMainController().authenticateAdmin();
        } else {
            errorLabel.setText("Mot de passe incorrect");
        }
    }

    @FXML
    public void handleBack() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }
}
