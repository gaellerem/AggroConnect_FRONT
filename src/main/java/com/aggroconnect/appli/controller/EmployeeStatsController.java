package com.aggroconnect.appli.controller;

import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.service.EmployeeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeStatsController {
    @FXML
    private PieChart departmentPieChart;
    @FXML
    private PieChart sitePieChart;

    private final EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        List<Employee> employees = employeeService.getEmployees();
        generateDepartmentStats(employees);
        generateSiteStats(employees);
    }

    private void generateDepartmentStats(List<Employee> employees) {
        Map<String, Integer> departmentCounts = new HashMap<>();

        for (Employee employee : employees) {
            String department = employee.getDepartment().nameProperty().get();
            departmentCounts.put(department, departmentCounts.getOrDefault(department, 0) + 1);
        }

        ObservableList<PieChart.Data> departmentData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : departmentCounts.entrySet()) {
            departmentData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        departmentPieChart.setData(departmentData);
    }

    private void generateSiteStats(List<Employee> employees) {
        Map<String, Integer> siteCounts = new HashMap<>();

        for (Employee employee : employees) {
            String site = employee.getSite().cityProperty().get();
            siteCounts.put(site, siteCounts.getOrDefault(site, 0) + 1);
        }

        ObservableList<PieChart.Data> siteData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : siteCounts.entrySet()) {
            siteData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        sitePieChart.setData(siteData);
    }
}
