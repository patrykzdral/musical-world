package com.patrykzdral.musicalworldcore.services.concert_application.service;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertApplication;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationExamineDTO;

import java.util.List;

public interface ConcertApplicationService {
    ConcertApplication save(ConcertApplicationDTO concertApplicationDTO, String username);
    List<ConcertApplication> getConcertApplications(Long id);
    void examine(ConcertApplicationExamineDTO concertApplicationExamineDTO, String username);
}
