package com.portifolio.daniel.service;

import com.portifolio.daniel.model.GitHubUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GitHubService {

    private static final String USUARIO = "TaroNukem";
    private static final long VALIDADE_MS = 10 * 60 * 1000L;
    private static final long NOVA_TENTATIVA_MS = 60 * 1000L;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;

    private GitHubUser cache;
    private long valeAte = 0;

    public GitHubService(@Value("${github.token:}") String token) {
        this.token = token;
    }

    public synchronized GitHubUser buscarUsuario() {
        long agora = System.currentTimeMillis();

        if (cache != null && agora < valeAte) {
            return cache;
        }

        try {
            GitHubUser github = restTemplate.exchange(
                    "https://api.github.com/users/" + USUARIO,
                    HttpMethod.GET,
                    requisicao(),
                    GitHubUser.class
            ).getBody();

            if (github == null) {
                throw new IllegalStateException("Resposta vazia do GitHub");
            }

            github.setTotal_commits(buscarTotalCommits(cache != null ? cache.getTotal_commits() : 0));

            cache = github;
            valeAte = agora + VALIDADE_MS;
            return cache;

        } catch (Exception e) {
            System.err.println("Não foi possível buscar os dados do GitHub: " + e.getMessage());
            valeAte = agora + NOVA_TENTATIVA_MS;

            if (cache != null) {
                return cache;
            }
            throw e;
        }
    }

    private int buscarTotalCommits(int valorAnterior) {
        try {
            Map<?, ?> resposta = restTemplate.exchange(
                    "https://api.github.com/search/commits?q=author:" + USUARIO,
                    HttpMethod.GET,
                    requisicao(),
                    Map.class
            ).getBody();

            if (resposta != null && resposta.get("total_count") instanceof Number total) {
                return total.intValue();
            }
        } catch (Exception e) {
            System.err.println("Não foi possível buscar o total de commits: " + e.getMessage());
        }
        return valorAnterior;
    }

    private HttpEntity<Void> requisicao() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("User-Agent", "portfolio-daniel");

        if (token != null && !token.isBlank()) {
            headers.setBearerAuth(token);
        }

        return new HttpEntity<>(headers);
    }
}