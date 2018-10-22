package com.patrykzdral.musicalworldcore.services.instrument.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Instrument;

import java.util.List;


public interface InstrumentService {
    public List<Instrument> findAll();

}
