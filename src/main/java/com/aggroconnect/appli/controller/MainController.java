package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Employee;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private boolean ctrlPressed = false;
    private boolean altPressed = false;

    @FXML
    BorderPane mainContainer;
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) mainContainer.getScene().getWindow();

            // Écouter les événements de touches dans la scène principale
            stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                // Vérifier si Ctrl ou Alt sont pressés
                if (event.getCode().toString().equals("CONTROL")) {
                    ctrlPressed = true;
                }
                if (event.getCode().toString().equals("ALT")) {
                    altPressed = true;
                }

                // Vérifier si la touche "A" est pressée lorsque Ctrl + Alt sont maintenus
                if (ctrlPressed && altPressed && event.getCode().toString().equals("A")) {
                    // Si la combinaison est correcte, demander un mot de passe
                    setContent("/com/aggroconnect/appli/fxml/AdminAccess.fxml", null);
                }
            });

            // Réinitialiser les touches lorsque relâchées
            stage.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                if (event.getCode().toString().equals("CONTROL")) {
                    ctrlPressed = false;
                }
                if (event.getCode().toString().equals("ALT")) {
                    altPressed = false;
                }
            });
        });
    }

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
