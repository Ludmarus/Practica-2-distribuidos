package com.loginDB.app.config;

import com.loginDB.app.security.FlaskAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.List;

@Configuration
/**
 * Configuración de seguridad para la aplicación.
 *
 * Define el comportamiento de login, logout, CSRF y la estrategia de autenticación
 * basada en FlaskAuthenticationProvider.
 */
public class SecurityConfig {

    // Proveedor de autenticación personalizado que delega el login a la API Flask
    private final FlaskAuthenticationProvider flaskAuthenticationProvider;

    /**
     * Constructor que inyecta el proveedor de autenticación personalizado.
     *
     * @param flaskAuthenticationProvider proveedor de autenticación delegada a Flask
     */
    public SecurityConfig(FlaskAuthenticationProvider flaskAuthenticationProvider) {
        this.flaskAuthenticationProvider = flaskAuthenticationProvider;
    }

    /**
     * Define la cadena de filtros de seguridad de Spring Security.
     *
     * Configura:
     * - Rutas públicas (permitAll)
     * - Login personalizado (/login)
     * - Logout
     * - Desactivación de CSRF para permitir POST desde fuera (Flask, etc.)
     *
     * @param http configuración del objeto HttpSecurity
     * @return SecurityFilterChain configurada
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/do-login", "/register", "/assets/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                      // Página personalizada
                        .loginProcessingUrl("/do-login")          // Donde el formulario POSTea
                        .defaultSuccessUrl("/account", true)             // Redirige aquí si éxito
                        .failureUrl("/login?error=true")          // Redirige aquí si falla
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Configura el repositorio de contexto de seguridad basado en la sesión HTTP.
     *
     * Este componente mantiene el contexto de autenticación entre peticiones.
     *
     * @return instancia de SecurityContextRepository
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * Configura el gestor de autenticación de Spring con el proveedor Flask personalizado.
     *
     * @return instancia de AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(flaskAuthenticationProvider));
    }
}