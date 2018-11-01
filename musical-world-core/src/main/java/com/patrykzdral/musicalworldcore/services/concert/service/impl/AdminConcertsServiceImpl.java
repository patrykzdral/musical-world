package com.patrykzdral.musicalworldcore.services.concert.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Address;
import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertRepository;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.service.AdminConcertsService;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminConcertsServiceImpl implements AdminConcertsService {

    private final
    ConcertRepository concertRepository;

    @Autowired
    public AdminConcertsServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<Concert> findAdminConcerts(String username) {
        return concertRepository.findAllUserConcerts(username);
    }

    @Override
    public Concert deleteConcertById(Long id) {
        Optional<Concert> concertOptional = concertRepository.findById(id);
        concertOptional.ifPresentOrElse(concertRepository::delete,
                () -> {
                    throw new InternalException("Something went wrong", "concert does not exists");
                });
        return concertOptional.get();
    }

    @Override
    public ConcertDTO update(ConcertDTO concertDTO, Long id) {
        Optional<Concert> optionalConcert = concertRepository.findById(id);
        optionalConcert.ifPresentOrElse(
                concert -> {
                    concert.toBuilder().name(concertDTO.getName())
                            .dateFrom(concertDTO.getDateFrom())
                            .dateTo(concertDTO.getDateTo())
                            .ensuredDrive(concertDTO.isEnsuredDrive())
                            .guaranteedMeal(concertDTO.isGuaranteedMeal())
                            .numberOfRehearsals(concertDTO.getNumberOfRehearsals())
                            .build();
                    concert.getAddress().toBuilder()
                            .address(concertDTO.getAddress().getAddress())
                            .longitude(concertDTO.getAddress().getLongitude())
                            .latitude(concertDTO.getAddress().getLatitude())
                            .street(concertDTO.getAddress().getStreet())
                            .postalCode(concertDTO.getAddress().getPostalCode())
                            .city(concertDTO.getAddress().getCity())
                            .country(concertDTO.getAddress().getCountry()).build();
                }
                ,
                () -> {
                    throw new InternalException("Something went wrong", "concert does not exists");
                });
        return concertDTO;
    }
}
