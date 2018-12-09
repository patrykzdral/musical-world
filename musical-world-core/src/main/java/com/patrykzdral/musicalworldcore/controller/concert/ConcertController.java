package com.patrykzdral.musicalworldcore.controller.concert;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertWithPhotoDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import com.patrykzdral.musicalworldcore.services.user.model.CustomUserDetails;
import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
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
    public void createConcert(Authentication authentication, @Valid @RequestBody ConcertDTO request) {
            concertService.save(request,((User) authentication.getPrincipal()).getUsername());

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

    @GetMapping(value = "/not-user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getAllNotUserConcerts( Authentication authentication) {
        List<Concert> concerts = concertService.findAllNotUserEvents(((User) authentication.getPrincipal()).getUsername());
        log.info(concerts.toString());
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    @GetMapping(value = "/not-user-with-photo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertWithPhotoDTOG> getAllNotUserConcertsWithPhoto(Authentication authentication) {
        return concertService.findAllNotUserEventsWithPhoto(((User) authentication.getPrincipal()).getUsername());
    }

    @GetMapping(value = "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getFilteredConcerts(Authentication authentication, @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "instruments", required = false) List<String> instruments,
                                                 @RequestParam(value = "dateFrom", required = false) Date dateFrom,
                                                 @RequestParam(value = "dateTo", required = false) Date dateTo) {
        List<Concert> concerts = concertService.filterConcerts(((User) authentication.getPrincipal()).getUsername(), name, instruments, dateFrom, dateTo);
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ConcertDTOG getOneConcert(@PathVariable("id") Long id) {
        var concertDTO = new Object() {
            ConcertDTOG concert = null;
        };
        Optional<Concert> concerts = concertService.findOne(id);
        concerts.ifPresentOrElse(concert -> concertDTO.concert = convertToDto(concert),
                () -> {

                });
        log.info(concerts.toString());
        return concertDTO.concert;

    }


    private ConcertDTOG convertToDto(Concert concert) {
        byte[] photo;
        ConcertDTOG dto = modelMapper.map(concert, ConcertDTOG.class);
        Optional<Picture> concertPhoto = Optional.ofNullable(concert.getPicture());
        if (concertPhoto.isPresent()) {
            photo = concertPhoto.get().getPic();
            StringBuilder base64 = new StringBuilder("data:image/png;base64,");
            base64.append(Base64.getEncoder().encodeToString(photo));
            dto.setPicture(base64.toString());
        }
        return dto;
    }
}
