package com.w2m.heroes.controller;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.exceptions.HeroNotFoundException;
import com.w2m.heroes.services.HeroService;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.BDDMockito.given;

//No uso @WebMvcTest dado que por la configuración de seguridad necesita algunos servicios que no se inyectan.
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="test", password = "test", authorities = {"ADMIN", "USER"})
class HeroControllerTest {

    private static final String BASE_PATH = "/api/hero";
    private static final long HERO_ID = 1L;
    public static final String NAME = "name";

    @MockBean
    private HeroService heroService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    void create_hero_should_return_created() {

        final HeroDto heroDto = HeroDto.builder()
                .name(NAME)
                .id(HERO_ID)
                .build();

        final HeroDto body = HeroDto.builder().name(NAME).build();

        given(this.heroService.create(body))
                .willReturn(heroDto);

        RestAssuredMockMvc
                .given()
                .body(body)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .post(BASE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void create_hero_without_body_should_return_bad_request() {

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .post(BASE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_hero_with_invalid_body_should_return_bad_request() {

        final HeroDto body = HeroDto.builder().build();

        RestAssuredMockMvc
                .given()
                .body(body)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .post(BASE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void get_by_id_should_return_ok() {

        final HeroDto heroDto = HeroDto.builder()
                .id(HERO_ID)
                .name(NAME)
                .build();
        given(this.heroService.findById(HERO_ID))
                .willReturn(heroDto);

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .get(BASE_PATH + "/{id}", HERO_ID)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void get_by_id_should_return_not_found() {

        given(this.heroService.findById(HERO_ID))
                .willThrow(new HeroNotFoundException(HERO_ID));

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .get(BASE_PATH + "/{id}", HERO_ID)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_hero_should_return_ok() {

        final HeroDto heroDto = HeroDto.builder()
                .name(NAME)
                .id(HERO_ID)
                .build();

        final HeroDto body = HeroDto.builder().name(NAME).build();

        given(this.heroService.update(HERO_ID, heroDto))
                .willReturn(heroDto);

        RestAssuredMockMvc
                .given()
                .body(body)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .put(BASE_PATH + "/{id}", HERO_ID)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void update_hero_without_body_should_return_bad_request() {


        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .put(BASE_PATH + "/{id}", HERO_ID)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_hero_with_invalid_body_should_return_bad_request() {

        final HeroDto body = HeroDto.builder().build();

        RestAssuredMockMvc
                .given()
                .body(body)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .put(BASE_PATH + "/{id}", HERO_ID)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void get_by_name_should_return_ok() {

        final HeroDto heroDto = HeroDto.builder()
                .id(HERO_ID)
                .name(NAME)
                .build();
        given(this.heroService.findByName(NAME))
                .willReturn(List.of(heroDto));

        RestAssuredMockMvc
                .given()
                .queryParam(NAME, NAME)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .get(BASE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void get_by_name__without_name_should_return_bad_request() {

        given(this.heroService.findByName(""))
                .willThrow(new IllegalArgumentException("name can not be empty"));

        RestAssuredMockMvc
                .given()
                .queryParam(NAME, "")
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .get(BASE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void list_all_should_return_ok() {

        final HeroDto heroDto = HeroDto.builder()
                .id(HERO_ID)
                .name(NAME)
                .build();

        given(this.heroService.findAll())
                .willReturn(List.of(heroDto));

        RestAssuredMockMvc
                .given()
                .queryParam(NAME, NAME)
                .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .when()
                .get(BASE_PATH + "/all")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
    }
}
