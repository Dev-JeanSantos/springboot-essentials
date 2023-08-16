package com.academy.fourtk.springbootessentials.utils;

import com.academy.fourtk.springbootessentials.entities.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Cavaleiro do Zodíaco")
                .build();
    }
    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1L)
                .name("Cavaleiro do Zodíaco")
                .build();
    }
    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .name("Dragon Ball Z")
                .build();
    }
}
