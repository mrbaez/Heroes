package com.w2m.heroes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.heroes.entities.Hero;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

}
