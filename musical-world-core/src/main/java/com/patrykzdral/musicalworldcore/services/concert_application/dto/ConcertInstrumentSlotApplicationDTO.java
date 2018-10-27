package com.patrykzdral.musicalworldcore.services.concert_application.dto;

import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.UserDTOG;
import com.patrykzdral.musicalworldcore.services.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertInstrumentSlotApplicationDTO {
    private Long id;
    private boolean taken;
    private InstrumentDTO instrument;
}
