package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String name);

}
