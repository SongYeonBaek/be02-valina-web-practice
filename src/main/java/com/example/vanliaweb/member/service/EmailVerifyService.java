package com.example.vanliaweb.member.service;

import com.example.vanliaweb.member.model.EmailVerify;
import com.example.vanliaweb.member.repository.EmailVerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailVerifyRepository emailVerifyRepository;

    public void create(String email, String token, String jwt) {
        emailVerifyRepository.save(EmailVerify.builder()
                .email(email)
                .token(token)
                        .jwt(jwt)
                .build());
    }

    public boolean confirm(String email, String jwt, String token) {
        Optional<EmailVerify> result = emailVerifyRepository.findByEmail(email);
        if(result.isPresent()){
            if(result.get().getToken().equals(token)){
                return  true;
            }
        }
        return false;
    }
}
