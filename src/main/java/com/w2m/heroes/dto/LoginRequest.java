package com.w2m.heroes.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class LoginRequest {

   @NotBlank
   String username;

   @NotBlank
   String password;
}
