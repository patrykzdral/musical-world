package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.ConcertApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertApplicationRepository extends JpaRepository<ConcertApplication, Long> {

    @Query("Select c FROM ConcertApplication c where c.concertInstrumentSlot.concert.id=?1")
    List<ConcertApplication> getAllByConcertInstrumentSlotConcert(Long id);

    @Query("Select c FROM ConcertApplication c where c.concertInstrumentSlot.id=?1")
    List<ConcertApplication> getAllByConcertInstrumentSlotId(Long id);

//    @Query("DELETE FROM ConcertApplication c where c.concertInstrumentSlot.id=?1")
    void deleteAllByConcertInstrumentSlotId(Long id);
}
