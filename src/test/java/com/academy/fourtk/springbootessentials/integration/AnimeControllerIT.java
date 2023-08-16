package com.academy.fourtk.springbootessentials.integration;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.entities.DevUser;
import com.academy.fourtk.springbootessentials.repositories.AnimeRepository;
import com.academy.fourtk.springbootessentials.repositories.DevUserRepository;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.utils.AnimeCreator;
import com.academy.fourtk.springbootessentials.utils.AnimePostRequestBodyCreate;
import com.academy.fourtk.springbootessentials.wrappers.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private AnimeRepository repository;
    @Autowired
    private DevUserRepository userRepository;


    private static final DevUser USER = DevUser.builder()
            .name("Jean Santos")
            .password("{bcrypt}$2a$10$OTD161mTzw8q4EhYHpRjGuP.Kpc6hbuor8uKLKTPLIFloOtQDkFW6")
            .username("jean")
            .authorities("ROLE_USER")
            .build();

    private static final DevUser ADMIN = DevUser.builder()
            .name("camilla Santos")
            .password("{bcrypt}$2a$10$OTD161mTzw8q4EhYHpRjGuP.Kpc6hbuor8uKLKTPLIFloOtQDkFW6")
            .username("devjs")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("jean", "jean");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("devjs", "jean");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("ListAllPagination returns list of anime inside page object when successful")
    void ListAllPagination_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        userRepository.save(USER);
        String expectedName = saveAnime.getName();
        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/api/v1/animes", HttpMethod.GET, null,
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
        userRepository.save(USER);
        String expectedName = saveAnime.getName();
        List<Anime> animes = testRestTemplateRoleUser.exchange("/api/v1/animes/all", HttpMethod.GET, null,
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
        userRepository.save(USER);
        Long idAnime = saveAnime.getId();

        Anime anime = testRestTemplateRoleUser.getForObject
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
        userRepository.save(USER);
        String expectedName = saveAnime.getName();
        String url = String.format("/api/v1/animes/find?name=%s", expectedName);

        List<Anime> animeList = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
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
        userRepository.save(USER);
        List<Anime> animeList = testRestTemplateRoleUser.exchange("/api/v1/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns anime when sucessful")
    void save_ReturnsAnime_WhenSuccesfull() {
        userRepository.save(ADMIN);
        AnimePostRequesteBody animePostRequestBody = AnimePostRequestBodyCreate.createAnimePostrequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleAdmin.postForEntity("/api/v1/animes/admin/", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace update anime when sucessful")
    void replace_UpdateAnime_WhenSuccesfull() {

        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        userRepository.save(ADMIN);
        saveAnime.setName("Iron Maiden");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
                "/api/v1/animes/admin/",
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
        userRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
                "/api/v1/animes/admin/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(saveAnime),
                Void.class,
                saveAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 when user not Admin")
    void delete_Returns403_WhenUserNotAdmin() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        userRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange(
                "/api/v1/animes/admin/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(saveAnime),
                Void.class,
                saveAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    @DisplayName("delete returns 401 when user not found")
    void delete_Returns401_WhenUserNotFound() {
        Anime saveAnime = repository.save(AnimeCreator.createAnimeToBeSaved());
        userRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange(
                "/api/v1/animes/admin/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(saveAnime),
                Void.class,
                saveAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
