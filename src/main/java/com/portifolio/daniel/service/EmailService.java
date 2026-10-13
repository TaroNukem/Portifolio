package com.portifolio.daniel.service;

import com.portifolio.daniel.dto.ContatoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {
    private final RestClient client;
    private final String destino;
    private final String remetente;

    public EmailService(@Value("${resend.api-key}") String apiKey, @Value("${contato.destino}") String destino, @Value("${contato.remetente}") String rementente) {
        this.client = RestClient.builder().baseUrl("https://api.resend.com").defaultHeader("Authorization", "Bearer " + apiKey).build();
        this.destino = destino;
        this.remetente = rementente;
    }
    public void enviar(ContatoRequest req){
        Map<String, Object> body = Map.of("from", remetente, "to", List.of(destino), "reply_to", req.email(), "subject", "Portfólio: mensagem de " + req.nome(), "text", "Nome: " + req.nome() + "\nE-mail: " + req.email() + "\n\n" + req.mensagem());
        client.post().uri("/emails").body(body).retrieve().toBodilessEntity();
    }
}
