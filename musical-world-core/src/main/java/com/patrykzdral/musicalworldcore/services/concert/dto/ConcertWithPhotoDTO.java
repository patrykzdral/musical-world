package com.patrykzdral.musicalworldcore.services.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertWithPhotoDTO {

    private String username;
    private String name;
    private String description;
    private Date dateFrom;
    private Date dateTo;
    private AddressDTO address;
    private List<ConcertInstrumentSlotDTO> concertInstrumentSlots;
    private Long numberOfRehearsals;
    private boolean ensuredDrive;
    private boolean guaranteedMeal;
    private String picture;
}
