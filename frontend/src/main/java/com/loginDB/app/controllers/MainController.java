package com.loginDB.app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador principal para rutas generales y utilidades de desarrollo.
 */
@Controller
public class MainController {

    /**
     * Página principal (/).
     *
     * Añade la ruta actual al modelo para facilitar la navegación o depuración.
     *
     * @param request objeto HTTP con la ruta actual
     * @param model modelo para la vista Thymeleaf
     * @return nombre de la plantilla 'index.html'
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }

    /**
     * Página de pruebas para API (/test-api).
     *
     * Puede usarse como sandbox visual o para testing de formularios.
     *
     * @param request objeto HTTP con la ruta actual
     * @param model modelo para la vista
     * @return nombre de la plantilla 'test-api.html'
     */
    @GetMapping("/test-api")
    public String testApi(HttpServletRequest request, Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "test-api";
    }

    /**
     * Muestra información básica del usuario autenticado.
     * Útil para desarrollo y depuración de sesión.
     *
     * @return cadena de texto con nombre de usuario y roles
     */
    @GetMapping("/debug")
    @ResponseBody
    public String debugAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return "Usuario: " + auth.getName() + ", Roles: " + auth.getAuthorities();
    }
}