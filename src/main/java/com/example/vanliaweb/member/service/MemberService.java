package com.example.vanliaweb.member.service;

import com.example.vanliaweb.member.model.Member;
import com.example.vanliaweb.member.model.dto.AuthenticationRequest;
import com.example.vanliaweb.member.model.dto.AuthenticationResponse;
import com.example.vanliaweb.member.model.dto.PostMemberReq;
import com.example.vanliaweb.member.model.dto.PostMemberRes;
import com.example.vanliaweb.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public PostMemberRes signup(PostMemberReq postMemberReq) {
        Member member = Member.builder()
                .email(postMemberReq.getEmail())
                .password(postMemberReq.getPassword())
                .nickname(postMemberReq.getNickname())
                .status(0)
                .authority("USER")
                .build();
        member = memberRepository.save(member);

        Map<String, Integer> map = new HashMap<>();
        map.put("idx",member.getIdx());
        map.put("status",member.getStatus());

        return PostMemberRes.builder()
                .isSuccess(true)
                .code(1000)
                .message("요청 성공")
                .result(map)
                .success(true)
                .build();
    }

    public PostMemberRes sellersignup(PostMemberReq postMemberReq) {
        Member member = Member.builder()
                .email(postMemberReq.getEmail())
                .password(postMemberReq.getPassword())
                .nickname(postMemberReq.getNickname())
                .status(0)
                .authority("SELLER")
                .build();
        member = memberRepository.save(member);

        Map<String, Integer> map = new HashMap<>();
        map.put("idx",member.getIdx());
        map.put("status",member.getStatus());
        return PostMemberRes.builder()
                .isSuccess(true)
                .code(1000)
                .message("요청 성공")
                .result(map)
                .success(true)
                .build();
    }

    public void update(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);
        if(result.isPresent()){
            Member member = result.get();
            member.setStatus(1);
            memberRepository.save(member);
        }
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).get();
    }

    public Boolean login(AuthenticationRequest authenticationRequest) {
        Optional<Member> member = memberRepository.findByEmail(authenticationRequest.getUsername());
        if(member.isPresent() && member.get().getPassword() == authenticationRequest.getPassword()){
            return false;
        }
        else
            return true;
    }

    public Boolean checkEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            return true;
        }
        return false;
    }
}
