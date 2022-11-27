package com.w2m.heroes.services;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.entities.Hero;
import com.w2m.heroes.exceptions.HeroNotFoundException;
import com.w2m.heroes.mappers.HeroMapper;
import com.w2m.heroes.repositories.HeroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeroService {

   private final HeroMapper heroMapper;

   private final HeroRepository heroRepository;

   public HeroDto create(final HeroDto heroDto) {
      final Hero hero = heroMapper.fromDto(heroDto);
      return heroMapper.toDto(heroRepository.save(hero));
   }

   public HeroDto update(final long id, final HeroDto heroDto) {
      log.info("updating hero with id: {}", id);
      final Hero hero = heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException(id));
      final Hero updated = heroRepository.save(heroMapper.merge(heroDto, hero));
      return heroMapper.toDto(updated);
   }

   public HeroDto delete(final long id) {
      log.info("trying to delete hero {}", id);
      final Hero hero = heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException(id));
      heroRepository.deleteById(id);
      return heroMapper.toDto(hero);
   }

   public List<HeroDto> findAll() {
      return heroRepository.findAll().stream().map(heroMapper::toDto).collect(Collectors.toList());
   }

   public HeroDto findById(final long id) {
      log.info("finding hero with id: {}", id);
      final Hero hero = heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException(id));
      return heroMapper.toDto(hero);
   }

   public List<HeroDto> findByName(final String name) {
      log.info("finding heroes with name: {}", name);
      if (isNull(name) || name.isBlank()) {
         throw new IllegalArgumentException("name can not be empty");
      }
      return heroRepository.findByNameContainsIgnoreCase(name).stream().map(heroMapper::toDto).collect(Collectors.toList());
   }

}
