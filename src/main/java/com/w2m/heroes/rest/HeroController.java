package com.w2m.heroes.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.services.HeroService;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hero")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HeroController {

   private final HeroService heroService;

   @PreAuthorize("hasAuthority('ADMIN')")
   @PostMapping
   ResponseEntity<HeroDto> create(@Valid @RequestBody HeroDto hero) {
      final HeroDto created = heroService.create(hero);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
   }

   @PreAuthorize("hasAuthority('ADMIN')")
   @PutMapping("/{id}")
   ResponseEntity<HeroDto> update(@PathVariable(value = "id") long id, @Valid @RequestBody HeroDto hero) {
      HeroDto updated = heroService.update(id, hero);
      return ResponseEntity.status(HttpStatus.OK).body(updated);
   }

   @PreAuthorize("hasAuthority('USER')")
   @GetMapping(value = "/{id}")
   HeroDto findById(@PathVariable(value = "id") long id) {
      return heroService.findById(id);
   }

   @PreAuthorize("hasAuthority('USER')")
   @GetMapping
   List<HeroDto> findByName(@RequestParam(value = "name") String name) {
      return heroService.findByName(name);
   }

   @PreAuthorize("hasAuthority('ADMIN')")
   @DeleteMapping("/{id}")
   HeroDto delete(@PathVariable(value = "id") Long id) {
      return heroService.delete(id);
   }

   @PreAuthorize("hasAuthority('USER')")
   @GetMapping("/all")
   List<HeroDto> all() {
      return heroService.findAll();
   }

}
