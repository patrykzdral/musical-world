package com.patrykzdral.musicalworldcore.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Data
public class Role{

    @Id
    @GeneratedValue
    private Long id;
    @Getter
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users;

    public Role() {
    }
}
