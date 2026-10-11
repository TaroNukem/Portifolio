
package com.portifolio.daniel.controllers;

import com.portifolio.daniel.model.GitHubUser;
import com.portifolio.daniel.service.GitHubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DanielController {

    private final GitHubService gitHubService;

    public DanielController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/")
    public String profile(Model model) {
        GitHubUser github;

        try {
            github = gitHubService.buscarUsuario();

            if (github == null) {
                github = new GitHubUser();
            }
        } catch (Exception e) {
            System.err.println(
                    "Não foi possível buscar os dados do GitHub: "
                            + e.getMessage()
            );

            github = new GitHubUser();
        }

        model.addAttribute("github", github);

        return "index";
    }
}
