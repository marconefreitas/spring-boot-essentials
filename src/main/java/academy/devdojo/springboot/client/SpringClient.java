package academy.devdojo.springboot.client;

import academy.devdojo.springboot.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        /*
        ResponseEntity<Anime> forEntity = restTemplate
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 22);
        log.info(forEntity);
        Anime[] forObject = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(forObject));

        ResponseEntity<List<Anime>> animes = restTemplate.exchange("http://localhost:8080/animes/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
        log.info(animes.getBody());
        */
        Anime anime = new Anime();
        ResponseEntity<Anime> animeGet = restTemplate.exchange("http://localhost:8080/animes/{id}",
                HttpMethod.GET, new HttpEntity<>(anime), Anime.class, 23 );
        log.info("Consulta anime unico " + animeGet.getBody());


        /*Anime sakura = Anime.builder().name("Sakura Card Captor").build();
        ResponseEntity<Anime> animeGet = restTemplate.exchange("http://localhost:8080/animes/",
                HttpMethod.POST, new HttpEntity<>(sakura), Anime.class);
        log.info("Anime criado " + animeGet.getBody());


        Anime animeUpdate = animeGet.getBody();
        animeUpdate.setName("Sakura Card Captors 2");

        ResponseEntity<Anime> animeUpdated = restTemplate.exchange("http://localhost:8080/animes/{id}",
                HttpMethod.PUT, new HttpEntity<>(animeUpdate), Anime.class, animeUpdate.getId());
        log.info(animeUpdated.getBody());
        */

        ResponseEntity<Void> animeDeleted = restTemplate.exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE, null, Void.class, animeGet.getBody().getId());
        log.info(animeDeleted.getBody());


    }

}
