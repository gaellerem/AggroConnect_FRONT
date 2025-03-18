package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.*;
import org.json.JSONObject;

import java.util.List;
import java.util.function.Function;

public class EmployeeService {

    private static final String BASE_ENDPOINT = "employee";

    private static final Function<JSONObject, Employee> employeeMapper = obj -> {
        int id = obj.getInt("id");
        String name = obj.getString("name");
        String email = obj.getString("email");
        String landline = obj.getString("landline");
        String cellphone = obj.getString("cellphone");

        // Récupérer le département
        JSONObject deptJson = obj.getJSONObject("department");
        Department department = new Department(deptJson.getInt("id"), deptJson.getString("name"));

        // Récupérer le site
        JSONObject siteJson = obj.getJSONObject("site");
        Site site = new Site(siteJson.getInt("id"), siteJson.getString("city"));

        return new Employee(id, name, email, landline, cellphone, department, site);
    };

    public List<Employee> getEmployees() {
        return ApiClient.getList(BASE_ENDPOINT, employeeMapper);
    }

    public Employee addEmployee(Employee employee) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", employee.nameProperty().get());
        jsonObject.put("email", employee.emailProperty().get());
        jsonObject.put("landline", employee.landlineProperty().get());
        jsonObject.put("cellphone", employee.cellphoneProperty().get());
        jsonObject.put("departmentId", employee.getDepartment().getId());
        jsonObject.put("siteId", employee.getSite().getId());

        // Ajoute l'employé et extrait les données depuis "data"
        return ApiClient.addEmployee(BASE_ENDPOINT, jsonObject, jsonResponse -> {
            JSONObject data = jsonResponse.getJSONObject("data"); // extraction de "data"
            return employeeMapper.apply(data);  // mapper l'objet de la réponse
        });
    }

    public void updateEmployee(Employee employee, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", employee.getId());
        jsonObject.put("name", employee.nameProperty().get());
        jsonObject.put("email", employee.emailProperty().get());
        jsonObject.put("landline", employee.landlineProperty().get());
        jsonObject.put("cellphone", employee.cellphoneProperty().get());
        jsonObject.put("departmentId", employee.getDepartment().getId());
        jsonObject.put("siteId", employee.getSite().getId());

        ApiClient.updateEntity(BASE_ENDPOINT + "/" + employee.getId(), jsonObject, onSuccess);
    }

    public void deleteEmployee(Employee employee) {
        ApiClient.deleteEntity(BASE_ENDPOINT + "/" + employee.getId());
    }
}
