package academy.devdojo.springboot.service;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.exception.BadRequestException;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.util.AnimeCreator;
import academy.devdojo.springboot.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    @BeforeEach
    void setup(){
        PageImpl<Anime> anime = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        when(animeRepository.findAll(any(PageRequest.class))).thenReturn(anime);

        when(animeRepository.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));
        when(animeRepository.findById(anyLong())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));
        when(animeRepository.findByName(anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));

        when(animeRepository.save(any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        doNothing().when(animeRepository).delete(any(Anime.class));
    }


    @Test
    @DisplayName("list that returns at least one anime inside page")
    void listAll_success(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> page =   animeService.listAll(PageRequest.of(1,2));

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page).isNotEmpty().hasSize(1);

        Assertions.assertThat(page.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("list that returns at least one anime without page")
    void list_success(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes =   animeService.listNonPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(animes).isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("find an anime by id")
    void findById_whenSuccess(){
        Long  animeId = AnimeCreator.createValidAnime().getId();

        Anime animeFound = this.animeService.findById(1);

        Assertions.assertThat(animeFound)
                .isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(animeId);

    }

    @Test
    @DisplayName("find an anime by id when throw an error")
    void findById_whenError(){
        when(animeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy( () ->  this.animeService.findById(1));

    }


    @Test
    @DisplayName("find an anime by name")
    void findByName_whenSuccess(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes =   animeService.findByName("Testeteste");

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(animes).isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("find an anime by name when it's as empty result")
    void findByName_whenEmpty(){

        when(animeService.findByName(anyString())).thenReturn(Collections.emptyList());

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes =   animeService.findByName("Testeteste");

        Assertions.assertThat(animes)
                .isEmpty();

    }

    @Test
    @DisplayName("creates a anime successful")
    void createAnime_whenSuccess(){
        Anime anime = AnimeCreator.createValidAnime();

        Anime savedAnime = this.animeService.save(AnimePostRequestBodyCreator.createValidAnimePostReqBody());

        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(anime);
    }

    @Test
    @DisplayName("update a anime successful")
    void updateAnime_whenSuccess(){
        Assertions.assertThatCode(()-> animeService.replace(AnimePutRequestBodyCreator.createValidAnimePutReqBody()))
            .doesNotThrowAnyException();
    }


    @Test
    @DisplayName("delete a anime successful")
    void deleteAnime_whenSuccess(){
        //Anime anime = AnimeCreator.createUpdatedAnime();
        Assertions.assertThatCode(()-> this.animeService.delete(1l))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("update a anime when error")
    void updateAnime_whenError(){
        when(animeRepository.findById(anyLong())).thenReturn(Optional.empty());


        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(()-> this.animeService.findById(1));




    }

}