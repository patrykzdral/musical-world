package com.patrykzdral.musicalworldcore.services.friendship.dto;

import com.patrykzdral.musicalworldcore.persistance.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendWithPhotoDTO {
    private Long friendshipId;
    private Long friendId;
    private String username;
    private String email;
    private String description;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Address address;
    private String photo;
}
