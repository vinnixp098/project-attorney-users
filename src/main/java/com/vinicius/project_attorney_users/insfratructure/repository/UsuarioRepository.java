package com.vinicius.project_attorney_users.insfratructure.repository;

import com.vinicius.project_attorney_users.busines.dto.UsuarioDTO;
import com.vinicius.project_attorney_users.insfratructure.entitys.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    List<UsuarioDTO> findAllByAtivo(Boolean ativo);

    Optional<Usuario> findByCpf(String cpf);

    @Transactional
    void deleteByCpf(String cpf);
}
