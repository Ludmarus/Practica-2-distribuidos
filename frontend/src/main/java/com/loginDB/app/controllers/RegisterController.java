// RegisterController.java
package com.loginDB.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
/**
 * RegisterController class.
 */
public class RegisterController {

    @Value("${flask.api.register-url}")
    private String flaskRegisterUrl;

    @GetMapping("/register")
/**
 * TODO: Describe showRegisterForm method.
 */
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      Model model) {
        // Validaciones del lado del servidor
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

        if (password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[0-9].*")) {
            model.addAttribute("error", "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número");
            return "register";
        }

        // Preparar y enviar solicitud a Flask
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(flaskRegisterUrl, payload, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/login?registered";
            } else {
                model.addAttribute("error", "No se pudo registrar el usuario");
                return "register";
            }
        } catch (HttpClientErrorException.BadRequest e) {
            // Captura específica para 400 (usuario ya existe)
            model.addAttribute("error", "El usuario ya está registrado");
            return "register";
        } catch (Exception e) {
            // Cualquier otro error real
            model.addAttribute("error", "Error al contactar con el servidor");
            return "register";
        }
    }
}