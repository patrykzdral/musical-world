package com.patrykzdral.musicalworldcore.controller.friendship;

import com.patrykzdral.musicalworldcore.services.friendship.dto.FriendshipListsDTO;
import com.patrykzdral.musicalworldcore.services.friendship.service.FriendshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendship")
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping(value = "get-friendship-info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public FriendshipListsDTO getFriendshipsInfo(Authentication authentication) {
        return friendshipService.getFriendshipLists(((User) authentication.getPrincipal()).getUsername());
    }

    @DeleteMapping(value = "delete-friend",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@RequestParam Long friendId, Authentication authentication) {
        friendshipService.deleteFriend(((User) authentication.getPrincipal()).getUsername(), friendId);
    }

    @GetMapping(value = "invite-friend",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void inviteFriend(@RequestParam Long friendId, Authentication authentication) {
        friendshipService.inviteFriend(((User) authentication.getPrincipal()).getUsername(), friendId);
    }

    @GetMapping(value = "accept-friend",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void acceptFriend(@RequestParam Long friendId, Authentication authentication) {
        friendshipService.acceptFriend(((User) authentication.getPrincipal()).getUsername(), friendId);
    }

    @DeleteMapping(value = "reject-invitation",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void rejectFriend(@RequestParam Long friendId, Authentication authentication) {
        friendshipService.rejectInvitation(((User) authentication.getPrincipal()).getUsername(), friendId);
    }

    @DeleteMapping(value = "cancel-invitation",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void cancelInvitation(@RequestParam Long friendId, Authentication authentication) {
        friendshipService.cancelInvitation(((User) authentication.getPrincipal()).getUsername(), friendId);
    }
}
