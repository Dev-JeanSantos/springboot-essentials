package com.academy.fourtk.springbootessentials.services;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.exceptions.BadRequestException;
import com.academy.fourtk.springbootessentials.repositories.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests of services")
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeRepository repositoryMockk;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(repositoryMockk.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);
        BDDMockito.when(repositoryMockk.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(repositoryMockk.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(repositoryMockk.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(repositoryMockk.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());
        //doNothing => Não faça nada (usado para retornos do tipo void)
        BDDMockito.doNothing().when(repositoryMockk).delete(ArgumentMatchers.any(Anime.class));

    }

    @Test
    @DisplayName("ListAllPagination returns list of anime inside page object when successful")
    void ListAllPagination_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.unsorted());
        Page<Anime> animePage = service.listAllPagination(pageRequest);
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
        List<Anime> animeList = service.listAll();
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("findByIdOrThrowBadRequestException return anime when successful")
    void findByIdOrThrowBadRequestException_ReturnAnime_WhenSuccessful() {

        Long idAnime = AnimeCreator.createValidAnime().getId();

        Anime anime = service.findByIdOrThrowBadRequestException(idAnime);
        Assertions.assertThat(anime)
                .isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(idAnime);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException return throws BadRequestException anime is not found")
    void findByIdOrThrowBadRequestException_ReturnThrowsBadRequestException_AnimeisNotFound() {

        BDDMockito.when(repositoryMockk.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());        Long idAnime = AnimeCreator.createValidAnime().getId();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> service.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("listAllByName returns list of anime when successful")
    void listAllByName_ReturnsListOfAnime_WhenSuccessful() {

        String nameAnime = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = service.listAllByName(nameAnime);
        List<Anime> animeList = service.listAll();
        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(nameAnime);
    }

    @Test
    @DisplayName("listAllByName returns an empty list of anime is not found")
    void flistAllByName_ReturnsAnEmptyListOfAnime_IsNotFound() {

        BDDMockito.when(repositoryMockk.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.EMPTY_LIST);

        List<Anime> animes = service.listAllByName("DBZ");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }
    @Test
    @DisplayName("save returns anime when sucessful")
    void save_ReturnsAnime_WhenSuccesfull() {
        Long idAnime = AnimeCreator.createValidUpdateAnime().getId();
        Anime animeSaved = service.save(AnimePostRequestBodyCreate.createAnimePostrequestBody());

        Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("replace update anime when sucessful")
    void replace_UpdateAnime_WhenSuccesfull() {

        service.replace(AnimePutRequestBodyCreate.createAnimePutRequestBody());

        Assertions.assertThatCode(() -> service.replace(AnimePutRequestBodyCreate.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemoveAnime_WhenSuccesfull() {
        Assertions.assertThatCode(() -> service.delete(1L))
                .doesNotThrowAnyException();
    }

}