package com.portifolio.daniel.controllers;

import com.portifolio.daniel.model.GitHubUser;
import com.portifolio.daniel.service.GitHubService;
import com.portifolio.daniel.service.WakaTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DanielController {

    private final GitHubService gitHubService;
    private final WakaTimeService wakaTimeService;

    public DanielController(GitHubService gitHubService, WakaTimeService wakaTimeService) {
        this.gitHubService = gitHubService;
        this.wakaTimeService = wakaTimeService;
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
        model.addAttribute("waka", wakaTimeService.buscarResumo());

        return "index";
    }
}