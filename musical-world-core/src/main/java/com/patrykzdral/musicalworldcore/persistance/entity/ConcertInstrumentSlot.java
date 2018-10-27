package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
public class ConcertInstrumentSlot {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @ManyToOne
    @Getter
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @ManyToOne
    @Getter
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    boolean taken;
}
