package com.patrykzdral.musicalworldcore.services.concert_application.service;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertApplication;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationDTO;

public interface ConcertApplicationService {
    ConcertApplication save(ConcertApplicationDTO concertApplicationDTO);

}
