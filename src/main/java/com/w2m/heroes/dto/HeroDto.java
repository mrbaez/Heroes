package com.w2m.heroes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class HeroDto {

   Long id;

   @NotBlank
   String name;
}
