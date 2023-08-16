package com.academy.fourtk.springbootessentials.services;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.exceptions.BadRequestException;
import com.academy.fourtk.springbootessentials.repositories.AnimeRepository;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;
import lombok.RequiredArgsConstructor;
import com.academy.fourtk.springbootessentials.mappers.AnimeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository repository;

    public Page<Anime> listAllPagination(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public List<Anime> listAll() {
        return repository.findAll();
    }
    public List<Anime> listAllByName(String name) {
        return repository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return repository.findById(id).orElseThrow(
                () -> new BadRequestException("Anime with id:"+ id + " not Found "));
    }

    @Transactional
    public Anime save(AnimePostRequesteBody animePostRequesteBody) {
        return repository.save(AnimeMapper.INSTANCE.toAnime(animePostRequesteBody));
    }

    public void delete(long id) {

        repository.delete(findByIdOrThrowBadRequestException(id));
    }

    @Transactional
    public void replace(AnimePutRequesteBody animePutRequesteBody) {
        Anime possibleAnime = findByIdOrThrowBadRequestException(animePutRequesteBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequesteBody);
        anime.setId(possibleAnime.getId());
        repository.save(anime);
    }
}
