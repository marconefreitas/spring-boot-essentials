package academy.devdojo.springboot.service;


import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.exception.BadRequestException;
import academy.devdojo.springboot.mapper.AnimeMapper;
import academy.devdojo.springboot.repository.AnimeRepository;
import academy.devdojo.springboot.requests.AnimePostRequestBody;
import academy.devdojo.springboot.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;



    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }
    public List<Anime> listNonPageable(){
        return animeRepository.findAll();
    }

    public Anime findById(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    public Anime save(AnimePostRequestBody anime) {
        Anime savedAnime = animeRepository.save(AnimeMapper.INSTANCE.toAnime(anime));

        return savedAnime;
    }

    public void delete(long id) {
        animeRepository.delete(findById(id));
    }

    public Anime replace(AnimePutRequestBody anime) {
        Anime saved = findById(anime.getId());

        Anime animeBuild = AnimeMapper.INSTANCE.toAnime(anime);
        animeBuild.setId(saved.getId());
        return animeRepository.save(animeBuild);
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

}
