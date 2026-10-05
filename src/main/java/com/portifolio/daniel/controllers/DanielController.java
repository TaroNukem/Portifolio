package com.portifolio.daniel.controllers;

import org.springframework.ui.Model;
import com.portifolio.daniel.model.GitHubUser;
import com.portifolio.daniel.service.GitHubService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DanielController {

    private final GitHubService gitHubService;

    public DanielController(GitHubService gitHubService){
        this.gitHubService = gitHubService;
    }

    @GetMapping("/")
    public String profile(Model model){
        GitHubUser github = gitHubService.buscarUsuario();
        model.addAttribute("github", github);
        return "index";
    }
}
