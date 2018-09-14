package com.patrykzdral.musicalworldcore.services.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrykzdral.musicalworldcore.validation.PasswordMatches;
import com.patrykzdral.musicalworldcore.validation.ValidEmail;
import com.patrykzdral.musicalworldcore.validation.ValidPassword;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
@PasswordMatches
public class RegisterUserRequestDTO {
    @NotNull
    private String username;

    @ValidEmail
    @NotNull
    @Size(min = 4)
    private String email;

    @ValidPassword
    @Size(min = 4)
    private String password;

    @NotNull
    private String matchingPassword;

    @JsonCreator
    public RegisterUserRequestDTO(@JsonProperty("username") String username,
                                  @JsonProperty("email") String email,
                                  @JsonProperty("password") String password,
                                  @JsonProperty("matchingPassword") String matchingPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }
}
