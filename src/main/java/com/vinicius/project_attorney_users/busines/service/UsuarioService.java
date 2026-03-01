package com.vinicius.project_attorney_users.busines.service;

import com.vinicius.project_attorney_users.busines.dto.LoginDTO;
import com.vinicius.project_attorney_users.busines.dto.LoginResponseDTO;
import com.vinicius.project_attorney_users.busines.dto.UsuarioCadastroDTO;
import com.vinicius.project_attorney_users.busines.dto.UsuarioDTO;
import com.vinicius.project_attorney_users.insfratructure.entitys.PasswordResetToken;
import com.vinicius.project_attorney_users.insfratructure.entitys.Token;
import com.vinicius.project_attorney_users.insfratructure.entitys.Usuario;
import com.vinicius.project_attorney_users.insfratructure.repository.PasswordResetTokenRepository;
import com.vinicius.project_attorney_users.insfratructure.repository.TokenRepository;
import com.vinicius.project_attorney_users.insfratructure.repository.UsuarioRepository;
import com.vinicius.project_attorney_users.insfratructure.security.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> salvarUsuario(@Valid UsuarioCadastroDTO usuarioDTO) {
        if (repository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .telefone(usuarioDTO.getTelefone())
                .cpf(usuarioDTO.getCpf())
                .senha(passwordEncoder.encode(usuarioDTO.getSenha()))
                .ativo(true)
                .build();

        repository.save(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @Transactional
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        Optional<Usuario> usuarioOpt = repository.findByEmail(loginDTO.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new LoginResponseDTO(false, "Usuário não encontrado!", null, null, null, null)
            );
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getAtivo()) {
            return ResponseEntity.status(403).body(
                    new LoginResponseDTO(false, "Usuário inativo!", null, null, null, null)
            );
        }

        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body(
                    new LoginResponseDTO(false, "Senha incorreta!", null, null, null, null)
            );
        }

        // Invalida tokens antigos
        tokenRepository.deleteByUsuarioId(usuario.getId());

        String tokenString = jwtUtil.generateToken(usuario.getEmail());
        LocalDateTime expiration = LocalDateTime.now().plusDays(1);

        Token token = Token.builder()
                .usuario(usuario)
                .token(tokenString)
                .dataExpiracao(expiration)
                .ativo(true)
                .build();
        tokenRepository.save(token);

        return ResponseEntity.ok(
                new LoginResponseDTO(true, "Login realizado com sucesso!",
                        usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), tokenString)
        );
    }

    public List<UsuarioDTO> buscarAtivos() {
        return repository.findAllByAtivo(true)
                .stream()
                .map(u -> UsuarioDTO.builder()
                        .nome(u.getNome())
                        .email(u.getEmail())
                        .telefone(u.getTelefone())
                        .cpf(u.getCpf())
                        .build())
                .toList();
    }

    public void deletarPorCpf(String cpf) {
        repository.deleteByCpf(cpf);
    }

    public ResponseEntity<?> atualizarUsuario(String cpf, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioOpt = repository.findByCpf(cpf);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuarioAtualizado.getNome() != null) usuario.setNome(usuarioAtualizado.getNome());
        if (usuarioAtualizado.getEmail() != null) usuario.setEmail(usuarioAtualizado.getEmail());
        if (usuarioAtualizado.getSenha() != null)
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));

        repository.save(usuario);
        return ResponseEntity.ok("Usuário atualizado com sucesso!");
    }

    private String gerarCodigo() {
        return String.format("%06d", new Random().nextInt(999999));
    }


    @Transactional
    public ResponseEntity<?> enviarCodigoRecuperacao(String email) {
        Optional<Usuario> usuarioOpt = repository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }

        // Gera código
        String codigo = gerarCodigo();

        // Remove tokens antigos dentro da transação
        passwordResetTokenRepository.deleteByEmail(email);

        // Cria novo token
        PasswordResetToken token = PasswordResetToken.builder()
                .email(usuarioOpt.get().getEmail())
                .token(codigo)
                .expiration(LocalDateTime.now().plusMinutes(10))
                .build();
        passwordResetTokenRepository.save(token);

        // Envia por email
        emailService.enviarCodigo(usuarioOpt.get().getEmail(), codigo);

        return ResponseEntity.ok("Código de recuperação enviado para o e-mail!");
    }


    public ResponseEntity<?> validarCodigoRecuperacao(String email, String codigo, String novaSenha) {
        PasswordResetToken token = passwordResetTokenRepository.findByEmailAndToken(email, codigo)
                .orElse(null);

        if (token == null) {
            return ResponseEntity.status(400).body("Código inválido!");
        }

        if (token.getExpiration().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(token);
            return ResponseEntity.status(400).body("Código expirado!");
        }

        Usuario usuario = repository.findByEmail(email)
                .orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        repository.save(usuario);

        // Remove token após uso
        passwordResetTokenRepository.delete(token);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }
}
