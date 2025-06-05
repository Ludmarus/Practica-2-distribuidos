package com.loginDB.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
/**
 * LoginController class.
 */
public class LoginController {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    @GetMapping("/login")
/**
 * TODO: Describe showLoginForm method.
 */
    public String showLoginForm() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/do-login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, payload, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                return "redirect:/";
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "login";
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al contactar con el servidor");
            return "login";
        }
    }
}