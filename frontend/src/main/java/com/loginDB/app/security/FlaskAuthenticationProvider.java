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

@Component
/**
 * FlaskAuthenticationProvider class.
 */
public class FlaskAuthenticationProvider implements AuthenticationProvider {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    @Override
/**
 * TODO: Describe authenticate method.
 */
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("username", username, "password", password);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(flaskApiUrl, request, String.class);
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    @Override
/**
 * TODO: Describe supports method.
 */
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}