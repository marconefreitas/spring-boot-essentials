package academy.devdojo.springboot.mapper;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.requests.AnimePostRequestBody;
import academy.devdojo.springboot.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class AnimeMapper {

    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);


    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
