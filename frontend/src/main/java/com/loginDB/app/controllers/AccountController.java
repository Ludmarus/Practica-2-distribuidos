// AccountController.java
package com.loginDB.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

/**
 * Controlador responsable de la gestión de la página /account.
 */
@Controller
public class AccountController {

    // Ruta del backend (localhost en IntelliJ, backend en Docker)
    @Value("${flask.api.base-url}")
    private String flaskApiBaseUrl;

    /**
     * Muestra la página /account con los datos del usuario autenticado.
     *
     * Consulta al backend Flask usando el nombre del usuario y obtiene:
     * - username
     * - fecha de creación
     */
    @GetMapping("/account")
    public String getAccountInfo(Model model, Authentication authentication) {
        String username = authentication.getName();
        String apiUrl = flaskApiBaseUrl + "/user/" + username;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> datos = response.getBody();
                model.addAttribute("usuario", datos.get("username"));
                model.addAttribute("creacion", datos.get("created_at"));
            } else {
                model.addAttribute("error", "No se pudo obtener la información del usuario");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al contactar con el servidor");
        }

        return "account";
    }
}