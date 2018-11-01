package com.patrykzdral.musicalworldcore.services.concert.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;

import java.util.List;

public interface AdminConcertsService {
    List<Concert> findAdminConcerts(String username);
    Concert deleteConcertById(Long id);
    ConcertDTO update(ConcertDTO concertDTO, Long id);
}
