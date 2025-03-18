package com.aggroconnect.appli.service;

import com.aggroconnect.appli.model.Site;
import org.json.JSONObject;

import java.util.List;

public class SiteService {
    private static final String BASE_ENDPOINT = "site";

    public List<Site> getSites() {
        return ApiClient.getList(BASE_ENDPOINT, json ->
                new Site(json.getInt("id"), json.getString("city"))
        );
    }

    public void addSite(Site site, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("city", site.cityProperty().get());

        ApiClient.addEntity(BASE_ENDPOINT, jsonObject, onSuccess);
    }

    public void updateSite(Site site, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", site.getId());
        jsonObject.put("city", site.cityProperty().get());

        ApiClient.updateEntity(BASE_ENDPOINT + "/" + site.getId(), jsonObject, onSuccess);
    }

    public void deleteSite(Site site) {
        ApiClient.deleteEntity(BASE_ENDPOINT + "/" + site.getId());
    }
}
