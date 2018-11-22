package com.patrykzdral.musicalworldcore.services.user.model;

import com.patrykzdral.musicalworldcore.persistance.entity.Address;
import com.patrykzdral.musicalworldcore.persistance.entity.Picture;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithPhotoDTO {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String description;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private Address address;

    private String photo;
}
