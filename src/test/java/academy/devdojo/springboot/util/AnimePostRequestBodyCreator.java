package academy.devdojo.springboot.util;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createValidAnimePostReqBody(){
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
