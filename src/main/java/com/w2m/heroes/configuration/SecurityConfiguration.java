package com.w2m.heroes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.w2m.heroes.security.jwt.AuthEntryPointJwt;
import com.w2m.heroes.security.jwt.AuthTokenFilter;
import com.w2m.heroes.security.services.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

   private static final String[] AUTH_WHITELIST = {
         // -- Swagger UI v2
         "/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
         "/webjars/**",
         // -- Swagger UI v3 (OpenAPI)
         "/v3/api-docs/**", "/swagger-ui/**" };

   private final UserDetailsServiceImpl userDetailsService;

   private final AuthEntryPointJwt unauthorizedHandler;

   private final AuthTokenFilter authTokenFilter;

   @Override
   public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
      authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }

   @Bean
   @Override
   public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {

      http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST)
            .permitAll()
            .antMatchers("/h2-console/**")
            .permitAll()
            .antMatchers("/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated();

      http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
   }
}
