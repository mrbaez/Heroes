package com.w2m.heroes.security.jwt;

import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.w2m.heroes.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

   @Value("${heroes.jwtSecret}")
   private String jwtSecret;

   @Value("${heroes.jwtExpirationMs}")
   private int jwtExpirationMs;

   public String generateJwtToken(Authentication authentication) {

      final UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

      return Jwts
            .builder()
            .setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
   }

   public String getUserNameFromJwtToken(String token) {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
   }

   public boolean validateJwtToken(String authToken) {
      try {
         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
         return true;
      } catch (SignatureException e) {
         log.error("Invalid JWT signature: {}", e.getMessage());
      } catch (MalformedJwtException e) {
         log.error("Invalid JWT token: {}", e.getMessage());
      } catch (ExpiredJwtException e) {
         log.error("JWT token is expired: {}", e.getMessage());
      } catch (UnsupportedJwtException e) {
         log.error("JWT token is unsupported: {}", e.getMessage());
      } catch (IllegalArgumentException e) {
         log.error("JWT claims string is empty: {}", e.getMessage());
      }

      return false;
   }
}
