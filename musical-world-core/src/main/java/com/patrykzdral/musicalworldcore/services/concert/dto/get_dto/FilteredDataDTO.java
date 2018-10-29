package com.patrykzdral.musicalworldcore.services.concert.dto.get_dto;

import com.patrykzdral.musicalworldcore.persistance.entity.Instrument;
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
public class FilteredDataDTO {
    String name;
    List<Instrument> instruments;
    Date dateFrom;
    Date dateTo;
}
