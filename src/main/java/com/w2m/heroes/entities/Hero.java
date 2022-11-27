package com.w2m.heroes.entities;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hero {

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @EqualsAndHashCode.Include
   private Long id;

   private String name;

}
