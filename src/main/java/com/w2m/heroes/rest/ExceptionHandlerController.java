package com.w2m.heroes.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.w2m.heroes.exceptions.HeroNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

   @ResponseStatus(HttpStatus.NOT_FOUND)
   @ExceptionHandler(HeroNotFoundException.class)
   public void handleNotFound(HeroNotFoundException ex) {
      log.error("Requested hero not found");
   }

   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(IllegalArgumentException.class)
   public void handleBlankName(IllegalArgumentException ex) {
      log.error("Bad request");
   }

}
