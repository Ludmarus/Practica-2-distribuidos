// === CAMBIO: Controlador para subir archivo y reenviar a Flask ===
package com.loginDB.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
/**
 * ApiController class.
 */
public class ApiController {

    @Value("${flask.api.url:http://localhost:5000/api/cargar}")
    private String flaskApiUrl;

    @PostMapping("/api/test-flask")
    @ResponseBody
/**
 * TODO: Describe testFlask method.
 */
    public ResponseEntity<String> testFlask(@RequestParam("archivo") MultipartFile archivo) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            var body = new LinkedMultiValueMap<String, Object>();
            body.add("archivo", new ByteArrayResource(archivo.getBytes()) {
                @Override
/**
 * TODO: Describe getFilename method.
 */
                public String getFilename() {
                    return archivo.getOriginalFilename();
                }
            });

            HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"No se pudo leer el archivo.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("{\"error\":\"Error al comunicarse con el servidor Flask.\"}");
        }
    }
}