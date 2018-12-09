package com.patrykzdral.musicalworldcore.controller.concert_application;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertApplication;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationExamineDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.service.ConcertApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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
    public void createConcertApplication(@Valid @RequestBody ConcertApplicationDTO request, Authentication authentication) {
        concertApplicationService.save(request,((User) authentication.getPrincipal()).getUsername());
    }

    @PostMapping(value = "/examine", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void examineConcertApplication(Authentication authentication, @RequestBody ConcertApplicationExamineDTO
                                                                        concertApplicationExamineDTO) {
        log.info(concertApplicationExamineDTO.toString());
        concertApplicationService.examine(concertApplicationExamineDTO, ((User) authentication.getPrincipal()).getUsername());
    }

    @GetMapping(value = "/list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertApplication> getApplications(@PathVariable Long id) {
        return concertApplicationService.getConcertApplications(id);
    }

}
