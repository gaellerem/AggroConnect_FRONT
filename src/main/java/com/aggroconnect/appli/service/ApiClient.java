package com.aggroconnect.appli.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApiClient {
    private static final OkHttpClient client = new OkHttpClient();

    public static <T> List<T> getList(String url, Function<JSONObject, T> mapper) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONArray jsonArray = new JSONArray(response.body().string());
                return IntStream.range(0, jsonArray.length())
                        .mapToObj(i -> mapper.apply(jsonArray.getJSONObject(i)))
                        .collect(Collectors.toList());
            }
            throw new IOException("Erreur API : " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
    }

    public static void addEntity(String url, JSONObject jsonObject, Runnable onSuccess) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        executeRequest(request, onSuccess);
    }


    public static void updateEntity(String url, JSONObject jsonObject, Runnable onSuccess) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        executeRequest(request, onSuccess);
    }

    public static void deleteEntity(String url) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Content-Type", "application/json")
                .build();

        executeRequest(request, null);
    }

    private static void executeRequest(Request request, Runnable onSuccess) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && onSuccess != null) {
                onSuccess.run();
            } else if (!response.isSuccessful()) {
                throw new IOException("Échec de la requête : " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
    }
}
