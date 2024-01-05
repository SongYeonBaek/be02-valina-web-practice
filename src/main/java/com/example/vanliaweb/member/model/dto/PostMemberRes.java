package com.example.vanliaweb.member.model.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMemberRes {
    Boolean isSuccess;
    Integer code;
    String message;
    Map<String, Integer> result;
    Boolean success;
}
