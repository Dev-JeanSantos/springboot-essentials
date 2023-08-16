package com.academy.fourtk.springbootessentials.controllers;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;
import com.academy.fourtk.springbootessentials.services.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RequiredArgsConstructor //injeta todas variaveis com o final
@RestController
@RequestMapping("api/v1/animes")
public class AnimeController {
//    Logger logger = Logger.getLogger(AnimeController.class.getName());
    private final AnimeService service;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable) {
        return new ResponseEntity<>(service.listAllPagination(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll() {
        return new ResponseEntity<>(service.listAll(), HttpStatus.OK);
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> listByName(@RequestParam String name) {
        return new ResponseEntity<>(service.listAllByName(name), HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return new ResponseEntity<>(service.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/admin")
    public ResponseEntity<Void> replace(@Valid @RequestBody AnimePutRequesteBody animePutRequesteBody) {
        service.replace(animePutRequesteBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping(path = "/admin")
    public ResponseEntity<Anime> save(@Valid @RequestBody AnimePostRequesteBody animePostRequesteBody) {
        return new ResponseEntity<>(service.save(animePostRequesteBody), HttpStatus.CREATED);
    }
}
