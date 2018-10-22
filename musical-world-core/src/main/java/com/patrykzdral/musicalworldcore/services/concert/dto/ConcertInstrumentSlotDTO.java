package com.patrykzdral.musicalworldcore.services.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertInstrumentSlotDTO {
    private String name;
    private String taken;
    private boolean chosen;
    private Long quantity;
}
