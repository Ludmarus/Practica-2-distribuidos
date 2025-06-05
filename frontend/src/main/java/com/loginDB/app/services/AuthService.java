package com.loginDB.app.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
/**
 * AuthService class.
 */
public class AuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://localhost:5000/api";

    /**
     * Método público.
     */
/**
 * TODO: Describe login method.
 */
    public boolean login(String username, String password) {
        String url = API_URL + "/login";
        Map<String, String> request = Map.of("username", username, "password", password);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método público.
     */
/**
 * TODO: Describe register method.
 */
    public boolean register(String username, String password) {
        String url = API_URL + "/register";
        Map<String, String> request = Map.of("username", username, "password", password);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}