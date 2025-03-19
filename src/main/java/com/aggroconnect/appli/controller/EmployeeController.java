package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class EmployeeController {
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Label employeeName, employeeEmail, employeeLandline, employeeCellphone, employeeDepartment, employeeSite;

    private Employee employee;
    private final EmployeeService employeeService = new EmployeeService();

    public void initialize() {
        // afficher les boutons modifier et supprimer uniquement si admin connecté
        editButton.setVisible(MainApp.getMainController().isAuthenticated());
        deleteButton.setVisible(MainApp.getMainController().isAuthenticated());
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        employeeName.setText(employee.nameProperty().get());
        employeeEmail.setText(employee.emailProperty().get());
        employeeLandline.setText(employee.landlineProperty().get());
        employeeCellphone.setText(employee.cellphoneProperty().get());
        employeeDepartment.setText(employee.getDepartment().nameProperty().get());
        employeeSite.setText(employee.getSite().cityProperty().get());
    }

    @FXML
    public void handleBack() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml");
    }

    @FXML
    public void editEmployee() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeForm.fxml", employee, true);
    }

    @FXML
    public void deleteEmployee() {
        Alert alert = showAlert(
                Alert.AlertType.CONFIRMATION,
                "Confirmation",
                "Êtes-vous sûr de vouloir supprimer cet employé ?",
                employee.nameProperty().get()
        );

        if(alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                employeeService.deleteEmployee(employee);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "La suppression a échoué",
                        e.getMessage()
                ).showAndWait();
            }
            handleBack();
        }
    }

    private Alert showAlert(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }
}
