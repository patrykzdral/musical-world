package com.patrykzdral.musicalworldcore.services.concert.dto.get_dto;

import com.patrykzdral.musicalworldcore.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOG {
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String LastName;
    private String description;
}
