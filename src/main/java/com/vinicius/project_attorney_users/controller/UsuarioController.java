package com.vinicius.project_attorney_users.controller;

import com.vinicius.project_attorney_users.busines.dto.LoginDTO;
import com.vinicius.project_attorney_users.busines.dto.PasswordCodeValidationDTO;
import com.vinicius.project_attorney_users.busines.dto.PasswordRecoveryRequestDTO;
import com.vinicius.project_attorney_users.busines.dto.UsuarioCadastroDTO;
import com.vinicius.project_attorney_users.busines.service.UsuarioService;
import com.vinicius.project_attorney_users.insfratructure.entitys.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/cadastrar")
    public ResponseEntity<?> salvarUsuario(@Valid @RequestBody UsuarioCadastroDTO usuario) {
        return usuarioService.salvarUsuario(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login) {
        return usuarioService.login(login);
    }

    @GetMapping("/buscar-todos")
    public ResponseEntity<?> buscarTodos(){
        return ResponseEntity.ok(usuarioService.buscarAtivos());
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> deletarUsuario(@RequestParam String cpf) {
        usuarioService.deletarPorCpf(cpf);
        return ResponseEntity.ok("Usuário deletado com sucesso!");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@RequestParam String cpf, @RequestBody Usuario usuario) {
        return usuarioService.atualizarUsuario(cpf, usuario);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> enviarCodigo(@RequestBody PasswordRecoveryRequestDTO dto) {
        return usuarioService.enviarCodigoRecuperacao(dto.getEmail());
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<?> validarCodigo(@RequestBody PasswordCodeValidationDTO dto) {
        return usuarioService.validarCodigoRecuperacao(dto.getEmail(), dto.getCodigo(), dto.getNovaSenha());
    }
}
