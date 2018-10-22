package com.patrykzdral.musicalworldcore.services.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String address;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String latitude;
    private String longitude;

}
