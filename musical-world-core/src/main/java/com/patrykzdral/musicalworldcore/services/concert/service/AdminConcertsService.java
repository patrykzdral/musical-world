package com.patrykzdral.musicalworldcore.services.concert.service;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertInstrumentSlotDeleteDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminConcertsService {
    List<Concert> findAdminConcerts(String username);
    Concert deleteConcertById(Long id, String username);
    ConcertDTOG update(ConcertDTOG concertDTO, String username);
    void assignPhotoToConcert(MultipartFile file, Long id) throws IOException;

    void deleteConcertInstrumentSlots(List<ConcertInstrumentSlotDeleteDTO> concertApplicationDTO);
}
