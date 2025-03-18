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
    private static final String BASE_URL = "http://localhost:8080/api/";

    public static <T> List<T> getList(String endpoint, Function<JSONObject, T> mapper) {
        Request request = new Request.Builder().url(BASE_URL + endpoint).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                return IntStream.range(0, jsonArray.length())
                        .mapToObj(i -> mapper.apply(jsonArray.getJSONObject(i)))
                        .collect(Collectors.toList());
            }
            throw new IOException("Erreur API : " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
    }

    public static void addEntity(String endpoint, JSONObject jsonObject, Runnable onSuccess) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        executeRequest(request, onSuccess);
    }

    public static Employee addEmployee(String endpoint, JSONObject jsonObject, Function<JSONObject, Employee> mapper) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject responseObject = new JSONObject(response.body().string());
                JSONObject data = responseObject.getJSONObject("data");
                return mapper.apply(data);
            } else {
                throw new IOException("Échec de la requête : " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau : " + e.getMessage(), e);
        }
    }


    public static void updateEntity(String endpoint, JSONObject jsonObject, Runnable onSuccess) {
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        executeRequest(request, onSuccess);
    }

    public static void deleteEntity(String endpoint) {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
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
            JSONObject errorResponse = new JSONObject(responseBody);
            // récupère le message d'erreur sinon en renseigne un par défaut
            String message = errorResponse.optString("message", "Aucune description d'erreur fournie.");
            errorMessage += "\n" + message;
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
