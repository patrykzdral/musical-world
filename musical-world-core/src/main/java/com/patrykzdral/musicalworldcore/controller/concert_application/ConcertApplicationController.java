package com.patrykzdral.musicalworldcore.controller.concert_application;

import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.service.ConcertApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/concert-applications")
@Slf4j
public class ConcertApplicationController {

    private final
    ConcertApplicationService concertApplicationService;

    @Autowired
    public ConcertApplicationController(ConcertApplicationService concertApplicationService) {
        this.concertApplicationService = concertApplicationService;
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createConcert(@Valid @RequestBody ConcertApplicationDTO request,
                              HttpServletRequest httpServletRequest, Locale locale) {
        log.info(request.toString());
        log.info(httpServletRequest.toString());
        log.info(locale.toString());
        concertApplicationService.save(request);
    }
}
