package com.patrykzdral.musicalworldcore.controller.concert;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/concerts")
@Slf4j
public class ConcertController {

    private final
    ConcertService concertService;

    private final
    ModelMapper modelMapper;

    @Autowired
    public ConcertController(ConcertService concertService, ModelMapper modelMapper) {
        this.concertService = concertService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createConcert(@Valid @RequestBody ConcertDTO request,
                                    HttpServletRequest httpServletRequest, Locale locale) {
        log.info(request.toString());
        log.info(httpServletRequest.toString());
        log.info(locale.toString());
        concertService.save(request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getAllConcerts() {
        List<Concert> concerts = concertService.findAll();
        log.info(concerts.toString());
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    private ConcertDTOG convertToDto(Concert concert) {
        return modelMapper.map(concert, ConcertDTOG.class);
    }
}
