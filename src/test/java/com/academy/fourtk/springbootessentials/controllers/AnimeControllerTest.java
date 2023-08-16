package com.academy.fourtk.springbootessentials.controllers;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.services.AnimeService;
import com.academy.fourtk.springbootessentials.utils.AnimeCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class) //Não sobe todo o contexto do spring
@DisplayName("Tests of controllers")
class AnimeControllerTest {

    @InjectMocks //Na classe testada
    private  AnimeController controller;

    @Mock //Todas as classes injetadas na classe a serem testadas
    private AnimeService serviceMockk;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(serviceMockk.listAllPagination(ArgumentMatchers.any()))
                .thenReturn(animePage);
    }
    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void ListAllPagination_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = controller.list(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

}