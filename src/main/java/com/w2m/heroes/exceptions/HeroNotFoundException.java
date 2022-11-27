package com.w2m.heroes.exceptions;

public class HeroNotFoundException extends RuntimeException {

   public HeroNotFoundException(Long id) {
      super("Could not find hero " + id);
   }

}
