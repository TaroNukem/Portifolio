package com.portifolio.daniel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContatoRequest(
        @NotBlank @Size(max=100) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 2000) String mensagem,
        String website
) {
}
