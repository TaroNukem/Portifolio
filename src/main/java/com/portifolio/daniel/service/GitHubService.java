package com.portifolio.daniel.service;

import com.portifolio.daniel.model.GitHubUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitHubService {

    private final RestClient restClient;

    public GitHubService(@Value("${github.token}") String token) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }

    public GitHubUser buscarUsuario() {
        return restClient
                .get()
                .uri("/users/TaroNukem")
                .retrieve()
                .body(GitHubUser.class);
    }
}