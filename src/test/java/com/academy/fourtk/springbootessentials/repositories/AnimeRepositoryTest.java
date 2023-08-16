package com.academy.fourtk.springbootessentials.repositories;

import com.academy.fourtk.springbootessentials.entities.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@DisplayName("Tests of repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository repository;

    @Test
    @DisplayName("saved persitence anime when sucessful")
    //NOME DO METODO A TESTAR_O QUE ELE PRECISA FAZER_O RESULTADO DO METODO
    public void saved_PersitenceAnime_WhenSucessful() {
        Anime anime = createAnime();

        Anime saveAnime = this.repository.save(anime);

        //AssertThat => verfique que
        Assertions.assertThat(saveAnime).isNotNull();
        Assertions.assertThat(saveAnime.getId()).isNotNull();
        Assertions.assertThat(saveAnime.getName()).isEqualTo(anime.getName());

    }
    @Test
    @DisplayName("find by name returns list of anime when sucessfull")
    public void findByName_ReturnsListOfAnime_WhenSucessfull() {
        Anime anime = createAnime();

        Anime saveAnime = this.repository.save(anime);
        String name = saveAnime.getName();

        List<Anime> animes = repository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(saveAnime);
    }

    @Test
    @DisplayName("find by name returns empty list  when anime is not found")
    public void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {

        List<Anime> animes = repository.findByName("Xerxes");

        Assertions.assertThat(animes).isEmpty();
    }

    private Anime createAnime() {
        return Anime.builder()
                .name("Cavaleiro do Zodíaco")
                .build();
    }
}