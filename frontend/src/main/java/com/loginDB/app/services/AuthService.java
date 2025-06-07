package com.loginDB.app.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Servicio que encapsula la lógica de autenticación y registro de usuarios,
 * delegando las operaciones en la API Flask mediante peticiones HTTP.
 */
@Service
public class AuthService {
    // Cliente HTTP para realizar peticiones REST a Flask
    private final RestTemplate restTemplate = new RestTemplate();

    // URL base de la API Flask
    private static final String API_URL = "http://localhost:5000/api";

    /**
     * Intenta autenticar al usuario enviando sus credenciales a la API Flask.
     *
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @return true si la respuesta de Flask es 2xx (login exitoso), false en caso contrario
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
     * Intenta registrar un nuevo usuario enviando los datos a la API Flask.
     *
     * @param username nombre de usuario a registrar
     * @param password contraseña en texto plano
     * @return true si el registro fue exitoso (código HTTP 2xx), false en caso de error
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