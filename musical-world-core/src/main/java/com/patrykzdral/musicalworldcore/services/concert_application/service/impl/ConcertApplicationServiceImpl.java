package com.patrykzdral.musicalworldcore.services.concert_application.service.impl;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertApplication;
import com.patrykzdral.musicalworldcore.persistance.entity.ConcertInstrumentSlot;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertApplicationRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.ConcertInstrumentSlotRepository;
import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.dto.ConcertApplicationExamineDTO;
import com.patrykzdral.musicalworldcore.services.concert_application.service.ConcertApplicationService;
import com.patrykzdral.musicalworldcore.validation.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertApplicationServiceImpl implements ConcertApplicationService {

    private final ConcertApplicationRepository concertApplicationRepository;

    private final UserRepository userRepository;

    private final ConcertInstrumentSlotRepository concertInstrumentSlotRepository;

    @Autowired
    public ConcertApplicationServiceImpl(ConcertApplicationRepository concertApplicationRepository, UserRepository userRepository, ConcertInstrumentSlotRepository concertInstrumentSlotRepository) {
        this.concertApplicationRepository = concertApplicationRepository;
        this.userRepository = userRepository;
        this.concertInstrumentSlotRepository = concertInstrumentSlotRepository;
    }

    @Override
    @Transactional
    public ConcertApplication save(ConcertApplicationDTO concertApplicationDTO) {
        ConcertInstrumentSlot concertInstrumentSlotFound;
        User userFound;
        Optional<User> optionalUser = userRepository.findByUsername(concertApplicationDTO.getUsername());
        Optional<ConcertInstrumentSlot> optionalConcertInstrumentSlot =
                concertInstrumentSlotRepository.findById(concertApplicationDTO.getConcertInstrumentSlot().getId());
        if (optionalConcertInstrumentSlot.isPresent()) {
            concertInstrumentSlotFound = optionalConcertInstrumentSlot.get();
            if (concertInstrumentSlotFound.isTaken()) {
                throw new InternalException("Creation exception", "Slot is taken");
            }
        } else {
            throw new InternalException("Creation exception", "Slot does not exists");
        }

        if (optionalUser.isPresent()) {
            userFound = optionalUser.get();
        } else {
            throw new InternalException("Creation exception", "User does not exists");
        }
        return concertApplicationRepository.save(ConcertApplication.builder()
                .accepted(concertApplicationDTO.isAccepted())
                .user(userFound)
                .concertInstrumentSlot(concertInstrumentSlotFound)
                .build());
    }

    @Override
    public List<ConcertApplication> getConcertApplications(Long id) {
        return concertApplicationRepository.getAllByConcertInstrumentSlot_Concert(id);
    }

    @Override
    public void examine(ConcertApplicationExamineDTO concertApplicationExamineDTO) {
        Optional<ConcertApplication> optionalConcertApplication = concertApplicationRepository.findById(concertApplicationExamineDTO.getId());
        optionalConcertApplication.ifPresentOrElse(
                value -> {
                    if (concertApplicationExamineDTO.isAccepted()) {
                        value.getConcertInstrumentSlot().setTaken(true);
                        value.getConcertInstrumentSlot().setUser(value.getUser());
                        concertApplicationRepository.delete(value);
                        List<ConcertApplication> concertApplications = concertApplicationRepository.findAll();
                        concertApplications.forEach(concertApplication -> {
                                    if(concertApplication.getConcertInstrumentSlot().equals(value.getConcertInstrumentSlot())){
                                        concertApplicationRepository.delete(concertApplication);
                                    }
                                }
                        );
                    } else{
                        concertApplicationRepository.delete(value);
                    }
                },
                () -> {
                    throw new InternalException("Something went wrong", "concert application does not exists");
                }

        );

    }


}
