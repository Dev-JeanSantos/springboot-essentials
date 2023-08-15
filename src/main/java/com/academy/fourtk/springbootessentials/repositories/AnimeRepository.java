package com.academy.fourtk.springbootessentials.repositories;

import com.academy.fourtk.springbootessentials.entities.Anime;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AnimeRepository {
    public List<Anime> listAll();
}
