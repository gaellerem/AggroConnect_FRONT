package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.Department;
import org.json.JSONObject;

import java.util.List;

public class DepartmentService {
    private static final String BASE_ENDPOINT = "department";

    public List<Department> getDepartments() {
        return ApiClient.getList(BASE_ENDPOINT, json ->
                new Department(json.getInt("id"), json.getString("name"))
        );
    }

    public void addDepartment(Department department, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", department.nameProperty().get());

        ApiClient.addEntity(BASE_ENDPOINT, jsonObject, onSuccess);
    }

    public void updateDepartment(Department department, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", department.getId());
        jsonObject.put("name", department.nameProperty().get());

        ApiClient.updateEntity(BASE_ENDPOINT + "/" + department.getId(), jsonObject, onSuccess);
    }

    public void deleteDepartment(Department department) {
        ApiClient.deleteEntity(BASE_ENDPOINT + "/" + department.getId());
    }
}
