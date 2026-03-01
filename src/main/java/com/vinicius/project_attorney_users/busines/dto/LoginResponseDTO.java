package com.vinicius.project_attorney_users.busines.dto;

public record LoginResponseDTO(
        boolean logado,
        String mensagem,
        String telefone,
        String usuario,
        String email,
        String token
) {}
