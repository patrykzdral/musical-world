package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "concert_application")
@Data
@NoArgsConstructor
public class ConcertApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "concert_instrument_slot_id")
    private ConcertInstrumentSlot concertInstrumentSlot;

    private boolean accepted;

}
