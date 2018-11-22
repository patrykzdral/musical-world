package com.patrykzdral.musicalworldcore.services.concert.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertWithPhotoDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertWithPhotoDTOG;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConcertService {

    List<Concert> findAll();

    List<Concert> findAllNotUserEvents(String name);

    Concert save(ConcertDTO concert) throws IOException;

    Optional<Concert> findOne(Long id);

    List<Concert> filterConcerts(String username, String name, List<InstrumentDTO> instruments, Date dateFrom,Date dateTo);

    List<ConcertWithPhotoDTOG> findAllNotUserEventsWithPhoto(String name);
}
