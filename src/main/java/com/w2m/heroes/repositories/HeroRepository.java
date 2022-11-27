package com.w2m.heroes.repositories;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.heroes.entities.Hero;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

   List<Hero> findByNameContainsIgnoreCase(final String name);
}
