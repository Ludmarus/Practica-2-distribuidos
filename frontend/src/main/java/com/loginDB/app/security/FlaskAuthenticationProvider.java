package com.loginDB.app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Proveedor de autenticación personalizado que delega la validación de credenciales
 * al backend Flask mediante una petición HTTP.
 */
@Component
public class FlaskAuthenticationProvider implements AuthenticationProvider {

    /**
     * URL del endpoint de login en Flask.
     * Se configura desde application.properties.
     */
    @Value("${flask.api.url}")
    private String flaskApiUrl;

    /**
     * Intenta autenticar al usuario enviando sus credenciales a Flask.
     * Si Flask responde con éxito (código 2xx), se construye un token válido para Spring Security.
     * En caso de fallo, lanza BadCredentialsException.
     *
     * @param authentication objeto que contiene el username y la contraseña
     * @return token de autenticación con rol ROLE_USER si las credenciales son válidas
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("username", username, "password", password);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // Si Flask responde con 200, se considera autenticado
            restTemplate.postForEntity(flaskApiUrl, request, String.class);
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (Exception e) {
            // Cualquier excepción se interpreta como credenciales inválidas
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    /**
     * Indica que este AuthenticationProvider solo gestiona tokens de tipo UsernamePasswordAuthenticationToken.
     *
     * @param authentication clase del token
     * @return true si el token es soportado
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}