package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date dateFrom;

    private Date dateTo;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;

    private Long numberOfRehearsals;

    public boolean ensuredDrive;

    public boolean guaranteedMeal;

    @OneToMany(mappedBy = "concert", cascade = {CascadeType.ALL})
    List<ConcertInstrumentSlot> concertInstrumentSlots;


}
