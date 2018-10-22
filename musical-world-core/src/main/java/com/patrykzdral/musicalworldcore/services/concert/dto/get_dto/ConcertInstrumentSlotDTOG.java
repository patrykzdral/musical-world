package com.patrykzdral.musicalworldcore.services.concert.dto.get_dto;

import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertInstrumentSlotDTOG {
    private boolean taken;
    private InstrumentDTO instrument;
    private UserDTOG user;
}
