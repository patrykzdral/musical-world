package com.patrykzdral.musicalworldcore.services.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertInstrumentSlotDeleteDTO {
    public Long id;
    public boolean taken;
}
