package com.seph_worker.worker.core.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public Map<String, JavaMailSender> mailSenderMap() {
        Map<String, JavaMailSender> mailSenders = new HashMap<>();
        mailSenders.put("becarios", createMailSender("sustitutos_nomina@seph.gob.mx", "Sustitutos2025"));
        mailSenders.put("licencias", createMailSender("marco.vazquez@seph.gob.mx", "Vazquez1976$"));
        return mailSenders;
    }

    private JavaMailSender createMailSender(String email, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}