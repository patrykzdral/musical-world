package com.patrykzdral.musicalworldcore.services.concert.dto.get_dto;

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
public class ConcertDTOG {
    private String username;
    private String name;
    private String description;
    private Date dateFrom;
    private Date dateTo;
    private AddressDTOG address;
    private List<ConcertInstrumentSlotDTOG> concertInstrumentSlots;
    private Long numberOfRehearsals;
    private boolean ensuredDrive;
    private boolean guaranteedMeal;

}
