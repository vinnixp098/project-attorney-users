package com.vinicius.project_attorney_users.busines.dto;

import lombok.Data;

@Data
public class PasswordCodeValidationDTO {
    private String email;
    private String codigo;
    private String novaSenha;
}
