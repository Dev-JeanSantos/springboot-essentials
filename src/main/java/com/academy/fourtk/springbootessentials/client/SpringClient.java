package com.academy.fourtk.springbootessentials.client;

import com.academy.fourtk.springbootessentials.entities.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {

        //TESTANDO GETTERS
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/api/v1/animes/1", Anime.class);
        log.info(entity);

        //TESTANDO GETALL COM GETFOROBJECT
        Anime object = new RestTemplate().getForObject("http://localhost:8080/api/v1/animes/{id}", Anime.class, 5);
        log.info(object);

        //TESTANDO GETALL COM EXCHANGE
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange(
                "http://localhost:8080/api/v1/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                });
        log.info(exchange.getBody());

        //TESTANDO POST COM POSTFOROBJECT
        Anime DBZ = Anime.builder().name("DBZ").build();
        Anime newAnime = new RestTemplate().postForObject("http://localhost:8080/api/v1/animes/admin/", DBZ, Anime.class);
        log.info("saved anime {}", newAnime);

        //TESTANDO POST COM EXCHANGE
        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange("http://localhost:8080/api/v1/animes/admin/",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo, createJsonHeader()),
                Anime.class);
        log.info("saved anime {}", samuraiChamplooSaved);

        //TESTANDO UPDATE COM EXCHANGE
        Anime animeUpdate = samuraiChamplooSaved.getBody();
        animeUpdate.setName("Samurai Champloo 2");
        ResponseEntity<Void> animePut = new RestTemplate().exchange("http://localhost:8080/api/v1/animes/admin/",
                HttpMethod.PUT,
                new HttpEntity<>(animeUpdate, createJsonHeader()),
                Void.class);
        log.info(animeUpdate);

        //TESTANDO DELETE COM EXCHANGE
        ResponseEntity<Void> animeDelete = new RestTemplate().exchange("http://localhost:8080/api/v1/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeUpdate.getId());
        log.info(animeDelete);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
