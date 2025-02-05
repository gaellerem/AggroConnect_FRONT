package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.service.EmployeeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class EmployeeController {
    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    @FXML
    private TableColumn<Employee, String> landlineColumn;

    @FXML
    private TableColumn<Employee, String> cellphoneColumn;

    @FXML
    private TableColumn<Employee, String> departmentColumn;

    @FXML
    private TableColumn<Employee, String> siteColumn;

    private final EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Employee
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        landlineColumn.setCellValueFactory(cellData -> cellData.getValue().landlineProperty());
        cellphoneColumn.setCellValueFactory(cellData -> cellData.getValue().cellphoneProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartment().nameProperty());
        siteColumn.setCellValueFactory(cellData -> cellData.getValue().getSite().cityProperty());

        // Charger les données
        List<Employee> employees = employeeService.getEmployees();
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList(employees);

        // Remplir le tableau
        employeeTableView.setItems(employeeObservableList);
    }
}
