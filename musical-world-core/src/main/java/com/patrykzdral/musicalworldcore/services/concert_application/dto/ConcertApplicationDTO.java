package com.patrykzdral.musicalworldcore.services.concert_application.dto;

import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.ConcertInstrumentSlotDTOG;
import com.patrykzdral.musicalworldcore.services.concert.dto.get_dto.UserDTOG;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertApplicationDTO {
    ConcertInstrumentSlotApplicationDTO concertInstrumentSlot;
    String username;
    boolean accepted;
}
