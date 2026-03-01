package com.vinicius.project_attorney_users.busines.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCodigo(String email, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperação de Senha");
        message.setText("""
                Olá!
                
                Seu código de recuperação de senha é: %s
                
                Esse código expira em 10 minutos.
                
                Caso não tenha solicitado, ignore este e-mail.
                """.formatted(codigo));
        mailSender.send(message);
    }

//    public void enviarTeste() {
//        enviarCodigo("comovcfaraologin2018@gmail.com", "123456");
//    }

}
