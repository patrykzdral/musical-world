package com.patrykzdral.musicalworldcore.controller.concert;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.FilteredDataDTO;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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

    @GetMapping(value= "/not-user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getAllNotUserConcerts(@RequestParam String name) {
        List<Concert> concerts = concertService.findAllNotUserEvents(name);
        log.info(concerts.toString());
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    @GetMapping(value= "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getFilteredConcerts(@RequestParam(value = "name",required = false) String name ,
                                                 @RequestParam(value = "instruments", required=false) List<InstrumentDTO> instruments,
                                                 @RequestParam(value = "dateFrom",required = false)  Date dateFrom,
                                                 @RequestParam(value = "dateTo", required = false) Date dateTo) {
        List<Concert> concerts = concertService.filterConcerts(name,instruments,dateFrom,dateTo);
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

//    @GetMapping(value= "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public List<ConcertDTOG> getFilteredConcerts(@RequestParam(value = "filterData",required = false)FilteredDataDTO filteredData) {
////        DateTimeFormatter format = DateTimeFormatter.;
////        Date dateFromm = Date.
////        log.info(dateFromm.toString());
//        List<Concert> concerts = concertService.filterConcerts(filteredData.getName(),null,
//                filteredData.getDateFrom(),filteredData.getDateTo());
//        return concerts.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());

//    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ConcertDTOG getOneConcert(@PathVariable("id") Long id) {
        var concertDTO = new Object() {
            ConcertDTOG concert=null;
        };
        Optional<Concert> concerts = concertService.findOne(id);
        concerts.ifPresentOrElse(concert -> {concertDTO.concert=convertToDto(concert);},
        () -> {

        });
        log.info(concerts.toString());
        return concertDTO.concert;

    }

    private ConcertDTOG convertToDto(Concert concert) {
        log.info(modelMapper.map(concert, ConcertDTOG.class).toString());
        return modelMapper.map(concert, ConcertDTOG.class);
    }
}
