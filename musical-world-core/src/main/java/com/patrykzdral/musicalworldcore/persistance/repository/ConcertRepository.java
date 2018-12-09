package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Override
    Optional<Concert> findById(Long id);

    @Query("Select c FROM Concert c where not c.user.username=?1")
    List<Concert> findAllNotUserConcerts(String name);

    @Query("Select c FROM Concert c where c.user.username=?1")
    List<Concert> findAllUserConcerts(String name);

}
