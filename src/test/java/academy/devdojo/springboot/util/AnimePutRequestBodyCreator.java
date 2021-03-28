package academy.devdojo.springboot.util;

import academy.devdojo.springboot.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createValidAnimePutReqBody(){
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createUpdatedAnime().getName())
                .id(AnimeCreator.createUpdatedAnime().getId())
                .build();
    }
}
