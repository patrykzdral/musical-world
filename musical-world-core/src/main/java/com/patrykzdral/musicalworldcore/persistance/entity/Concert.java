package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Size(min = 2, max = 1000)
    @NotNull
    @Getter
    private String name;

    @Size(max = 2000)
    @Getter
    private String description;

    @Getter
    private Date dateFrom;

    @Getter
    private Date dateTo;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
    @Getter
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Getter
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "picture_id" )
    @Getter
    private Picture picture;

    @Getter
    private Long numberOfRehearsals;

    @Getter
    private boolean ensuredDrive;

    @Getter
    private boolean guaranteedMeal;

    @OneToMany(mappedBy = "concert", cascade = {CascadeType.ALL})
    @Getter
    List<ConcertInstrumentSlot> concertInstrumentSlots;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    List<ConcertApplication> concertApplications;
}
