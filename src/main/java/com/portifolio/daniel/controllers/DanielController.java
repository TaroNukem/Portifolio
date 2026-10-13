package com.portifolio.daniel.controllers;

import com.portifolio.daniel.model.GitHubUser;
import com.portifolio.daniel.service.FraseService;
import com.portifolio.daniel.service.GitHubService;
import com.portifolio.daniel.service.WakaTimeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DanielController {

    private final GitHubService gitHubService;
    private final WakaTimeService wakaTimeService;
    private final FraseService fraseService;

    public DanielController(GitHubService gitHubService,
                            WakaTimeService wakaTimeService,
                            FraseService fraseService) {
        this.gitHubService = gitHubService;
        this.wakaTimeService = wakaTimeService;
        this.fraseService = fraseService;
    }

    @GetMapping("/")
    public String profile(Model model, HttpSession session) {
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
        model.addAttribute("frase", fraseService.sortear(session));

        return "index";
    }
}