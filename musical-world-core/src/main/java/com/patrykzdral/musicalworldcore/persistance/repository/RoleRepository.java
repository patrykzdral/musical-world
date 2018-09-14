package com.patrykzdral.musicalworldcore.persistance.repository;

import com.patrykzdral.musicalworldcore.persistance.entity.Role;
import com.patrykzdral.musicalworldcore.persistance.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
