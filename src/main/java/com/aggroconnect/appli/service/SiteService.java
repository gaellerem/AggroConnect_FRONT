package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.Site;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SiteService {
    private final OkHttpClient client = new OkHttpClient();

    public List<Site> getSites() {
        List<Site> sites = new ArrayList<>();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/site")
                .build();

        try (Response response = client.newCall(request).execute()){
            if (response.isSuccessful()){
                JSONArray jsonArray = new JSONArray(response.body().string());

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int id = jsonObject.getInt("id");
                    String city = jsonObject.getString("city");
                    Site site = new Site(id, city);
                    sites.add(site);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sites;
    }
}
