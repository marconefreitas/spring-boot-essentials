package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findAll();

    Optional<Anime> findById(Long id);

    List<Anime> findByName(String name);

}
