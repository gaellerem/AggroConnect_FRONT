package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.Department;
import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.model.Site;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    private final OkHttpClient client = new OkHttpClient();

    // Appel API pour récupérer la liste de tous les employés
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/employee")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONArray jsonArray = new JSONArray(response.body().string());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    String email = obj.getString("email");
                    String landline = obj.getString("landline");
                    String cellphone = obj.getString("cellphone");

                    JSONObject deptJson = obj.getJSONObject("department");
                    Department department = new Department(deptJson.getInt("id"), deptJson.getString("name"));

                    JSONObject siteJson = obj.getJSONObject("site");
                    Site site = new Site(siteJson.getInt("id"), siteJson.getString("city"));

                    Employee employee = new Employee(id, name, email, landline, cellphone, department, site);
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }
}
