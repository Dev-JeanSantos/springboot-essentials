package com.academy.fourtk.springbootessentials.services;

import com.academy.fourtk.springbootessentials.entities.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnimeServices {

    private static List<Anime> animes;
    static {
        animes = new ArrayList<>(List.of(new Anime(1L,"Fullmetal Alchemist"),
                new Anime(2L,"Demon Slayer"),
                new Anime(3L," Ginga eiyû densetsu "),
                new Anime(4L,"Kakegurui "),
                new Anime(5L,"JoJo's Bizarre Adventure ")));
    }
    public List<Anime> listAll(){
        return animes;
    }

    public Anime findById(long id) {
        return animes.stream().filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not Found "));
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(6, 10000));
        animes.add(anime);
        return anime;
    }

    public void delete(long id) {
        animes.remove(findById(id));
    }
    public void replace(Anime anime) {
        delete(anime.getId());
        animes.add(anime);
    }
}
