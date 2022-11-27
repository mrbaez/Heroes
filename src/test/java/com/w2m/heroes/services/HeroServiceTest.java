package com.w2m.heroes.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.entities.Hero;
import com.w2m.heroes.exceptions.HeroNotFoundException;
import com.w2m.heroes.mappers.HeroMapper;
import com.w2m.heroes.repositories.HeroRepository;

@ExtendWith(MockitoExtension.class)
class HeroServiceTest {

   private HeroService service;

   @Mock
   private HeroRepository repository;

   @BeforeEach
   void setUp() {
      this.service = new HeroService(Mappers.getMapper(HeroMapper.class), repository);
   }

   @Test
   void create() {
      given(repository.save(any(Hero.class))).willReturn(Hero.builder().id(1L).name("Iron Man").build());

      final var result = service.create(HeroDto.builder().name("Iron Man").build());

      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getName()).isEqualTo("Iron Man");
   }

   @Test
   void update_when_exist_hero() {
      final var hero = Hero.builder().id(1L).name("Iron Man").build();

      given(repository.findById(1L)).willReturn(Optional.of(hero));
      given(repository.save(argThat((Hero h) -> "Thor".equals(h.getName())))).willReturn(Hero.builder().id(1L).name("Thor").build());

      final HeroDto result = service.update(1L, HeroDto.builder().name("Thor").build());

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getName()).isEqualTo("Thor");
   }

   @Test
   void update_when_not_exist_hero() {
      given(repository.findById(1L)).willReturn(Optional.empty());

      assertThatThrownBy(() -> service.update(1, HeroDto.builder().name("Thor").build())).isExactlyInstanceOf(HeroNotFoundException.class);
   }

   @Test
   void delete_when_exist_hero() {
      final var hero1 = Hero.builder().id(1L).name("Iron Man").build();
      given(repository.findById(1L)).willReturn(Optional.of(hero1));

      var heroDeleted = service.delete(1L);

      assertThat(heroDeleted).isNotNull();
      assertThat(heroDeleted.getId()).isEqualTo(hero1.getId());
   }

   @Test
   void delete_when_not_exist_hero() {
      given(repository.findById(1L)).willReturn(Optional.empty());
      assertThatThrownBy(() -> service.delete(1L)).isExactlyInstanceOf(HeroNotFoundException.class);
   }

   @Test
   void findAll() {
      final var heroes = List.of(Hero.builder().id(1L).name("Iron Man").build(), Hero.builder().id(2L).name("Thor").build());

      given(repository.findAll()).willReturn(heroes);

      var all = service.findAll();
      assertThat(all).isNotNull();
      assertThat(all.size()).isEqualTo(2);
   }

   @Test
   void findById_when_exist_hero() {
      final var hero1 = Hero.builder().id(1L).name("Iron Man").build();
      given(repository.findById(1L)).willReturn(Optional.of(hero1));

      HeroDto result = service.findById(1L);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(hero1.getId());
   }

   @Test
   void findById_when_not_exist_hero() {
      given(repository.findById(1L)).willReturn(Optional.empty());
      assertThatThrownBy(() -> service.findById(1L)).isExactlyInstanceOf(HeroNotFoundException.class);
   }

   @Test
   void findByName_when_exist_hero() {
      final List<Hero> heroes = Arrays.asList(Hero.builder().id(1L).name("Iron Man").build(), Hero.builder().id(2L).name("Superman").build());

      given(repository.findByNameContainsIgnoreCase("man")).willReturn(heroes);

      final var results = service.findByName("man");
      assertThat(results).isNotNull();
      assertThat(results.size()).isEqualTo(2);
   }

   @Test
   void findByName_when_not_exist_hero() {
      given(repository.findByNameContainsIgnoreCase("man")).willReturn(Collections.emptyList());

      final List<HeroDto> results = service.findByName("man");
      assertThat(results).isNotNull();
      assertThat(results).isEmpty();
   }
}
