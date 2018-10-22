package com.patrykzdral.musicalworldcore.services.concert.dto.get_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTOG {
    private String address;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String latitude;
    private String longitude;

}
