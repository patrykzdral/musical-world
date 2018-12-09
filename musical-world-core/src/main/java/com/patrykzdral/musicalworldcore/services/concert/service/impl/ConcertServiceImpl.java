package com.patrykzdral.musicalworldcore.services.concert.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.*;
import com.patrykzdral.musicalworldcore.persistance.repository.*;
import com.patrykzdral.musicalworldcore.services.concert.dto.ConcertDTO;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertWithPhotoDTOG;
import com.patrykzdral.musicalworldcore.services.concert.service.ConcertService;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import com.patrykzdral.musicalworldcore.validation.exception.ApplicationException;
import com.patrykzdral.musicalworldcore.validation.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ConcertServiceImpl implements ConcertService {
    private final ModelMapper modelMapper;
    private final ConcertRepository concertRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final ConcertInstrumentSlotRepository concertInstrumentSlotRepository;

    @Autowired
    public ConcertServiceImpl(ModelMapper modelMapper, ConcertRepository concertRepository,
                              PictureRepository pictureRepository, UserRepository userRepository, InstrumentRepository instrumentRepository,
                              ConcertInstrumentSlotRepository concertInstrumentSlotRepository) {
        this.modelMapper = modelMapper;
        this.concertRepository = concertRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.concertInstrumentSlotRepository = concertInstrumentSlotRepository;
    }
    @Override
    @Transactional
    public Concert save(ConcertDTO concert, String username)  {
        log.info(concert.getPictureName());
        Picture picture = null;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<Picture> optionalPicture = pictureRepository.findByFileName(concert.getPictureName());
        if (!optionalUser.isPresent()) {
            throw new ApplicationException(ExceptionCode.EXCEPTION_005, "User does not exists");
        }
        if (optionalPicture.isPresent()) {
            picture = optionalPicture.get();
        }
        User user = optionalUser.get();

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
                .picture(picture)

                .ensuredDrive(concert.isEnsuredDrive())
                .guaranteedMeal(concert.isGuaranteedMeal())
                .numberOfRehearsals(concert.getNumberOfRehearsals())
                //.concertInstrumentSlots(concert.getConcertInstrumentSlots())
                .build();

        concertRepository.save(concertToSave);


        concert.getConcertInstrumentSlots()
                .forEach(istr -> IntStream.range(0, istr.getQuantity().intValue())
                        .forEach(num -> {
                            ConcertInstrumentSlot c = ConcertInstrumentSlot.builder()
                                    .concert(concertToSave)
                                    .taken(false)
                                    .instrument(instrumentRepository
                                            .findByName(istr.getName())).build();
                            concertInstrumentSlotRepository.save(c);
                        }));
        return concertRepository.save(concertToSave);
    }

    @Override
    public List<Concert> findAll() {
        return concertRepository.findAll();
    }

    @Override
    public List<Concert> findAllNotUserEvents(String name) {
        return concertRepository.findAllNotUserConcerts(name);
    }

    @Override
    public Optional<Concert> findOne(Long id) {
        return concertRepository.findById(id);
    }

    @Override
    public List<Concert> filterConcerts(String username, String name, List<String> instruments, Date dateFrom, Date dateTo) {
        List<Concert> filteredConcerts = concertRepository.findAllNotUserConcerts(username);
        if (name != null) filteredConcerts = filteredConcerts.stream().filter(concert -> concert
                .getName().toLowerCase()
                .contains(name.toLowerCase()))
                .collect(Collectors.toList());

        if (dateFrom != null){
            log.info(dateFrom.toString());
            filteredConcerts = filteredConcerts.stream().filter(filteredConcert -> filteredConcert.getDateFrom().after(dateFrom)).collect(Collectors.toList());
        }
        if (dateTo != null){
            log.info(dateTo.toString());
            filteredConcerts = filteredConcerts.stream().filter(filteredConcert -> filteredConcert.getDateFrom().before(dateTo)).collect(Collectors.toList());
        }

        if (instruments != null) filteredConcerts = filteredConcerts
                .stream()
                .filter(concert -> filterConcert(concert, instruments))
                .collect(Collectors.toList());

        return filteredConcerts;
    }

    @Override
    public List<ConcertWithPhotoDTOG> findAllNotUserEventsWithPhoto(String name) {
        List<Concert> concertList = concertRepository.findAllNotUserConcerts(name);
        return concertList.stream().map(concert -> {
                    try {
                        return convertToDto(concert);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    private static boolean filterConcert(Concert concert, List<String> instruments) {
        return concert.getConcertInstrumentSlots().stream().anyMatch(
                concertInstrumentSlot -> instruments
                        .stream()
                        .anyMatch(instrumentDTO -> instrumentDTO.equals(concertInstrumentSlot.getInstrument().getName()))
        );
    }




    private ConcertWithPhotoDTOG convertToDto(Concert concert) throws IOException {
        byte[] photo;
        ConcertWithPhotoDTOG dto = modelMapper.map(concert, ConcertWithPhotoDTOG.class);
        dto.setUsername(concert.getUser().getUsername());
        Optional<Picture> concertPhoto = Optional.ofNullable(concert.getPicture());
        if (concertPhoto.isPresent()) {
            photo = concertPhoto.get().getPic();
            StringBuilder base64 = new StringBuilder("data:image/png;base64,");
            base64.append(Base64.getEncoder().encodeToString(photo));
            dto.setPicture(base64.toString());
        }
        return dto;
    }


}
