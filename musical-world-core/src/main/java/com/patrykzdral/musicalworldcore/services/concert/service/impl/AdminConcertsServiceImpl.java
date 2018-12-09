package com.patrykzdral.musicalworldcore.services.concert.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertApplicationRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertInstrumentSlotRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertRepository;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertInstrumentSlotDeleteDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.AdminConcertsService;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminConcertsServiceImpl implements AdminConcertsService {

    private final
    ConcertRepository concertRepository;

    private final
    ConcertApplicationRepository concertApplicationRepository;

    private final
    ConcertInstrumentSlotRepository concertInstrumentSlotRepository;

    @Autowired
    public AdminConcertsServiceImpl(ConcertRepository concertRepository, ConcertApplicationRepository concertApplicationRepository, ConcertInstrumentSlotRepository concertInstrumentSlotRepository) {
        this.concertRepository = concertRepository;
        this.concertApplicationRepository = concertApplicationRepository;
        this.concertInstrumentSlotRepository = concertInstrumentSlotRepository;
    }

    @Override
    public List<Concert> findAdminConcerts(String username) {
        return concertRepository.findAllUserConcerts(username);
    }

    @Override
    public Concert deleteConcertById(Long id, String username) {
        var concertReference = new Object() {
            Concert concert;
        };
        Optional<Concert> concertOptional = concertRepository.findById(id);
        concertOptional.ifPresentOrElse(concert -> {
                    if (!concert.getUser().getUsername().equals(username)) {
                        throw new ApplicationException("Auth error", "Trying to delete not user concert");

                    }
                    concertReference.concert = concert;
                    concertRepository.deleteById(id);
                }
                ,
                () -> {
                    throw new ApplicationException("Something went wrong", "concert does not exists");
                });

        return concertReference.concert;

    }

    @Override
    public ConcertDTOG update(ConcertDTOG concertDTO, String username) {
        Optional<Concert> optionalConcert = concertRepository.findById(concertDTO.getId());
        optionalConcert.ifPresentOrElse(
                concert -> {
                    if (!concert.getUser().getUsername().equals(username))
                        throw new ApplicationException("Auth error", "Trying to update not user concert");
                    Concert updatedConcert;
                    updatedConcert = concert.toBuilder().name(concertDTO.getName())
                            .description(concertDTO.getDescription())
                            .dateFrom(concertDTO.getDateFrom())
                            .dateTo(concertDTO.getDateTo())
                            .ensuredDrive(concertDTO.isEnsuredDrive())
                            .guaranteedMeal(concertDTO.isGuaranteedMeal())
                            .numberOfRehearsals(concertDTO.getNumberOfRehearsals())
                            .build();
                    updatedConcert.setAddress(concert.getAddress().toBuilder()
                            .address(concertDTO.getAddress().getAddress())
                            .longitude(concertDTO.getAddress().getLongitude())
                            .latitude(concertDTO.getAddress().getLatitude())
                            .street(concertDTO.getAddress().getStreet())
                            .postalCode(concertDTO.getAddress().getPostalCode())
                            .city(concertDTO.getAddress().getCity())
                            .country(concertDTO.getAddress().getCountry()).build());
                    concertRepository.save(updatedConcert);
                }
                ,
                () -> {
                    throw new ApplicationException("Something went wrong", "concert does not exists");
                });
        return concertDTO;
    }

    @Override
    public void assignPhotoToConcert(MultipartFile file, Long id) throws IOException {
        Optional<Concert> optionalConcert = concertRepository.findById(id);
        if (optionalConcert.isPresent()) {
            Picture picture = Picture.builder()
                    .fileName(file.getOriginalFilename())
                    .mimetype(file.getContentType())
                    .pic(file.getBytes())
                    .build();
            optionalConcert.get().setPicture(picture);
            concertRepository.save(optionalConcert.get());
        } else throw new ApplicationException("Concert exception", "Concert does not exists");
    }

    @Override
    @Transactional
    public void deleteConcertInstrumentSlots(List<ConcertInstrumentSlotDeleteDTO> concertInstrumentSlotDeleteDTOList) {

        concertInstrumentSlotDeleteDTOList.forEach(concertApp -> {
            concertApplicationRepository.deleteAllByConcertInstrumentSlotId(concertApp.getId());
            concertInstrumentSlotRepository.deleteById(concertApp.getId());
        });
    }


}
