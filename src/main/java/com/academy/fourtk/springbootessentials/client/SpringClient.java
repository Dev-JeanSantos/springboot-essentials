package com.academy.fourtk.springbootessentials.client;

import com.academy.fourtk.springbootessentials.entities.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
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
    }
}
