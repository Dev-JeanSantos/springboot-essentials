package com.academy.fourtk.springbootessentials.services;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.repositories.AnimeRepository;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;
import lombok.RequiredArgsConstructor;
import com.academy.fourtk.springbootessentials.mapper.AnimeMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeServices {

    private final AnimeRepository repository;

    public List<Anime> listAll() {
        return repository.findAll();
    }
    public List<Anime> listAllByName(String name) {
        return repository.findByName(name);
    }

    public Anime findByIdOrThrowBadrequestException(long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not Found "));
    }

    public Anime save(AnimePostRequesteBody animePostRequesteBody) {
        return repository.save(AnimeMapper.INSTANCE.toAnime(animePostRequesteBody));
    }

    public void delete(long id) {

        repository.delete(findByIdOrThrowBadrequestException(id));
    }

    public void replace(AnimePutRequesteBody animePutRequesteBody) {
        Anime possibleAnime = findByIdOrThrowBadrequestException(animePutRequesteBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequesteBody);
        anime.setId(possibleAnime.getId());
        repository.save(anime);
    }
}
