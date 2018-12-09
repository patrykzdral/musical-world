package com.patrykzdral.musicalworldcore.controller.concert;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertInstrumentSlotDeleteDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.AdminConcertsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
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
    public void deleteConcert(@PathVariable("id") Long id, Authentication authentication) {
        adminConcertsService.deleteConcertById(id, ((User) authentication.getPrincipal()).getUsername());
    }

    @PostMapping(value = "delete-applications", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteConcertApplications(@RequestBody List<ConcertInstrumentSlotDeleteDTO> concertApplicationDTO) {
        adminConcertsService.deleteConcertInstrumentSlots(concertApplicationDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConcertDTOG> getAllAdminConcerts(Authentication authentication) {
        List<Concert> concerts = adminConcertsService.findAdminConcerts(((User) authentication.getPrincipal()).getUsername());
        log.info(concerts.toString());
        return concerts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    @PutMapping(value = "update")
    @ResponseStatus(HttpStatus.OK)
    public ConcertDTOG update(@RequestBody ConcertDTOG concert, Authentication authentication) {
        return adminConcertsService.update(concert, ((User) authentication.getPrincipal()).getUsername());
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
