package com.patrykzdral.musicalworldcore.services.user_reference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceDTO {
    String userFromUsername;
    String userToUsername;
    String text;
    Integer starRating;
    ZonedDateTime referenceDate;
}
