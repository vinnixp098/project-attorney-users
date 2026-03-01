package com.vinicius.project_attorney_users.insfratructure.repository;

import com.vinicius.project_attorney_users.insfratructure.entitys.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    void deleteByUsuarioId(Integer usuarioId);
}
