package com.portifolio.daniel.service;

import com.portifolio.daniel.model.GitHubUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GitHubService {

    private final RestTemplate restTemplate = new RestTemplate();

    public GitHubUser buscarUsuario() {

        String usuario = "TaroNukem";

        String urlUsuario = "https://api.github.com/users/" + usuario;

        GitHubUser github = restTemplate.getForObject(
                urlUsuario,
                GitHubUser.class
        );

        String urlCommits =
                "https://api.github.com/search/commits?q=author:" + usuario;

        Map resposta = restTemplate.getForObject(
                urlCommits,
                Map.class
        );

        if (github != null && resposta != null) {
            int totalCommits = (Integer) resposta.get("total_count");
            github.setTotal_commits(totalCommits);
        }

        return github;
    }
}