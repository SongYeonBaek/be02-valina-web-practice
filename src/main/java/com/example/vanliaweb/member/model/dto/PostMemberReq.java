package com.example.vanliaweb.member.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMemberReq {
    String email;
    String nickname;
    String password;
}
