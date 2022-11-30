package com.w2m.heroes.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.w2m.heroes.mappers.HeroMapper;

@Configuration
public class MapperConfig {

   @Bean
   public HeroMapper heroMapper() {
      return Mappers.getMapper(HeroMapper.class);
   }

}
