package com.patrykzdral.musicalworldcore.services.friendship.service;

import com.patrykzdral.musicalworldcore.services.friendship.dto.FriendshipListsDTO;

public interface FriendshipService {
    void deleteFriend(String email, Long friendId);
    void inviteFriend(String email, Long friendId);
    void acceptFriend(String email, Long friendId);
    void rejectInvitation(String email, Long friendId);
    void cancelInvitation(String email, Long friendId);
    FriendshipListsDTO getFriendshipLists(String username);
}
