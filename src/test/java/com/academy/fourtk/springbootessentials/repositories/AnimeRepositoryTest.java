package com.academy.fourtk.springbootessentials.repositories;

import com.academy.fourtk.springbootessentials.entities.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    private Anime createAnime() {
        return Anime.builder()
                .name("Cavaleiro do Zodíaco")
                .build();
    }
}