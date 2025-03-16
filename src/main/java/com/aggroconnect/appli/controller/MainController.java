package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Employee;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
    private boolean isAdmin = false;

    @FXML
    BorderPane mainContainer;
    @FXML
    private VBox sidebar;
    @FXML
    private ToggleGroup menuGroup;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) mainContainer.getScene().getWindow();

            // Écouter les événements de touches dans la scène principale si l'utilisateur n'est pas connecté admin
            stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if(!isAdmin) {
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

            menuGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    ToggleButton selectedButton = (ToggleButton) menuGroup.getSelectedToggle();
                    String text = selectedButton.getText();

                    switch (text) {
                        case "Employés":
                            setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
                            break;
                        case "Sites":
                            setContent("/com/aggroconnect/appli/fxml/SiteList.fxml", null);
                            break;
                        case "Services":
                            setContent("/com/aggroconnect/appli/fxml/DepartmentList.fxml", null);
                            break;
                        case "Statistiques":
                            setContent("/com/aggroconnect/appli/fxml/EmployeeStats.fxml", null);
                            break;
                    }
                }
            });

            setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
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
            showFallbackDashboard(fxmlPath, employee);
        }
        // Afficher ou masquer le menu latéral selon l'état admin
        sidebar.setVisible(isAdmin);
        sidebar.setManaged(isAdmin);
    }

    private void showFallbackDashboard(String content, Employee employee) {
        Label errorMessage = new Label("Impossible de charger les données. Vérifiez votre connexion.");
        errorMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: red; -fx-padding: 10px;");

        Button retryButton = new Button("Réessayer");
        retryButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px 10px;");
        retryButton.setOnAction(event -> setContent(content, employee));

        VBox placeholder = new VBox(errorMessage, retryButton);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setSpacing(10);

        mainContainer.setCenter(placeholder);
    }

    public void authenticateAdmin() {
        isAdmin = true;
        setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }

    public void logoutAdmin() {
        isAdmin = false;
        setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }

    public boolean isAuthenticated() {
        return isAdmin;
    }

    public void setEmployeeViewMode(Employee employee, boolean isEditMode) {
        try {
            FXMLLoader loader;

            if (isEditMode) {
                loader = new FXMLLoader(MainApp.class.getResource("/com/aggroconnect/appli/fxml/EmployeeForm.fxml"));
            } else {
                loader = new FXMLLoader(MainApp.class.getResource("/com/aggroconnect/appli/fxml/EmployeeView.fxml"));
            }

            Parent view = loader.load();

            if (isEditMode) {
                EmployeeFormController controller = loader.getController();
                controller.setEmployee(employee);
            } else {
                EmployeeController controller = loader.getController();
                controller.setEmployee(employee);
            }

            mainContainer.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
