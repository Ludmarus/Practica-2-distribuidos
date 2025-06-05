package com.loginDB.app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/**
 * MainController class.
 */
public class MainController {

    @GetMapping("/")
/**
 * TODO: Describe index method.
 */
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }

    @GetMapping("/test-api")
/**
 * TODO: Describe testApi method.
 */
    public String testApi(HttpServletRequest request, Model model) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "test-api";
    }

    @GetMapping("/debug")
    @ResponseBody
/**
 * TODO: Describe debugAuth method.
 */
    public String debugAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return "Usuario: " + auth.getName() + ", Roles: " + auth.getAuthorities();
    }
}