package com.academy.fourtk.springbootessentials.integration;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.repositories.AnimeRepository;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.utils.AnimeCreator;
import com.academy.fourtk.springbootessentials.utils.AnimePostRequestBodyCreate;
import com.academy.fourtk.springbootessentials.wrappers.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Tests of integrations")
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository repository;


    @Test
    @DisplayName("ListAllPagination returns list of anime inside page object when successful")
    void ListAllPagination_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = saveAnime.getName();
        PageableResponse<Anime> animePage = testRestTemplate.exchange("/api/v1/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime inside when successful")
    void ListAll_ReturnsListOfAnimeInside_WhenSuccessful() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = saveAnime.getName();
        List<Anime> animes = testRestTemplate.exchange("/api/v1/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById return anime when successful")
    void findById_ReturnAnime_WhenSuccessful() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        Long idAnime = saveAnime.getId();

        Anime anime = testRestTemplate.getForObject
                ("/api/v1/animes/{id}",
                        Anime.class,
                        idAnime);
        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(idAnime);
    }
    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = saveAnime.getName();
        String url = String.format("/api/v1/animes/find?name=%s", expectedName);

        List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime is not found")
    void findByName_ReturnsAnEmptyListOfAnime_IsNotFound() {
        List<Anime> animeList = testRestTemplate.exchange("/api/v1/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }
    @Test
    @DisplayName("save returns anime when sucessful")
    void save_ReturnsAnime_WhenSuccesfull() {
        AnimePostRequesteBody animePostRequestBody = AnimePostRequestBodyCreate.createAnimePostrequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/api/v1/animes/", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace update anime when sucessful")
    void replace_UpdateAnime_WhenSuccesfull() {

        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        saveAnime.setName("Iron Maiden");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/api/v1/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(saveAnime),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemoveAnime_WhenSuccesfull() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/api/v1/animes/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(saveAnime),
                Void.class,
                saveAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
