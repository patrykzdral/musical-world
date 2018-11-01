package com.patrykzdral.musicalworldcore.controller.concert;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.AdminConcertsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/concerts/admin")
@Slf4j
public class AdminConcertController {
    private final
    AdminConcertsService adminConcertsService;
    private final
    ModelMapper modelMapper;
    @Autowired
    public AdminConcertController(AdminConcertsService adminConcertsService, ModelMapper modelMapper) {
        this.adminConcertsService = adminConcertsService;
        this.modelMapper = modelMapper;
    }

    //fail przy mapowaniu
    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ConcertDTOG deleteConcert(@PathVariable("id") Long id) {
        Concert concert = adminConcertsService.deleteConcertById(id);
        return null;

    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getAllAdminConcerts(@RequestParam String name) {
        List<Concert> concerts = adminConcertsService.findAdminConcerts(name);
        log.info(concerts.toString());
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    @PutMapping(value = "update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConcertDTO update(@PathVariable("id") Long id, @RequestParam ConcertDTO concert){
         return  adminConcertsService.update(concert,id);
    }

    private ConcertDTOG convertToDto(Concert concert) {
        log.info(modelMapper.map(concert, ConcertDTOG.class).toString());
        return modelMapper.map(concert, ConcertDTOG.class);
    }

}
