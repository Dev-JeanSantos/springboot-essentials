package com.academy.fourtk.springbootessentials.utils;

import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;

public class AnimePostRequestBodyCreate {
    public static AnimePostRequesteBody createAnimePostrequestBody(){
    return AnimePostRequesteBody.builder()
            .name(AnimeCreator.createAnimeToBeSaved().getName())
            .build();
    }
}
