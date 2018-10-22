package com.patrykzdral.musicalworldcore.services.concert.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.Address;
import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import com.patrykzdral.musicalworldcore.persistance.entity.ConcertInstrumentSlot;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertInstrumentSlotRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.InstrumentRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertInstrumentSlotService;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.user.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ConcertServiceImpl implements ConcertService {
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final ConcertInstrumentSlotRepository concertInstrumentSlotRepository;
    @Autowired
    public ConcertServiceImpl(ConcertRepository concertRepository,
                              UserRepository userRepository, InstrumentRepository instrumentRepository,
                              ConcertInstrumentSlotRepository concertInstrumentSlotRepository) {
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.concertInstrumentSlotRepository = concertInstrumentSlotRepository;
    }

    @Override
    public List<Concert> findAll() {
        return concertRepository.findAll();
    }

    @Override
    @Transactional
    public Concert save(ConcertDTO concert) {
        User user;
        Optional<User> optionalUser = userRepository.findByUsername(concert.getUsername());
        if(!optionalUser.isPresent()){
            throw new InternalException("Creation exception", "User does not exists");
        }
        List<ConcertInstrumentSlot> concertInstrumentSlots = new ArrayList<>();

        user=optionalUser.get();
        Concert concertToSave = Concert.builder()
                .name(concert.getName())
                .description(concert.getDescription())
                .dateFrom(concert.getDateFrom())
                .dateTo(concert.getDateTo())
                .user(user)
                .address(Address.builder()
                        .address(concert.getAddress().getAddress())
                        .city(concert.getAddress().getCity())
                        .country(concert.getAddress().getCountry())
                        .latitude(concert.getAddress().getLatitude())
                        .longitude(concert.getAddress().getLongitude())
                        .postalCode(concert.getAddress().getPostalCode())
                        .street(concert.getAddress().getStreet())
                        .build())
                .ensuredDrive(concert.isEnsuredDrive())
                .guaranteedMeal(concert.isGuaranteedMeal())
                .numberOfRehearsals(concert.getNumberOfRehearsals())
                //.concertInstrumentSlots(concert.getConcertInstrumentSlots())
                .build();

        concertRepository.save(concertToSave);


        concert.getConcertInstrumentSlots()
                .forEach(istr ->{
                    IntStream.range(0, istr.getQuantity().intValue())
                            .forEach(num->{ConcertInstrumentSlot c= ConcertInstrumentSlot.builder()
                                            .concert(concertToSave)
                                            .taken(false)
                                            .instrument(instrumentRepository
                                                    .findByName(istr.getName())).build();
                                                    concertInstrumentSlotRepository.save(c);});
                });
        return concertRepository.save(concertToSave);
    }

}
