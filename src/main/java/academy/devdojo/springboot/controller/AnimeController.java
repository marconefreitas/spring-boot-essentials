package academy.devdojo.springboot.controller;


import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.requests.AnimePostRequestBody;
import academy.devdojo.springboot.requests.AnimePutRequestBody;
import academy.devdojo.springboot.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    @PageableAsQueryParam
    @Operation(summary = "list all animes paginated", description = "The default size is 20", tags = {"anime"})
    public ResponseEntity<Page<Anime>> list(@Parameter(hidden = true)
                                                        Pageable pageable){
        return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);

    }
    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        return new ResponseEntity<>(animeService.listNonPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        return new ResponseEntity<>(animeService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "by-id/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        log.info(userDetails);
        return new ResponseEntity<>(animeService.findById(id), HttpStatus.OK);
    }


    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return new ResponseEntity<>(animeService.findByName(name), HttpStatus.OK);
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){
        Anime animeResp = animeService.save(anime);
        return new ResponseEntity<>(animeResp, HttpStatus.CREATED);

    }

    @DeleteMapping("/admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When anime not exists in the database"),
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Anime> replace(@RequestBody AnimePutRequestBody anime){
        Anime animeResp = animeService.replace(anime);
        return new ResponseEntity<>(animeResp, HttpStatus.CREATED);
    }
}
