package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @Getter
    private String name;


}
