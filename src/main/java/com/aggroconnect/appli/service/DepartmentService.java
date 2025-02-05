package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.Department;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    private final OkHttpClient client = new OkHttpClient();

    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/department")
                .build();

        try (Response response = client.newCall(request).execute()){
            if (response.isSuccessful()){
                JSONArray jsonArray = new JSONArray(response.body().string());

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    Department department = new Department(id, name);
                    departments.add(department);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departments;
    }
}
