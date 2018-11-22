package com.patrykzdral.musicalworldcore.services.instrument.dto;

import com.patrykzdral.musicalworldcore.persistance.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentDTO {
    private String name;
    private Type type;
}
