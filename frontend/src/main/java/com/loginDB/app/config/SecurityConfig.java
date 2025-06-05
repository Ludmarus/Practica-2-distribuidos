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
 * SecurityConfig class.
 */
public class SecurityConfig {

    private final FlaskAuthenticationProvider flaskAuthenticationProvider;

/**
 * TODO: Describe SecurityConfig method.
 */
    public SecurityConfig(FlaskAuthenticationProvider flaskAuthenticationProvider) {
        this.flaskAuthenticationProvider = flaskAuthenticationProvider;
    }

    @Bean
/**
 * TODO: Describe securityFilterChain method.
 */
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

    @Bean
/**
 * TODO: Describe securityContextRepository method.
 */
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
/**
 * TODO: Describe authenticationManager method.
 */
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(flaskAuthenticationProvider));
    }
}