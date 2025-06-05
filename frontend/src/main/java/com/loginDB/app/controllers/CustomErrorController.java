package com.loginDB.app.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de errores personalizado que captura detalles de errores HTTP.
 */
@Controller
/**
 * CustomErrorController class.
 */
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
/**
 * TODO: Describe handleError method.
 */
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        // Para debug en consola (puedes quitarlo luego)
        System.out.println("🛑 Error capturado:");
        System.out.println("Código: " + status);
        System.out.println("Mensaje: " + message);
        System.out.println("Ruta: " + path);
        System.out.println("Excepción: " + exception);

        model.addAttribute("status", status != null ? status.toString() : "Desconocido");
        model.addAttribute("message", message != null ? message.toString() :
                (exception != null ? exception.toString() : "Sin mensaje"));
        model.addAttribute("path", path != null ? path.toString() : "Ruta no disponible");

        return "error";  // Apunta a templates/error.html
    }
}