package com.alita.usermanagement.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendVerificationEmail(String to, String token) {

        String link = "http://localhost:8080/api/auth/verify-email?token=" + token;

        System.out.println("==================================");
        System.out.println("ENVIANDO EMAIL (SIMULADO)");
        System.out.println("Para: " + to);
        System.out.println("Link: " + link);
        System.out.println("==================================");
    }
}
