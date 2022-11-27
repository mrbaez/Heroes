package com.w2m.heroes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.entities.Hero;

@Mapper
public interface HeroMapper {

   HeroDto toDto(Hero entity);

   Hero fromDto(HeroDto dto);

   @Mappings({ @Mapping(target = "id", ignore = true) })
   Hero merge(HeroDto dto, @MappingTarget Hero entity);
}
