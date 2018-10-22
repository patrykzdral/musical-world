package com.patrykzdral.musicalworldcore.services.concert.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;

import java.util.List;

public interface ConcertService {

    public List<Concert> findAll();

    public Concert save(ConcertDTO concert);

}
