package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.service.EmployeeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

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
    private TextField searchField;

    private final EmployeeService employeeService = new EmployeeService();
    private ObservableList<Employee> employees;

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Employee
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        landlineColumn.setCellValueFactory(cellData -> cellData.getValue().landlineProperty());
        cellphoneColumn.setCellValueFactory(cellData -> cellData.getValue().cellphoneProperty());

        // Charger les données
        List<Employee> employeesList = employeeService.getEmployees();
        employees = FXCollections.observableArrayList(employeesList);

        // Remplir le tableau
        employeeTableView.setItems(employees);
    }

    public void handleSearch() {
        filterEmployees(searchField.getText());
    }

    private void filterEmployees(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            employeeTableView.setItems(employees);
        } else {
            List<Employee> filtered = employees.stream()
                    .filter(emp -> emp.getName().get().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            employeeTableView.setItems(FXCollections.observableArrayList(filtered));
        }
    }


}
