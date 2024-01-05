package com.example.vanliaweb.member.controller;

import com.example.vanliaweb.member.model.dto.AuthenticationRequest;
import com.example.vanliaweb.member.model.dto.AuthenticationResponse;
import com.example.vanliaweb.member.model.dto.PostMemberReq;
import com.example.vanliaweb.member.repository.EmailVerifyRepository;
import com.example.vanliaweb.member.service.EmailVerifyService;
import com.example.vanliaweb.member.service.MemberService;
import com.example.vanliaweb.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JavaMailSender emailSender;
    private final EmailVerifyService emailVerifyService;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Integer expiredTimeMs;

    @RequestMapping("/authenticate")
    public ResponseEntity login(AuthenticationRequest authenticationRequest){
        if(memberService.login(authenticationRequest)){
            String jwt = JwtUtils.generateAccessToken(authenticationRequest.getUsername(), secretKey, expiredTimeMs);

            return ResponseEntity.ok().body(AuthenticationResponse.builder()
                    .token(jwt)
                    .build());
        }
        return ResponseEntity.ok().body("error");
    }

    @RequestMapping(method = RequestMethod.GET, value ="/checkemail")
    public Boolean checkEmail(@RequestBody String email){
        return memberService.checkEmail(email);
    }

    @RequestMapping(method = RequestMethod.POST, value ="/sellersignup")
    public ResponseEntity sellersignup(PostMemberReq postMemberReq){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(postMemberReq.getEmail());
        message.setSubject("[Vanlia] 이메일을 인증해주세요");

        String token = UUID.randomUUID().toString();
        String jwt = JwtUtils.generateAccessToken(postMemberReq.getEmail(), secretKey, expiredTimeMs);

        message.setText("http://localhost:8080/member/confirm?email=" + postMemberReq.getEmail()+"&token="+token +"&jwt=" + jwt);
        emailSender.send(message);

        emailVerifyService.create(postMemberReq.getEmail(), token, jwt);
        return ResponseEntity.ok().body(memberService.sellersignup(postMemberReq));
    }

    @RequestMapping(method = RequestMethod.POST, value ="/signup")
    public ResponseEntity signup(@RequestBody PostMemberReq postMemberReq){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(postMemberReq.getEmail());
        message.setSubject("[Vanlia] 이메일을 인증해주세요");

        String token = UUID.randomUUID().toString();
        String jwt = JwtUtils.generateAccessToken(postMemberReq.getEmail(), secretKey, expiredTimeMs);

        message.setText("http://localhost:8080/member/confirm?email=" + postMemberReq.getEmail()+"&token="+token +"&jwt=" + jwt);
        emailSender.send(message);

        emailVerifyService.create(postMemberReq.getEmail(), token, jwt);
        return ResponseEntity.ok().body(memberService.signup(postMemberReq));
    }

    @RequestMapping("/confirm")
    public RedirectView confirm(String email, String jwt, String token){
        if(emailVerifyService.confirm(email, jwt, token)){
            memberService.update(email);
            return new RedirectView("http://localhost:3000/emailconfirm/" + jwt);
        }

        return new RedirectView("http://localhost:3000/emailCertError");
    }
}
