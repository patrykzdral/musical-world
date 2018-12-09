package com.patrykzdral.musicalworldcore.services.friendship.dto;

import com.patrykzdral.musicalworldcore.services.user.model.UserWithPhotoDTO;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@Builder
@ToString
public class FriendshipListsDTO {
    List<FriendWithPhotoDTO> friends;
    List<FriendWithPhotoDTO> friendsIDidNotAccept;
    List<FriendWithPhotoDTO> friendsWhoDidNotAcceptMe;
    List<FriendWithPhotoDTO> outsideOfFriendshipsUsers;
}
