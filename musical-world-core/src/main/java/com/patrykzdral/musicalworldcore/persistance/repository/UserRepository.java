package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String name);
}
