package com.w2m.heroes.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.heroes.dto.JwtResponse;
import com.w2m.heroes.dto.LoginRequest;
import com.w2m.heroes.security.jwt.JwtUtils;
import com.w2m.heroes.security.services.UserDetailsImpl;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

   public static final String BEARER = "Bearer";

   private final AuthenticationManager authenticationManager;

   private final JwtUtils jwtUtils;

   @PostMapping("/signin")
   public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

      final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      final String jwt = jwtUtils.generateJwtToken(authentication);

      final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      final List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

      return ResponseEntity.ok(JwtResponse
            .builder()
            .token(jwt)
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .type(BEARER)
            .email(userDetails.getEmail())
            .roles(roles)
            .build());
   }

}
