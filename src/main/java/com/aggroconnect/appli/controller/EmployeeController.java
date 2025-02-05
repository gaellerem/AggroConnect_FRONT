package com.aggroconnect.appli.controller;

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

    @FXML
    private ComboBox<Department> departmentFilter;

    @FXML
    private ComboBox<Site> siteFilter;

    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final SiteService siteService = new SiteService();

    private ObservableList<Employee> employees;

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

    private void loadData() {
        List<Employee> employeesList = employeeService.getEmployees();
        employees = FXCollections.observableArrayList(employeesList);
        employeeTableView.setItems(employees);

        // Charger les départements
        List<Department> departmentList = departmentService.getDepartments();
        ObservableList<Department> departments = FXCollections.observableArrayList(departmentList);
        departmentFilter.getItems().add(new Department(0, "Tous les services"));
        departmentFilter.getItems().addAll(departments);

        // Définir comment afficher le nom du département
        departmentFilter.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((empty || department == null) ? null : department.nameProperty().get());
            }
        });
        departmentFilter.setButtonCell(departmentFilter.getCellFactory().call(null));

        List<Site> siteList = siteService.getSites();
        ObservableList<Site> sites = FXCollections.observableArrayList(siteList);
        siteFilter.getItems().add(new Site(0, "Tous les sites"));
        siteFilter.getItems().addAll(sites);

        // Définir comment afficher la ville du site
        siteFilter.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Site site, boolean empty) {
                super.updateItem(site, empty);
                setText((empty || site == null) ? null : site.cityProperty().get());
            }
        });
        siteFilter.setButtonCell(siteFilter.getCellFactory().call(null));
    }


    private boolean matchesSearch(Employee emp, String searchText) {
        return searchText == null || searchText.isEmpty() ||
                emp.nameProperty().get().toLowerCase().contains(searchText.toLowerCase());
    }

    private boolean matchesDepartment(Employee emp, Department selectedDepartment) {
        return selectedDepartment == null ||
                selectedDepartment.getId() == 0 || // L'ID 0 désactive le filtre
                emp.getDepartment().getId() == selectedDepartment.getId();
    }

    private boolean matchesSite(Employee emp, Site selectedSite) {
        return selectedSite == null ||
                selectedSite.getId() == 0 || // L'ID 0 désactive le filtre
                emp.getSite().getId() == selectedSite.getId();
    }

    private void filterEmployees(String searchText) {
        Department selectedDepartment = departmentFilter.getValue();
        Site selectedSite = siteFilter.getValue();

        List<Employee> filtered = employees.stream()
                .filter(emp -> matchesSearch(emp, searchText) && matchesDepartment(emp, selectedDepartment) && matchesSite(emp, selectedSite))
                .collect(Collectors.toList());

        employeeTableView.setItems(FXCollections.observableArrayList(filtered));
    }
}
