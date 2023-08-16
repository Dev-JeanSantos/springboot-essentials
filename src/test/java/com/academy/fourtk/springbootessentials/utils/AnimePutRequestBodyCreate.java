package com.academy.fourtk.springbootessentials.utils;

import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;

public class AnimePutRequestBodyCreate {
    public static AnimePutRequesteBody createAnimePutRequestBody(){
    return AnimePutRequesteBody.builder()
            .id(AnimeCreator.createValidUpdateAnime().getId())
            .name(AnimeCreator.createValidUpdateAnime().getName())
            .build();
    }
}
