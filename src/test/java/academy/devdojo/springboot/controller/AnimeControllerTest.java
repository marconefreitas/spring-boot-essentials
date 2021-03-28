package academy.devdojo.springboot.controller;


import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.requests.AnimePostRequestBody;
import academy.devdojo.springboot.requests.AnimePutRequestBody;
import academy.devdojo.springboot.service.AnimeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    @BeforeEach
    void setup(){
        PageImpl<Anime> anime = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        when(animeService.listAll(any())).thenReturn(anime);
        when(animeService.listNonPageable()).thenReturn(List.of(AnimeCreator.createValidAnime()));
        when(animeService.findById(anyLong())).thenReturn(AnimeCreator.createValidAnime());
        when(animeService.findByName(anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));


        when(animeService.save(any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());
        when(animeService.replace(any(AnimePutRequestBody.class))).thenReturn(AnimeCreator.createUpdatedAnime());

        doNothing().when(animeService).delete(anyLong());
    }


    @Test
    @DisplayName("list that returns at least one anime inside page")
    void listAll_success(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> page =   animeController.list(null).getBody();

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(page.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("list that returns at least one anime without page")
    void list_success(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes =   animeController.listAll().getBody();

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

        Anime animeFound = this.animeController.findById(1).getBody();

        Assertions.assertThat(animeFound)
                .isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(animeId);

    }

    @Test
    @DisplayName("find an anime by name")
    void findByName_whenSuccess(){

        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes =   animeController.findByName("Testeteste").getBody();

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
        List<Anime> animes =   animeController.findByName("Testeteste").getBody();

        Assertions.assertThat(animes)
                .isEmpty();

    }

    @Test
    @DisplayName("creates a anime successful")
    void createAnime_whenSuccess(){
        Anime anime = AnimeCreator.createValidAnime();

        Anime savedAnime = this.animeController.save(AnimePostRequestBodyCreator.createValidAnimePostReqBody()).getBody();

        Assertions.assertThat(savedAnime)
                .isNotNull()
                .isEqualTo(anime);
    }

    @Test
    @DisplayName("update a anime successful")
    void updateAnime_whenSuccess(){
        Anime anime = AnimeCreator.createUpdatedAnime();

        Anime updatedAnime = this.animeController.replace(AnimePutRequestBodyCreator.createValidAnimePutReqBody()).getBody();

        Assertions.assertThat(updatedAnime)
                .isNotNull()
                .isEqualTo(anime);
    }


    @Test
    @DisplayName("delete a anime successful")
    void deleteAnime_whenSuccess(){
       //Anime anime = AnimeCreator.createUpdatedAnime();
        Assertions.assertThatCode(()-> this.animeController.delete(1l))
                .doesNotThrowAnyException();

        ResponseEntity<Void> delete = this.animeController.delete(1l);


        Assertions.assertThat(delete.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }
}