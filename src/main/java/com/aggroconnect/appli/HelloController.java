package com.aggroconnect.appli;

import com.aggroconnect.appli.model.Department;
import com.aggroconnect.appli.model.Employee;
import com.aggroconnect.appli.model.Site;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    public void btn(ActionEvent actionEvent) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/employee")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonData);

                List<Employee> employees = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject empJson = jsonArray.getJSONObject(i);

                    int id = empJson.getInt("id");
                    String name = empJson.getString("name");
                    String email = empJson.getString("email");
                    String landline = empJson.getString("landline");
                    String cellphone = empJson.getString("cellphone");

                    JSONObject deptJson = empJson.getJSONObject("department");
                    Department department = new Department(deptJson.getInt("id"), deptJson.getString("name"));

                    JSONObject siteJson = empJson.getJSONObject("site");
                    Site site = new Site(siteJson.getInt("id"), siteJson.getString("city"));

                    Employee employee = new Employee(id, name, email, landline, cellphone, department, site);
                    employees.add(employee);
                }

                // Affichage des employés
                for (Employee emp : employees) {
                    System.out.println(emp);
                }
            } else {
                System.out.println("Erreur lors de la récupération des données : " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}