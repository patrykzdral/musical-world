package com.patrykzdral.musicalworldcore.controller.instrument;

import com.patrykzdral.musicalworldcore.persistance.entity.Instrument;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import com.patrykzdral.musicalworldcore.services.instrument.service.InstrumentService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instruments")
@Slf4j
public class InstrumentController {

    private final
    InstrumentService instrumentService;

    private final ModelMapper modelMapper;

    @Autowired
    public InstrumentController(InstrumentService instrumentService, ModelMapper modelMapper) {
        this.instrumentService = instrumentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<InstrumentDTO> getAllInstruments() {
        List<Instrument> instruments = instrumentService.findAll();
        return instruments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InstrumentDTO convertToDto(Instrument instrument) {
        return modelMapper.map(instrument, InstrumentDTO.class);
    }

}
