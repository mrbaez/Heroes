package com.w2m.heroes.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.entities.Hero;

class HeroMapperTest {

   private static final HeroMapper MAPPER = Mappers.getMapper(HeroMapper.class);

   @Test
   void toDto() {
      Hero hero = Hero.builder().name("Thor").id(1L).build();
      HeroDto heroDto = MAPPER.toDto(hero);

      assertThat(hero.getId()).isEqualTo(heroDto.getId());
      assertThat(hero.getName()).isEqualTo(heroDto.getName());
   }

   @Test
   void fromDto() {
      HeroDto heroDto = HeroDto.builder().name("Iron Man").id(1L).build();
      Hero hero = MAPPER.fromDto(heroDto);

      assertThat(hero.getId()).isEqualTo(heroDto.getId());
      assertThat(hero.getName()).isEqualTo(heroDto.getName());
   }

   @Test
   void merge() {
      HeroDto heroDto = HeroDto.builder().name("Spider Man").build();
      Hero hero = Hero.builder().name("Batman").id(1L).build();
      Hero hero1 = MAPPER.merge(heroDto, hero);
      assertThat(hero1.getId()).isEqualTo(1L);
      assertThat(hero1.getName()).isEqualTo("Spider Man");
   }
}
