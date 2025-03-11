package com.aggroconnect.appli.service;

import com.aggroconnect.appli.exception.EntityDeletionException;
import com.aggroconnect.appli.model.Employee;
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

    public static Employee addEmployee(String url, JSONObject jsonObject, Function<JSONObject, Employee> mapper) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject responseObject = new JSONObject(response.body().string());
                return mapper.apply(responseObject);
            } else {
                throw new IOException("Échec de la requête : " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
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
            if (response.isSuccessful()) {
                if (onSuccess != null) {
                    onSuccess.run();
                }
            } else {
                handleErrorResponse(response);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
    }

    private static void handleErrorResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        String errorMessage = "Échec de la requête : " + response.code();

        if (!responseBody.isEmpty()) {
            errorMessage += "\n" + responseBody;
        }

        switch (response.code()) {
            case 400 -> throw new IllegalArgumentException(errorMessage);
            case 403 -> throw new SecurityException(errorMessage);
            case 404 -> throw new IllegalStateException(errorMessage);
            case 409 -> throw new EntityDeletionException(errorMessage);
            default -> throw new IOException(errorMessage);
        }
    }
}
