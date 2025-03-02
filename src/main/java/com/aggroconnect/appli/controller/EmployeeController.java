package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EmployeeController {
    @FXML
    private VBox employeePane;

    @FXML
    private Label employeeName, employeeEmail, employeeLandline, employeeCellphone, employeeDepartment, employeeSite;

    public void initialize() {
        employeePane.setUserData(this);
    }

    public void setEmployee(Employee employee) {
        employeeName.setText(employee.nameProperty().get());
        employeeEmail.setText("Email : " + employee.emailProperty().get());
        employeeLandline.setText("Téléphone Fixe : " + employee.landlineProperty().get());
        employeeCellphone.setText("Téléphone Mobile : " + employee.cellphoneProperty().get());
        employeeDepartment.setText("Service : " + employee.getDepartment().nameProperty().get());
        employeeSite.setText("Site : " + employee.getSite().cityProperty().get());
    }

    @FXML
    public void handleBack() {
        MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml", null);
    }
}
