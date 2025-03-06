package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

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
            openAdminPage();
        } else {
            showError("Mot de passe incorrect.");
        }
    }

    private void openAdminPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Accès administrateur activé !", ButtonType.OK);
        alert.showAndWait();

        // Charger la vue d'administration
        // Par exemple, on pourrait appeler setContent avec la page admin
        // mainController.setContent("/com/aggroconnect/appli/fxml/AdminPage.fxml", null);
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
