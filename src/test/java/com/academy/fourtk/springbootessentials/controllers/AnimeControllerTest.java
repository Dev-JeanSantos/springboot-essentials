package com.academy.fourtk.springbootessentials.controllers;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;
import com.academy.fourtk.springbootessentials.services.AnimeService;
import com.academy.fourtk.springbootessentials.utils.AnimeCreator;
import com.academy.fourtk.springbootessentials.utils.AnimePostRequestBodyCreate;
import com.academy.fourtk.springbootessentials.utils.AnimePutRequestBodyCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class) //Não sobe todo o contexto do spring
@DisplayName("Tests of controllers")
class AnimeControllerTest {

    @InjectMocks //Na classe testada
    private AnimeController controller;

    @Mock //Todas as classes injetadas na classe a serem testadas
    private AnimeService serviceMockk;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(serviceMockk.listAllPagination(ArgumentMatchers.any()))
                .thenReturn(animePage);
        BDDMockito.when(serviceMockk.listAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(serviceMockk.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());
        BDDMockito.when(serviceMockk.listAllByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(serviceMockk.save(ArgumentMatchers.any(AnimePostRequesteBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());
        //doNothing => Não faça nada (usado para retornos do tipo void)
        BDDMockito.doNothing().when(serviceMockk).replace(ArgumentMatchers.any(AnimePutRequesteBody.class));
        BDDMockito.doNothing().when(serviceMockk).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("ListAllPagination returns list of anime inside page object when successful")
    void ListAllPagination_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = controller.list(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime inside when successful")
    void ListAll_ReturnsListOfAnimeInside_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = controller.listAll().getBody();
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("findById return anime when successful")
    void findById_ReturnAnime_WhenSuccessful() {

        Long idAnime = AnimeCreator.createValidAnime().getId();

        Anime anime = controller.findById(1).getBody();
        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(idAnime);
    }
    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {

        String nameAnime = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = controller.listByName(nameAnime).getBody();
        List<Anime> animeList = controller.listAll().getBody();
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(nameAnime);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime is not found")
    void findByName_ReturnsAnEmptyListOfAnime_IsNotFound() {

        BDDMockito.when(serviceMockk.listAllByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.EMPTY_LIST);

        List<Anime> animes = controller.listByName("DBZ").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }
    @Test
    @DisplayName("save returns anime when sucessful")
    void save_ReturnsAnime_WhenSuccesfull() {
        Long idAnime = AnimeCreator.createValidUpdateAnime().getId();
        Anime animeSaved = controller.save(AnimePostRequestBodyCreate.createAnimePostrequestBody()).getBody();

        Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("replace update anime when sucessful")
    void replace_UpdateAnime_WhenSuccesfull() {

        controller.replace(AnimePutRequestBodyCreate.createAnimePutrequestBody()).getBody();

        Assertions.assertThatCode(() -> controller.replace(AnimePutRequestBodyCreate.createAnimePutrequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = controller.replace(AnimePutRequestBodyCreate.createAnimePutrequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemoveAnime_WhenSuccesfull() {
        Assertions.assertThatCode(() -> controller.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = controller.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}