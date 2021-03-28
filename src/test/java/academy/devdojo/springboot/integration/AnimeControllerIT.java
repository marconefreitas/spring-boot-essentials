package academy.devdojo.springboot.integration;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.DevDojoUser;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.repository.DevDojoUserRepository;
import academy.devdojo.springboot.requests.AnimePostRequestBody;
import academy.devdojo.springboot.util.AnimeCreator;
import academy.devdojo.springboot.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {


    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private DevDojoUserRepository devDojoUserRepository;

    private final static DevDojoUser ROLE_USER = DevDojoUser.builder()
            .name("User")
            .password("{bcrypt}$2a$10$yWkP0/x8mLdBE0Gns5s9L.SCnoJNQlK31/UqQ7W1ueuGZfnoFCqfK")
            .username("user")
            .authorities("ROLE_USER")
            .build();
    private final static DevDojoUser ROLE_ADMIN = DevDojoUser.builder()
            .name("Marcone")
            .password("{bcrypt}$2a$10$yWkP0/x8mLdBE0Gns5s9L.SCnoJNQlK31/UqQ7W1ueuGZfnoFCqfK")
            .username("marcone")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config{

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreatorUser(@Value("${local.server.port}") int port){
            RestTemplateBuilder restBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                   .basicAuthentication("user", "senha123");
            return new TestRestTemplate(restBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleUserCreatorAdmin(@Value("${local.server.port}") int port){
            RestTemplateBuilder restBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("marcone", "senha123");
            return new TestRestTemplate(restBuilder);
        }
    }

    @Test
    @DisplayName("list that returns at least one anime inside page")
    void listAll_success(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);

        String expectedName = save.getName();

        PageableResponse<Anime> response =  testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();


        Assertions.assertThat(response).isNotNull();

        Assertions.assertThat(response.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(response.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("list that returns at least one anime without page")
    void list_success(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);

        String expectedName = save.getName();

        List<Anime> response =  testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();


        Assertions.assertThat(response).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(response.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("find an anime by id")
    void findById_whenSuccess(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);

        Long  animeId = save.getId();

        Anime animeFound = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, animeId);

        Assertions.assertThat(animeFound)
                .isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(animeId);

    }

    @Test
    @DisplayName("find an anime by name")
    void findByName_whenSuccess(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);


        String expectedName = save.getName();

        List<Anime> response =  testRestTemplateRoleUser.exchange("/animes/find?name=" + expectedName, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);


        Assertions.assertThat(response.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("find an anime by name when it's as empty result")
    void findByName_whenEmpty(){
        devDojoUserRepository.save(ROLE_USER);

        List<Anime> response =  testRestTemplateRoleUser.exchange("/animes/find?name=teste" , HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(response)
                .isNotNull()
                .isEmpty();


    }

    @Test
    @DisplayName("creates a anime successful")
    void createAnime_whenSuccess(){
        AnimePostRequestBody aprb = AnimePostRequestBodyCreator.createValidAnimePostReqBody();
        devDojoUserRepository.save(ROLE_USER);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", aprb, Anime.class);


        Assertions.assertThat(animeResponseEntity)
                .isNotNull()
                .isEqualTo(animeResponseEntity);
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();


    }

    @Test
    @DisplayName("update a anime successful")
    void updateAnime_whenSuccess(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);

        save.setName("Anime alterado 2");




        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/{id}",
                HttpMethod.PUT, new HttpEntity<>(save), Anime.class, save.getId());


        Assertions.assertThat(animeResponseEntity)
                .isNotNull()
                .isEqualTo(animeResponseEntity);
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }


    @Test
    @DisplayName("delete a anime successful")
    void deleteAnime_whenSuccess(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_ADMIN);


        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Anime.class, save.getId());

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete a anime successful - expects a 403 response")
    void deleteAnime_whenUserNotAdmin(){
        Anime save = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ROLE_USER);


        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Anime.class, save.getId());

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

}
