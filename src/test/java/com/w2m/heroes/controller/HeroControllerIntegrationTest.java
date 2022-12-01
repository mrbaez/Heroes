package com.w2m.heroes.controller;

import com.w2m.heroes.dto.HeroDto;
import com.w2m.heroes.dto.JwtResponse;
import com.w2m.heroes.dto.LoginRequest;
import com.w2m.heroes.repositories.HeroRepository;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HeroControllerIntegrationTest {

    @Autowired
    private HeroRepository repository;

    @LocalServerPort
    private String port;

    private String uri;

    private RequestSpecification specification;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
        final LoginRequest user = LoginRequest.builder().username("admin").password("admin").build();

        JwtResponse response =
                given().contentType(ContentType.JSON)
                        .body(user)
                        .when()
                        .post(uri + "/api/auth/signin").then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body().as(JwtResponse.class);

        specification =
                new RequestSpecBuilder()
                        .addHeader("Authorization", "Bearer " + response.getToken())
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ErrorLoggingFilter())
                        .build();
    }

    @Test
    void test_get_by_id_one_is_ok() throws Exception {

        given().spec(specification)
                .when()
                .get(uri + "/api/hero/" + 1)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void test_get_by_id_three_is_not_found() throws Exception {
        given().spec(specification)
                .when()
                .get(uri + "/api/hero/" + 3)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void test_create_a_hero() throws Exception {
        int previousSize = repository.findAll().size();

        given().spec(specification).contentType(ContentType.JSON)
                        .body(HeroDto.builder().name("spiderman").build())
                        .when()
                        .post(uri + "/api/hero/").then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value());

        assertThat(repository.findAll().size()).isEqualTo(previousSize + 1);
    }


    @Test
    void test_create_a_hero_without_correct_authorities() throws Exception {

        JwtResponse response =
                given().contentType(ContentType.JSON)
                        .body(LoginRequest.builder().username("user").password("user").build())
                        .when()
                        .post(uri + "/api/auth/signin").then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body().as(JwtResponse.class);

        RequestSpecification specification =
                new RequestSpecBuilder()
                        .addHeader("Authorization", "Bearer " + response.getToken())
                        .build();

        given().spec(specification).contentType(ContentType.JSON)
                .body(HeroDto.builder().name("spiderman").build())
                .when()
                .post(uri + "/api/hero/").then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void test_create_a_hero_without_name() throws Exception {
        given().spec(specification).contentType(ContentType.JSON)
                .body(HeroDto.builder().build())
                .when()
                .post(uri + "/api/hero/").then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void test_update_a_hero() throws Exception {
        given().spec(specification).contentType(ContentType.JSON)
                .body(HeroDto.builder().name("spiderman").build())
                .when()
                .put(uri + "/api/hero/1").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        assertThat(repository.findById(1L).get().getName()).isEqualTo("spiderman");
    }


    @Test
    void test_update_a_hero_without_name() throws Exception {
        given().spec(specification).contentType(ContentType.JSON)
                .body(HeroDto.builder().build())
                .when()
                .put(uri + "/api/hero/1").then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void test_update_a_hero_not_found() throws Exception {
        given().spec(specification).contentType(ContentType.JSON)
                .body(HeroDto.builder().name("spiderman").build())
                .when()
                .put(uri + "/api/hero/100").then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
