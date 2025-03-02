package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.MainApp;
import com.aggroconnect.appli.model.Department;
import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.model.Site;
import com.aggroconnect.appli.service.DepartmentService;
import com.aggroconnect.appli.service.EmployeeService;
import com.aggroconnect.appli.service.SiteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeeListController {
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
    @FXML
    private MenuButton departmentFilter;
    @FXML
    private MenuButton siteFilter;

    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final SiteService siteService = new SiteService();

    private ObservableList<Employee> employees;
    private ObservableList<Department> departments;
    private ObservableList<Site> sites;

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de l'objet Employee
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        landlineColumn.setCellValueFactory(cellData -> cellData.getValue().landlineProperty());
        cellphoneColumn.setCellValueFactory(cellData -> cellData.getValue().cellphoneProperty());

        // Charger les données
        loadData();
    }

    public void handleSearch() {
        filterEmployees(searchField.getText());
    }

    @FXML
    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Double clic
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                MainApp.getMainController().setContent("/com/aggroconnect/appli/fxml/EmployeeView.fxml", selectedEmployee);
            }
        }
    }

    private void loadData() {
        List<Employee> employeesList = employeeService.getEmployees();
        employees = FXCollections.observableArrayList(employeesList);
        employeeTableView.setItems(employees);

        // Charger les départements
        List<Department> departmentList = departmentService.getDepartments();
        departments = FXCollections.observableArrayList(departmentList);
        for (Department department : departmentList) {
            CheckMenuItem item = new CheckMenuItem(department.nameProperty().get());
            departmentFilter.getItems().add(item);
            item.setOnAction(e -> {
                handleSearch();
                updateMenuButtonText(departmentFilter);
            });
        }

        // Charger les sites
        List<Site> siteList = siteService.getSites();
        sites = FXCollections.observableArrayList(siteList);
        for (Site site : siteList) {
            CheckMenuItem item = new CheckMenuItem(site.cityProperty().get());
            siteFilter.getItems().add(item);
            item.setOnAction(e -> {
                handleSearch();
                updateMenuButtonText(siteFilter);
            });
        }
    }

    private boolean matchesSearch(Employee emp, String searchText) {
        return searchText == null || searchText.isEmpty() ||
                emp.nameProperty().get().toLowerCase().contains(searchText.toLowerCase());
    }

    private List<Department> getSelectedDepartments() {
        return departmentFilter.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(item -> (CheckMenuItem) item)
                .filter(CheckMenuItem::isSelected)
                .map(item -> departments.stream()
                        .filter(dep -> dep.nameProperty().get().equals(item.getText()))
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Site> getSelectedSites() {
        return siteFilter.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(item -> (CheckMenuItem) item)
                .filter(CheckMenuItem::isSelected)
                .map(item -> sites.stream()
                        .filter(site -> site.cityProperty().get().equals(item.getText()))
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @FXML
    private void clearDepartmentFilter() {
        departmentFilter.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(item -> (CheckMenuItem) item)
                .forEach(item -> item.setSelected(false));

        updateMenuButtonText(departmentFilter);
        handleSearch();
    }

    @FXML
    private void clearSiteFilter() {
        siteFilter.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(item -> (CheckMenuItem) item)
                .forEach(item -> item.setSelected(false));

        updateMenuButtonText(siteFilter);
        handleSearch();
    }

    private boolean matchesDepartment(Employee emp, List<Department> selectedDepartments) {
        if (selectedDepartments.isEmpty()) {
            return true; // Pas de filtre appliqué
        }
        return selectedDepartments.stream()
                .anyMatch(dep -> emp.getDepartment().getId() == dep.getId());
    }

    private boolean matchesSite(Employee emp, List<Site> selectedSites) {
        if (selectedSites.isEmpty()) {
            return true; // Pas de filtre appliqué
        }
        return selectedSites.stream()
                .anyMatch(site -> emp.getSite().getId() == site.getId());
    }

    private void filterEmployees(String searchText) {
        List<Department> selectedDepartments = getSelectedDepartments();
        List<Site> selectedSites = getSelectedSites();

        List<Employee> filtered = employees.stream()
                .filter(emp -> matchesSearch(emp, searchText) &&
                        matchesDepartment(emp, selectedDepartments) &&
                        matchesSite(emp, selectedSites))
                .collect(Collectors.toList());

        employeeTableView.setItems(FXCollections.observableArrayList(filtered));
    }

    private void updateMenuButtonText(MenuButton menuButton) {
        String selectedItems = menuButton.getItems().stream()
                .filter(item -> item instanceof CheckMenuItem)
                .map(item -> (CheckMenuItem) item)
                .filter(CheckMenuItem::isSelected)
                .map(MenuItem::getText)
                .collect(Collectors.joining(", "));

        if (selectedItems.isEmpty()) {
            menuButton.setText("Tous");
        } else {
            menuButton.setText(selectedItems);
        }
    }
}
