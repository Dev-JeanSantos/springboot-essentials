package com.academy.fourtk.springbootessentials.repositories;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.utils.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
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
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime saveAnime = this.repository.save(anime);

        //AssertThat => verfique que
        Assertions.assertThat(saveAnime).isNotNull();
        Assertions.assertThat(saveAnime.getId()).isNotNull();
        Assertions.assertThat(saveAnime.getName()).isEqualTo(anime.getName());

    }

    @Test
    @DisplayName("saved update anime when sucessful")
    public void saved_UpdateAnime_WhenSucessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

        Anime saveAnime = this.repository.save(anime);
        saveAnime.setName("DBZ");
        Anime updateAnime = repository.save(saveAnime);

        Assertions.assertThat(updateAnime).isNotNull();
        Assertions.assertThat(saveAnime.getId()).isNotNull();
        Assertions.assertThat(updateAnime.getName()).isEqualTo(saveAnime.getName());

    }
    @Test
    @DisplayName("find by name returns list of anime when sucessfull")
    public void findByName_ReturnsListOfAnime_WhenSucessfull() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();

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

    @Test
    @DisplayName("save throw ConstraintValidationException when name is empty")
    public void save_ThrowConstraintValidationException_WhenNameisEmpty() {
        Anime anime = new Anime();
//        Assertions.assertThatThrownBy(() -> repository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.save(anime))
                .withMessageContaining("The anime name cannot be empty");
    }
}