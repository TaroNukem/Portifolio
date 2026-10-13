package com.portifolio.daniel.controllers;

import com.portifolio.daniel.dto.ContatoRequest;
import com.portifolio.daniel.service.EmailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
public class ContatoController {

    private final EmailService emailService;
    private final MessageSource messages;

    public ContatoController(EmailService emailService, MessageSource messages) {
        this.emailService = emailService;
        this.messages = messages;
    }

    @PostMapping("/contato")
    public ResponseEntity<Map<String, String>> enviar(@RequestBody ContatoRequest req, Locale locale) {
        if (req.website() != null && !req.website().isBlank()) {
            return ResponseEntity.ok(Map.of("message", messages.getMessage("contact.form.success", null, locale)));
        }
        if (isBlank(req.nome()) || isBlank(req.email()) || isBlank(req.mensagem())
                || !req.email().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
                || req.nome().length() > 100
                || req.mensagem().length() > 2000) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", messages.getMessage("contact.form.invalid", null, locale)));
        }
        try {
            emailService.enviar(req);
            return ResponseEntity.ok(Map.of("message", messages.getMessage("contact.form.success", null, locale)));
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("message", messages.getMessage("contact.form.error", null, locale)));
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}