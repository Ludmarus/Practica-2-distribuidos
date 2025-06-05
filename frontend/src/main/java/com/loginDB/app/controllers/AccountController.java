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

@Controller
/**
 * AccountController class.
 */
public class AccountController {

    @Value("${flask.api.base-url}")
    private String flaskApiBaseUrl;

    @GetMapping("/account")
/**
 * TODO: Describe getAccountInfo method.
 */
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