package com.patrykzdral.musicalworldcore.services.instrument.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class InstrumentDTO {
    private String name;

    public InstrumentDTO(String name) {
        this.name = name;
    }
}
