package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertInstrumentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertInstrumentSlotRepository extends JpaRepository<ConcertInstrumentSlot,Long> {

}
