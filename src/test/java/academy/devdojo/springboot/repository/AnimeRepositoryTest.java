package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Testes for anime repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;



    @Test
    @DisplayName("save created anime when successful")
    void save_success(){

        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime save = this.animeRepository.save(anime);
        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isNotNull();
        Assertions.assertThat(save.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("update anime when successful")
    void update_success(){

        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime save = this.animeRepository.save(anime);
        save.setName("Claymore");
        Anime updated =  this.animeRepository.save(anime);


        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getId()).isNotNull();
        Assertions.assertThat(updated.getName()).isEqualTo(save.getName());
    }

    @Test
    @DisplayName("delete anime when successful")
    void delete_success(){

        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(anime);

        this.animeRepository.delete(animeSaved);
        Optional<Anime> byId = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(byId).isEmpty();

    }

    @Test
    @DisplayName("find anime by name successful")
    void findByName_success(){

        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(anime);

        List<Anime> animes = this.animeRepository.findByName(anime.getName());
        Assertions.assertThat(animes).isNotEmpty()
                .contains(animeSaved);
        Assertions.assertThat(animeSaved.getName()).isEqualTo(anime.getName());

    }

    @Test
    @DisplayName("find anime by name when no anime found")
    void findByName_empty(){
        List<Anime> animes = this.animeRepository.findByName("Test");
        Assertions.assertThat(animes).isEmpty();
    }

    /*private Anime AnimecrcreateAnimeToBeSave(){
        return Anime.builder().name("Hajime no Ippo").build();
    }*/


    @Test
    @DisplayName("save created anime when ConstraintViolationException when name is empty")
    void save_throwException(){
        Anime anime = new Anime();
       /* Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);
*/
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("Anime name cannot be empty");
    }
}