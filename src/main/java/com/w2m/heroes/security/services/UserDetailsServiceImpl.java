package com.w2m.heroes.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.w2m.heroes.entities.User;
import com.w2m.heroes.repositories.UserRepository;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      final User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User Not Found with username: %s", username)));

      return UserDetailsImpl.build(user);
   }
}
