package com.academy.fourtk.springbootessentials.controllers;

import com.academy.fourtk.springbootessentials.entities.Anime;
import com.academy.fourtk.springbootessentials.requesties.AnimePostRequesteBody;
import com.academy.fourtk.springbootessentials.requesties.AnimePutRequesteBody;
import com.academy.fourtk.springbootessentials.services.AnimeServices;
import com.academy.fourtk.springbootessentials.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RequiredArgsConstructor //injeta todas variaveis com o final
@RestController
@RequestMapping("api/v1/animes")
public class AnimeController {
//    Logger logger = Logger.getLogger(AnimeController.class.getName());
    private final DateUtil dateUtil;
    private final AnimeServices service;

    @GetMapping
    public ResponseEntity<List<Anime>> list() {
//        logger.info();
       log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(service.listAll(), HttpStatus.OK);
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> listByName(@RequestParam String name) {
        return new ResponseEntity<>(service.listAllByName(name), HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return new ResponseEntity<>(service.findByIdOrThrowBadrequestException(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequesteBody animePutRequesteBody) {
        service.replace(animePutRequesteBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequesteBody animePostRequesteBody) {
        return new ResponseEntity<>(service.save(animePostRequesteBody), HttpStatus.CREATED);
    }
}
