package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.*;
import com.aggroconnect.appli.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class EmployeeFormController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField landlineField;
    @FXML private TextField cellphoneField;
    @FXML private ComboBox<Department> departmentComboBox;
    @FXML private ComboBox<Site> siteComboBox;
    @FXML private Label errorLabel;

    private Employee currentEmployee = null;
    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final SiteService siteService = new SiteService();

    @FXML
    public void initialize() {

        // Charger les départements
        List<Department> departmentList = departmentService.getDepartments();
        ObservableList<Department> departments = FXCollections.observableArrayList(departmentList);
        departmentComboBox.getItems().addAll(departments);
        // Définir comment afficher le nom du département
        departmentComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((empty || department == null) ? null : department.nameProperty().get());
            }
        });
        departmentComboBox.setButtonCell(departmentComboBox.getCellFactory().call(null));

        List<Site> siteList = siteService.getSites();
        ObservableList<Site> sites = FXCollections.observableArrayList(siteList);
        siteComboBox.getItems().addAll(sites);
        // Définir comment afficher la ville du site

        siteComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Site site, boolean empty) {
                super.updateItem(site, empty);
                setText((empty || site == null) ? null : site.cityProperty().get());
            }
        });
        siteComboBox.setButtonCell(siteComboBox.getCellFactory().call(null));
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;

        if (employee != null) {
            nameField.setText(employee.nameProperty().get());
            emailField.setText(employee.emailProperty().get());
            landlineField.setText(employee.landlineProperty().get());
            cellphoneField.setText(employee.cellphoneProperty().get());
            departmentComboBox.getSelectionModel().select(findDepartment(employee.getDepartment()));
            siteComboBox.getSelectionModel().select(findSite(employee.getSite()));
        }
    }

    @FXML
    private void handleSave() {
        Employee employee;
        String name = nameField.getText();
        String email = emailField.getText();
        String landline = landlineField.getText();
        String cellphone = cellphoneField.getText();
        Department department = departmentComboBox.getValue();
        Site site = siteComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || landline.isEmpty() || cellphone.isEmpty() || department == null || site == null) {
            errorLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        try {
            if (currentEmployee == null) {
                employee = new Employee(0, name, email, landline, cellphone, department, site);
                employee = employeeService.addEmployee(employee);
            } else {
                employee = new Employee(currentEmployee.getId(), name, email, landline, cellphone, department, site);
                employeeService.updateEmployee(employee);
            }
            MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeView.fxml", employee);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if (currentEmployee == null) {
            MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeList.fxml");
        } else {
            MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeView.fxml", currentEmployee);
        }
    }

    private Department findDepartment(Department target) {
        return departmentComboBox.getItems().stream()
                .filter(dept -> dept.getId() == target.getId())
                .findFirst()
                .orElse(null);
    }

    private Site findSite(Site target) {
        return siteComboBox.getItems().stream()
                .filter(site -> site.getId() == target.getId())
                .findFirst()
                .orElse(null);
    }

}
