package com.patrykzdral.musicalworldcore.persistance.entity;

import com.patrykzdral.musicalworldcore.persistance.entity.Role;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @OneToMany
    private List<Role> roles;

}
