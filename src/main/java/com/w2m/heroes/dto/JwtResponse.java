package com.w2m.heroes.dto;

import java.util.List;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class JwtResponse {

   String token;

   String type;

   Long id;

   String username;

   String email;

   List<String> roles;
}
