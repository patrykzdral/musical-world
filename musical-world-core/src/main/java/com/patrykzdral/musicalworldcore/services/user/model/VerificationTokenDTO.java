package com.patrykzdral.musicalworldcore.services.user.model;

import com.patrykzdral.musicalworldcore.persistance.entity.User;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class VerificationTokenDTO {

    private Long id;

    private String token;

    private User user;

    private Date expiryDate;
}
