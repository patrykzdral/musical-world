package com.patrykzdral.musicalworldcore.services.instrument.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Instrument;
import com.patrykzdral.musicalworldcore.persistance.repository.InstrumentRepository;
import com.patrykzdral.musicalworldcore.services.instrument.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {
    private final InstrumentRepository instrumentRepository;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }
}
